package rw.ac.auca.budget;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import rw.ac.auca.budget.dto.BudgetSummaryResponse;
import rw.ac.auca.budget.dto.ContributionResponse;
import rw.ac.auca.budget.dto.CreateExpenseRequest;
import rw.ac.auca.budget.dto.ExpenseResponse;
import rw.ac.auca.budget.dto.MomoWebhookPayload;
import rw.ac.auca.task.VisibilityScope;
import rw.ac.auca.user.Role;
import rw.ac.auca.user.User;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;
import rw.ac.auca.wedding.WeddingSide;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private BudgetCategoryRepository budgetCategoryRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ContributionRepository contributionRepository;

    @Mock
    private WeddingRepository weddingRepository;

    @Mock
    private WeddingMemberRepository weddingMemberRepository;

    @InjectMocks
    private BudgetService budgetService;

    private User brideUser;
    private User groomUser;
    private Wedding wedding;
    private Budget budget;
    private Expense sharedExpense;
    private Expense brideExpense;

    @BeforeEach
    void setUp() {
        brideUser = User.builder()
                .firstName("Divine")
                .lastName("Mutesi")
                .email("divine@wedding.rw")
                .role(Role.ROLE_BRIDE)
                .build();
        brideUser.setId(1L);

        groomUser = User.builder()
                .firstName("Jean")
                .lastName("Mugisha")
                .email("jean@wedding.rw")
                .role(Role.ROLE_GROOM)
                .build();
        groomUser.setId(2L);

        wedding = Wedding.builder()
                .title("Divine & Jean Ubukwe")
                .targetBudget(new BigDecimal("15000000.00"))
                .bride(brideUser)
                .groom(groomUser)
                .build();
        wedding.setId(10L);

        budget = Budget.builder()
                .wedding(wedding)
                .totalPlannedRwf(new BigDecimal("15000000.00"))
                .build();
        budget.setId(100L);

        sharedExpense = Expense.builder()
                .wedding(wedding)
                .title("Catering Advance")
                .amountRwf(new BigDecimal("3000000.00"))
                .paidByUser(brideUser)
                .dateSpent(LocalDate.now())
                .visibilityScope(VisibilityScope.SHARED)
                .build();
        sharedExpense.setId(501L);

        brideExpense = Expense.builder()
                .wedding(wedding)
                .title("Bride Gown Purchase Private")
                .amountRwf(new BigDecimal("1200000.00"))
                .paidByUser(brideUser)
                .dateSpent(LocalDate.now())
                .visibilityScope(VisibilityScope.BRIDE_PRIVATE)
                .build();
        brideExpense.setId(502L);
    }

    @Test
    void getBudgetSummary_CalculatesPlannedSpentAndRemainingRwf() {
        when(weddingMemberRepository.findByUserId(1L)).thenReturn(List.of(
                WeddingMember.builder().wedding(wedding).user(brideUser).side(WeddingSide.BRIDE_SIDE).build()
        ));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(
                WeddingMember.builder().wedding(wedding).user(brideUser).side(WeddingSide.BRIDE_SIDE).build()
        ));
        when(budgetRepository.findByWeddingId(10L)).thenReturn(Optional.of(budget));
        when(expenseRepository.findByWeddingIdAndVisibilityScopeInAndDeletedAtIsNull(eq(10L), anyList()))
                .thenReturn(List.of(sharedExpense, brideExpense));
        when(contributionRepository.findByWeddingIdAndStatus(10L, ContributionStatus.COMPLETED))
                .thenReturn(List.of(
                        Contribution.builder().wedding(wedding).amountRwf(new BigDecimal("500000.00")).status(ContributionStatus.COMPLETED).build()
                ));

        BudgetSummaryResponse summary = budgetService.getBudgetSummary(brideUser);

        assertNotNull(summary);
        assertEquals(new BigDecimal("15000000.00"), summary.getTotalPlannedRwf());
        assertEquals(new BigDecimal("4200000.00"), summary.getTotalSpentRwf());
        assertEquals(new BigDecimal("10800000.00"), summary.getRemainingRwf());
        assertEquals(new BigDecimal("500000.00"), summary.getTotalContributionsRwf());
    }

    @Test
    void createExpense_ThrowsException_WhenAmountIsNonPositive() {
        CreateExpenseRequest req = CreateExpenseRequest.builder()
                .weddingId(10L)
                .title("Invalid Expense")
                .amountRwf(new BigDecimal("0.00"))
                .dateSpent(LocalDate.now())
                .build();

        when(weddingRepository.findById(10L)).thenReturn(Optional.of(wedding));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(
                WeddingMember.builder().wedding(wedding).user(brideUser).side(WeddingSide.BRIDE_SIDE).build()
        ));

        assertThrows(IllegalArgumentException.class, () -> budgetService.createExpense(req, brideUser));
    }

    @Test
    void createExpense_GroomUser_DeniedBridePrivateExpense() {
        CreateExpenseRequest req = CreateExpenseRequest.builder()
                .weddingId(10L)
                .title("Bride Private Expense")
                .amountRwf(new BigDecimal("500000.00"))
                .dateSpent(LocalDate.now())
                .visibilityScope(VisibilityScope.BRIDE_PRIVATE)
                .build();

        when(weddingRepository.findById(10L)).thenReturn(Optional.of(wedding));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 2L)).thenReturn(Optional.of(
                WeddingMember.builder().wedding(wedding).user(groomUser).side(WeddingSide.GROOM_SIDE).build()
        ));

        assertThrows(AccessDeniedException.class, () -> budgetService.createExpense(req, groomUser));
    }

    @Test
    void deleteExpense_SoftDeletes_AndRestoreClearsDeletedAt() {
        when(expenseRepository.findByIdAndDeletedAtIsNull(501L)).thenReturn(Optional.of(sharedExpense));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(
                WeddingMember.builder().wedding(wedding).user(brideUser).side(WeddingSide.BRIDE_SIDE).build()
        ));

        budgetService.deleteExpense(501L, brideUser);
        verify(expenseRepository, times(1)).save(sharedExpense);
        assertNotNull(sharedExpense.getDeletedAt());

        when(expenseRepository.findById(501L)).thenReturn(Optional.of(sharedExpense));
        when(expenseRepository.save(sharedExpense)).thenReturn(sharedExpense);

        ExpenseResponse restored = budgetService.restoreExpense(501L, brideUser);
        assertNotNull(restored);
        assertNull(sharedExpense.getDeletedAt());
    }

    @Test
    void processMomoWebhook_CreatesCompletedContribution() {
        MomoWebhookPayload payload = MomoWebhookPayload.builder()
                .weddingId(10L)
                .contributorName("Uncle Patrick")
                .contributorPhone("+250788123456")
                .amountRwf(new BigDecimal("200000.00"))
                .momoTransactionId("MOMO-TX-998877")
                .status(ContributionStatus.COMPLETED)
                .build();

        when(weddingRepository.findById(10L)).thenReturn(Optional.of(wedding));
        when(contributionRepository.findByMomoTransactionId("MOMO-TX-998877")).thenReturn(Optional.empty());
        when(contributionRepository.save(any(Contribution.class))).thenAnswer(i -> {
            Contribution c = i.getArgument(0);
            c.setId(901L);
            return c;
        });

        ContributionResponse resp = budgetService.processMomoWebhook(payload);

        assertNotNull(resp);
        assertEquals("Uncle Patrick", resp.getContributorName());
        assertEquals(new BigDecimal("200000.00"), resp.getAmountRwf());
        assertEquals(ContributionStatus.COMPLETED, resp.getStatus());
    }
}
