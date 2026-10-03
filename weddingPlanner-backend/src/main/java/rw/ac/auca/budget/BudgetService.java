package rw.ac.auca.budget;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.auca.budget.dto.BudgetCategoryResponse;
import rw.ac.auca.budget.dto.BudgetSummaryResponse;
import rw.ac.auca.budget.dto.ContributionResponse;
import rw.ac.auca.budget.dto.CreateBudgetCategoryRequest;
import rw.ac.auca.budget.dto.CreateExpenseRequest;
import rw.ac.auca.budget.dto.ExpenseResponse;
import rw.ac.auca.budget.dto.MomoWebhookPayload;
import rw.ac.auca.budget.dto.UpdateExpenseRequest;
import rw.ac.auca.ceremony.WeddingCeremony;
import rw.ac.auca.ceremony.WeddingCeremonyRepository;
import rw.ac.auca.task.VisibilityScope;
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;
import rw.ac.auca.wedding.WeddingSide;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetCategoryRepository budgetCategoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ContributionRepository contributionRepository;
    private final WeddingRepository weddingRepository;
    private final WeddingMemberRepository weddingMemberRepository;
    private final WeddingCeremonyRepository ceremonyRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public BudgetSummaryResponse getBudgetSummary(User currentUser) {
        Wedding wedding = getUserActiveWedding(currentUser);
        if (wedding == null) {
            return BudgetSummaryResponse.builder()
                    .totalPlannedRwf(BigDecimal.ZERO)
                    .totalSpentRwf(BigDecimal.ZERO)
                    .remainingRwf(BigDecimal.ZERO)
                    .totalContributionsRwf(BigDecimal.ZERO)
                    .categories(List.of())
                    .expenses(List.of())
                    .build();
        }

        WeddingSide userSide = getUserSideInWedding(wedding.getId(), currentUser);
        List<VisibilityScope> allowedScopes = getAllowedScopes(userSide);

        Budget budget = budgetRepository.findByWeddingId(wedding.getId())
                .orElseGet(() -> initDefaultBudget(wedding));

        List<Expense> activeExpenses = expenseRepository.findByWeddingIdAndVisibilityScopeInAndDeletedAtIsNull(
                wedding.getId(), allowedScopes
        );

        BigDecimal totalSpent = activeExpenses.stream()
                .map(Expense::getAmountRwf)
                .filter(amt -> amt != null && amt.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Sum category allocations
        BigDecimal totalPlanned = budget.getCategories().stream()
                .filter(c -> canUserAccessScope(userSide, c.getVisibilityScope()))
                .map(BudgetCategory::getAllocatedAmountRwf)
                .filter(amt -> amt != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPlanned.compareTo(BigDecimal.ZERO) == 0 && wedding.getTargetBudget() != null) {
            totalPlanned = wedding.getTargetBudget();
        }

        BigDecimal remaining = totalPlanned.subtract(totalSpent);

        // Calculate contributions
        List<Contribution> completedContributions = contributionRepository.findByWeddingIdAndStatus(
                wedding.getId(), ContributionStatus.COMPLETED
        );
        BigDecimal totalContributions = completedContributions.stream()
                .map(Contribution::getAmountRwf)
                .filter(amt -> amt != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Map category breakdowns
        Map<Long, BigDecimal> categorySpentMap = new HashMap<>();
        for (Expense expense : activeExpenses) {
            if (expense.getBudgetCategory() != null) {
                Long catId = expense.getBudgetCategory().getId();
                BigDecimal currentCatSpent = categorySpentMap.getOrDefault(catId, BigDecimal.ZERO);
                categorySpentMap.put(catId, currentCatSpent.add(expense.getAmountRwf()));
            }
        }

        List<BudgetCategoryResponse> categoryResponses = budget.getCategories().stream()
                .filter(c -> canUserAccessScope(userSide, c.getVisibilityScope()))
                .map(c -> BudgetCategoryResponse.fromEntity(c, categorySpentMap.getOrDefault(c.getId(), BigDecimal.ZERO)))
                .toList();

        List<ExpenseResponse> expenseResponses = activeExpenses.stream()
                .map(ExpenseResponse::fromEntity)
                .toList();

        return BudgetSummaryResponse.builder()
                .weddingId(wedding.getId())
                .totalPlannedRwf(totalPlanned)
                .totalSpentRwf(totalSpent)
                .remainingRwf(remaining)
                .totalContributionsRwf(totalContributions)
                .categories(categoryResponses)
                .expenses(expenseResponses)
                .build();
    }

    public BudgetCategoryResponse createBudgetCategory(CreateBudgetCategoryRequest request, User currentUser) {
        Wedding wedding = resolveWeddingForUser(request.getWeddingId(), currentUser);
        WeddingSide userSide = getUserSideInWedding(wedding.getId(), currentUser);
        VisibilityScope scope = request.getVisibilityScope() != null ? request.getVisibilityScope() : VisibilityScope.SHARED;

        if (!canUserAccessScope(userSide, scope)) {
            throw new AccessDeniedException("Forbidden: Cannot create private budget category for opposite side.");
        }

        Budget budget = budgetRepository.findByWeddingId(wedding.getId())
                .orElseGet(() -> initDefaultBudget(wedding));

        BudgetCategory category = BudgetCategory.builder()
                .budget(budget)
                .name(request.getName())
                .allocatedAmountRwf(request.getAllocatedAmountRwf() != null ? request.getAllocatedAmountRwf() : BigDecimal.ZERO)
                .visibilityScope(scope)
                .build();

        BudgetCategory saved = budgetCategoryRepository.save(category);

        // Recalculate total planned budget
        budget.setTotalPlannedRwf(budget.getTotalPlannedRwf().add(saved.getAllocatedAmountRwf()));
        budgetRepository.save(budget);

        return BudgetCategoryResponse.fromEntity(saved, BigDecimal.ZERO);
    }

    public ExpenseResponse createExpense(CreateExpenseRequest request, User currentUser) {
        Wedding wedding = resolveWeddingForUser(request.getWeddingId(), currentUser);
        WeddingSide userSide = getUserSideInWedding(wedding.getId(), currentUser);
        VisibilityScope scope = request.getVisibilityScope() != null ? request.getVisibilityScope() : VisibilityScope.SHARED;

        if (!canUserAccessScope(userSide, scope)) {
            throw new AccessDeniedException("Forbidden: Cannot create private expense for opposite side.");
        }

        if (request.getAmountRwf() == null || request.getAmountRwf().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Expense amount must be positive and greater than zero.");
        }

        WeddingCeremony ceremony = null;
        if (request.getCeremonyId() != null) {
            ceremony = ceremonyRepository.findById(request.getCeremonyId()).orElse(null);
        }

        BudgetCategory category = null;
        if (request.getBudgetCategoryId() != null) {
            category = budgetCategoryRepository.findById(request.getBudgetCategoryId()).orElse(null);
        }

        User paidByUser = currentUser;
        if (request.getPaidByUserId() != null) {
            paidByUser = userRepository.findById(request.getPaidByUserId()).orElse(currentUser);
        }

        Expense expense = Expense.builder()
                .wedding(wedding)
                .ceremony(ceremony)
                .budgetCategory(category)
                .title(request.getTitle())
                .amountRwf(request.getAmountRwf())
                .paidByUser(paidByUser)
                .dateSpent(request.getDateSpent())
                .visibilityScope(scope)
                .receiptNotes(request.getReceiptNotes())
                .build();

        Expense saved = expenseRepository.save(expense);
        return ExpenseResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesForUser(User currentUser) {
        Wedding wedding = getUserActiveWedding(currentUser);
        if (wedding == null) {
            return List.of();
        }

        WeddingSide userSide = getUserSideInWedding(wedding.getId(), currentUser);
        List<VisibilityScope> allowedScopes = getAllowedScopes(userSide);

        return expenseRepository.findByWeddingIdAndVisibilityScopeInAndDeletedAtIsNull(wedding.getId(), allowedScopes)
                .stream()
                .map(ExpenseResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(Long id, User currentUser) {
        Expense expense = expenseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

        WeddingSide userSide = getUserSideInWedding(expense.getWedding().getId(), currentUser);
        if (!canUserAccessScope(userSide, expense.getVisibilityScope())) {
            throw new AccessDeniedException("Forbidden: Access denied to private expense of the opposite side.");
        }

        return ExpenseResponse.fromEntity(expense);
    }

    public ExpenseResponse updateExpense(Long id, UpdateExpenseRequest request, User currentUser) {
        Expense expense = expenseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

        WeddingSide userSide = getUserSideInWedding(expense.getWedding().getId(), currentUser);
        if (!canUserAccessScope(userSide, expense.getVisibilityScope())) {
            throw new AccessDeniedException("Forbidden: Access denied to private expense of the opposite side.");
        }

        if (request.getAmountRwf() != null && request.getAmountRwf().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Expense amount must be positive and greater than zero.");
        }

        if (request.getTitle() != null) {
            expense.setTitle(request.getTitle());
        }
        if (request.getAmountRwf() != null) {
            expense.setAmountRwf(request.getAmountRwf());
        }
        if (request.getCeremonyId() != null) {
            WeddingCeremony ceremony = ceremonyRepository.findById(request.getCeremonyId()).orElse(null);
            expense.setCeremony(ceremony);
        }
        if (request.getBudgetCategoryId() != null) {
            BudgetCategory category = budgetCategoryRepository.findById(request.getBudgetCategoryId()).orElse(null);
            expense.setBudgetCategory(category);
        }
        if (request.getPaidByUserId() != null) {
            User paidBy = userRepository.findById(request.getPaidByUserId()).orElse(null);
            expense.setPaidByUser(paidBy);
        }
        if (request.getDateSpent() != null) {
            expense.setDateSpent(request.getDateSpent());
        }
        if (request.getVisibilityScope() != null) {
            if (!canUserAccessScope(userSide, request.getVisibilityScope())) {
                throw new AccessDeniedException("Forbidden: Cannot change visibility scope to opposite side private.");
            }
            expense.setVisibilityScope(request.getVisibilityScope());
        }
        if (request.getReceiptNotes() != null) {
            expense.setReceiptNotes(request.getReceiptNotes());
        }

        Expense updated = expenseRepository.save(expense);
        return ExpenseResponse.fromEntity(updated);
    }

    public void deleteExpense(Long id, User currentUser) {
        Expense expense = expenseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

        WeddingSide userSide = getUserSideInWedding(expense.getWedding().getId(), currentUser);
        if (!canUserAccessScope(userSide, expense.getVisibilityScope())) {
            throw new AccessDeniedException("Forbidden: Access denied to private expense of the opposite side.");
        }

        expense.setDeletedAt(LocalDateTime.now());
        expenseRepository.save(expense);
    }

    public ExpenseResponse restoreExpense(Long id, User currentUser) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

        WeddingSide userSide = getUserSideInWedding(expense.getWedding().getId(), currentUser);
        if (!canUserAccessScope(userSide, expense.getVisibilityScope())) {
            throw new AccessDeniedException("Forbidden: Access denied to private expense of the opposite side.");
        }

        expense.setDeletedAt(null);
        Expense restored = expenseRepository.save(expense);
        return ExpenseResponse.fromEntity(restored);
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> getDeletedExpenses(User currentUser) {
        Wedding wedding = getUserActiveWedding(currentUser);
        if (wedding == null) {
            return List.of();
        }

        WeddingSide userSide = getUserSideInWedding(wedding.getId(), currentUser);

        return expenseRepository.findDeletedExpensesByWeddingId(wedding.getId()).stream()
                .filter(expense -> canUserAccessScope(userSide, expense.getVisibilityScope()))
                .map(ExpenseResponse::fromEntity)
                .toList();
    }

    public ContributionResponse processMomoWebhook(MomoWebhookPayload payload) {
        Wedding wedding = weddingRepository.findById(payload.getWeddingId())
                .orElseThrow(() -> new RuntimeException("Wedding not found with id: " + payload.getWeddingId()));

        if (payload.getAmountRwf() == null || payload.getAmountRwf().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Contribution amount must be positive.");
        }

        Optional<Contribution> existing = contributionRepository.findByMomoTransactionId(payload.getMomoTransactionId());

        Contribution contribution;
        if (existing.isPresent()) {
            contribution = existing.get();
            if (payload.getStatus() != null) {
                contribution.setStatus(payload.getStatus());
            }
        } else {
            ContributionStatus status = payload.getStatus() != null ? payload.getStatus() : ContributionStatus.COMPLETED;
            contribution = Contribution.builder()
                    .wedding(wedding)
                    .contributorName(payload.getContributorName())
                    .contributorPhone(payload.getContributorPhone())
                    .amountRwf(payload.getAmountRwf())
                    .momoTransactionId(payload.getMomoTransactionId())
                    .status(status)
                    .paymentDate(LocalDateTime.now())
                    .build();
        }

        Contribution saved = contributionRepository.save(contribution);
        return ContributionResponse.fromEntity(saved);
    }

    private Budget initDefaultBudget(Wedding wedding) {
        Budget budget = Budget.builder()
                .wedding(wedding)
                .totalPlannedRwf(wedding.getTargetBudget() != null ? wedding.getTargetBudget() : new BigDecimal("15000000.00"))
                .build();
        Budget savedBudget = budgetRepository.save(budget);

        List<BudgetCategory> defaultCategories = List.of(
                BudgetCategory.builder().budget(savedBudget).name("Venue & Logistics").allocatedAmountRwf(new BigDecimal("3500000.00")).visibilityScope(VisibilityScope.SHARED).build(),
                BudgetCategory.builder().budget(savedBudget).name("Food & Catering").allocatedAmountRwf(new BigDecimal("4000000.00")).visibilityScope(VisibilityScope.SHARED).build(),
                BudgetCategory.builder().budget(savedBudget).name("Decoration & Flowers").allocatedAmountRwf(new BigDecimal("2000000.00")).visibilityScope(VisibilityScope.SHARED).build(),
                BudgetCategory.builder().budget(savedBudget).name("Attire & Beauty").allocatedAmountRwf(new BigDecimal("1800000.00")).visibilityScope(VisibilityScope.SHARED).build(),
                BudgetCategory.builder().budget(savedBudget).name("Photography & Video").allocatedAmountRwf(new BigDecimal("1500000.00")).visibilityScope(VisibilityScope.SHARED).build(),
                BudgetCategory.builder().budget(savedBudget).name("Home Preparation").allocatedAmountRwf(new BigDecimal("2200000.00")).visibilityScope(VisibilityScope.SHARED).build()
        );

        budgetCategoryRepository.saveAll(defaultCategories);
        savedBudget.setCategories(new ArrayList<>(defaultCategories));
        return savedBudget;
    }

    private Wedding resolveWeddingForUser(Long requestedWeddingId, User currentUser) {
        if (requestedWeddingId != null) {
            return weddingRepository.findById(requestedWeddingId)
                    .orElseThrow(() -> new RuntimeException("Wedding not found with id: " + requestedWeddingId));
        }
        Wedding active = getUserActiveWedding(currentUser);
        if (active == null) {
            throw new IllegalArgumentException("User has no active wedding workspace.");
        }
        return active;
    }

    private Wedding getUserActiveWedding(User currentUser) {
        if (currentUser == null) return null;
        List<WeddingMember> memberships = weddingMemberRepository.findByUserId(currentUser.getId());
        return memberships.isEmpty() ? null : memberships.get(0).getWedding();
    }

    private WeddingSide getUserSideInWedding(Long weddingId, User currentUser) {
        if (currentUser == null) return WeddingSide.SHARED;
        return weddingMemberRepository.findByWeddingIdAndUserId(weddingId, currentUser.getId())
                .map(WeddingMember::getSide)
                .orElse(WeddingSide.SHARED);
    }

    private List<VisibilityScope> getAllowedScopes(WeddingSide userSide) {
        if (userSide == WeddingSide.BRIDE_SIDE) {
            return List.of(VisibilityScope.BRIDE_PRIVATE, VisibilityScope.SHARED);
        } else if (userSide == WeddingSide.GROOM_SIDE) {
            return List.of(VisibilityScope.GROOM_PRIVATE, VisibilityScope.SHARED);
        }
        return List.of(VisibilityScope.SHARED);
    }

    private boolean canUserAccessScope(WeddingSide userSide, VisibilityScope scope) {
        if (scope == VisibilityScope.SHARED) return true;
        if (userSide == WeddingSide.BRIDE_SIDE && scope == VisibilityScope.BRIDE_PRIVATE) return true;
        if (userSide == WeddingSide.GROOM_SIDE && scope == VisibilityScope.GROOM_PRIVATE) return true;
        return false;
    }
}
