package rw.ac.auca.task.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.task.TaskPriority;
import rw.ac.auca.task.TaskStatus;
import rw.ac.auca.task.VisibilityScope;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

    private Long weddingId;

    private Long ceremonyId;

    private Long assignedUserId;

    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    private TaskPriority priority;

    private TaskStatus status;

    private VisibilityScope visibilityScope;

    private LocalDate dueDate;

    private BigDecimal estimatedBudget;
}
