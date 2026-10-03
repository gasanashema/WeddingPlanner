package rw.ac.auca.budget;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rw.ac.auca.budget.dto.BudgetCategoryResponse;
import rw.ac.auca.budget.dto.BudgetSummaryResponse;
import rw.ac.auca.budget.dto.CreateBudgetCategoryRequest;
import rw.ac.auca.common.ApiResponse;
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;

@RestController
@RequestMapping("/api/v1/budget")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<BudgetSummaryResponse>> getBudgetSummary(Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        BudgetSummaryResponse summary = budgetService.getBudgetSummary(currentUser);
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<BudgetCategoryResponse>> createBudgetCategory(
            @Valid @RequestBody CreateBudgetCategoryRequest request,
            Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        try {
            BudgetCategoryResponse category = budgetService.createBudgetCategory(request, currentUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Budget category created successfully", category));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElse(null);
    }
}
