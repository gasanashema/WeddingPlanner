package rw.ac.auca.task.dto;

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
public class UpdateTaskRequest {

    private String title;

    private String description;

    private Long ceremonyId;

    private Long assignedUserId;

    private TaskPriority priority;

    private TaskStatus status;

    private VisibilityScope visibilityScope;

    private LocalDate dueDate;

    private BigDecimal estimatedBudget;
}
