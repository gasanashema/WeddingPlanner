package rw.ac.auca.budget;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rw.ac.auca.budget.dto.ContributionResponse;
import rw.ac.auca.budget.dto.MomoWebhookPayload;
import rw.ac.auca.common.ApiResponse;

@RestController
@RequestMapping("/api/v1/public/contributions")
@RequiredArgsConstructor
public class PublicContributionController {

    private final BudgetService budgetService;

    @PostMapping("/webhook")
    public ResponseEntity<ApiResponse<ContributionResponse>> processMomoWebhook(
            @Valid @RequestBody MomoWebhookPayload payload) {
        try {
            ContributionResponse response = budgetService.processMomoWebhook(payload);
            return ResponseEntity.ok(ApiResponse.success("Mobile Money contribution webhook processed successfully", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
