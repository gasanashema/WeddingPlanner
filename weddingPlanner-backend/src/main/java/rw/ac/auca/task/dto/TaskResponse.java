package rw.ac.auca.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.task.Task;
import rw.ac.auca.task.TaskPriority;
import rw.ac.auca.task.TaskStatus;
import rw.ac.auca.task.VisibilityScope;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    private Long id;
    private Long weddingId;
    private Long ceremonyId;
    private String ceremonyName;
    private Long assignedUserId;
    private String assignedUserName;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private VisibilityScope visibilityScope;
    private LocalDate dueDate;
    private BigDecimal estimatedBudget;
    private LocalDateTime deletedAt;
    private Instant createdAt;
    private Instant updatedAt;

    public static TaskResponse fromEntity(Task task) {
        if (task == null) return null;

        String ceremonyName = task.getCeremony() != null ? task.getCeremony().getName() : null;
        String assignedUserName = task.getAssignedUser() != null 
                ? task.getAssignedUser().getFirstName() + " " + task.getAssignedUser().getLastName()
                : null;

        return TaskResponse.builder()
                .id(task.getId())
                .weddingId(task.getWedding() != null ? task.getWedding().getId() : null)
                .ceremonyId(task.getCeremony() != null ? task.getCeremony().getId() : null)
                .ceremonyName(ceremonyName)
                .assignedUserId(task.getAssignedUser() != null ? task.getAssignedUser().getId() : null)
                .assignedUserName(assignedUserName)
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .status(task.getStatus())
                .visibilityScope(task.getVisibilityScope())
                .dueDate(task.getDueDate())
                .estimatedBudget(task.getEstimatedBudget())
                .deletedAt(task.getDeletedAt())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
