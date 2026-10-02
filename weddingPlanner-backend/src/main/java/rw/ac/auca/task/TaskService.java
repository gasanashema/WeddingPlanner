package rw.ac.auca.task;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.auca.ceremony.WeddingCeremony;
import rw.ac.auca.ceremony.WeddingCeremonyRepository;
import rw.ac.auca.task.dto.CreateTaskRequest;
import rw.ac.auca.task.dto.TaskResponse;
import rw.ac.auca.task.dto.UpdateTaskRequest;
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;
import rw.ac.auca.wedding.WeddingSide;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final WeddingRepository weddingRepository;
    private final WeddingMemberRepository weddingMemberRepository;
    private final WeddingCeremonyRepository ceremonyRepository;
    private final UserRepository userRepository;

    public TaskResponse createTask(CreateTaskRequest request, User currentUser) {
        Wedding wedding = resolveWeddingForUser(request.getWeddingId(), currentUser);
        WeddingSide userSide = getUserSideInWedding(wedding.getId(), currentUser);

        VisibilityScope scope = request.getVisibilityScope() != null ? request.getVisibilityScope() : VisibilityScope.SHARED;

        // Ensure user cannot create private task for the opposite side
        if (!canUserAccessTaskScope(userSide, scope)) {
            throw new AccessDeniedException("Forbidden: You cannot create a private task for the opposite side.");
        }

        WeddingCeremony ceremony = null;
        if (request.getCeremonyId() != null) {
            ceremony = ceremonyRepository.findById(request.getCeremonyId()).orElse(null);
        }

        User assignedUser = null;
        if (request.getAssignedUserId() != null) {
            assignedUser = userRepository.findById(request.getAssignedUserId()).orElse(null);
        }

        Task task = Task.builder()
                .wedding(wedding)
                .ceremony(ceremony)
                .assignedUser(assignedUser)
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM)
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.PENDING)
                .visibilityScope(scope)
                .dueDate(request.getDueDate())
                .estimatedBudget(request.getEstimatedBudget())
                .build();

        Task savedTask = taskRepository.save(task);
        return TaskResponse.fromEntity(savedTask);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksForUser(User currentUser) {
        Wedding wedding = getUserActiveWedding(currentUser);
        if (wedding == null) {
            return List.of();
        }

        WeddingSide userSide = getUserSideInWedding(wedding.getId(), currentUser);
        List<VisibilityScope> allowedScopes = getAllowedScopes(userSide);

        return taskRepository.findByWeddingIdAndVisibilityScopeInAndDeletedAtIsNull(wedding.getId(), allowedScopes)
                .stream()
                .map(TaskResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id, User currentUser) {
        Task task = taskRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        WeddingSide userSide = getUserSideInWedding(task.getWedding().getId(), currentUser);
        if (!canUserAccessTaskScope(userSide, task.getVisibilityScope())) {
            throw new AccessDeniedException("Forbidden: Access denied to private task of the opposite side.");
        }

        return TaskResponse.fromEntity(task);
    }

    public TaskResponse updateTask(Long id, UpdateTaskRequest request, User currentUser) {
        Task task = taskRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        WeddingSide userSide = getUserSideInWedding(task.getWedding().getId(), currentUser);
        if (!canUserAccessTaskScope(userSide, task.getVisibilityScope())) {
            throw new AccessDeniedException("Forbidden: Cannot modify private task of the opposite side.");
        }

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
        if (request.getEstimatedBudget() != null) task.setEstimatedBudget(request.getEstimatedBudget());

        if (request.getVisibilityScope() != null) {
            if (!canUserAccessTaskScope(userSide, request.getVisibilityScope())) {
                throw new AccessDeniedException("Forbidden: Cannot change task visibility scope to opposite private side.");
            }
            task.setVisibilityScope(request.getVisibilityScope());
        }

        if (request.getCeremonyId() != null) {
            WeddingCeremony ceremony = ceremonyRepository.findById(request.getCeremonyId()).orElse(null);
            task.setCeremony(ceremony);
        }

        if (request.getAssignedUserId() != null) {
            User assignedUser = userRepository.findById(request.getAssignedUserId()).orElse(null);
            task.setAssignedUser(assignedUser);
        }

        Task updatedTask = taskRepository.save(task);
        return TaskResponse.fromEntity(updatedTask);
    }

    public void deleteTask(Long id, User currentUser) {
        Task task = taskRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        WeddingSide userSide = getUserSideInWedding(task.getWedding().getId(), currentUser);
        if (!canUserAccessTaskScope(userSide, task.getVisibilityScope())) {
            throw new AccessDeniedException("Forbidden: Cannot delete private task of the opposite side.");
        }

        // Soft delete
        task.setDeletedAt(LocalDateTime.now());
        taskRepository.save(task);
    }

    public TaskResponse restoreTask(Long id, User currentUser) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        WeddingSide userSide = getUserSideInWedding(task.getWedding().getId(), currentUser);
        if (!canUserAccessTaskScope(userSide, task.getVisibilityScope())) {
            throw new AccessDeniedException("Forbidden: Cannot restore private task of the opposite side.");
        }

        task.setDeletedAt(null);
        Task restoredTask = taskRepository.save(task);
        return TaskResponse.fromEntity(restoredTask);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getDeletedTasks(User currentUser) {
        Wedding wedding = getUserActiveWedding(currentUser);
        if (wedding == null) return List.of();

        WeddingSide userSide = getUserSideInWedding(wedding.getId(), currentUser);

        return taskRepository.findDeletedTasksByWeddingId(wedding.getId()).stream()
                .filter(task -> canUserAccessTaskScope(userSide, task.getVisibilityScope()))
                .map(TaskResponse::fromEntity)
                .toList();
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

    private boolean canUserAccessTaskScope(WeddingSide userSide, VisibilityScope taskScope) {
        if (taskScope == VisibilityScope.SHARED) return true;
        if (userSide == WeddingSide.BRIDE_SIDE && taskScope == VisibilityScope.BRIDE_PRIVATE) return true;
        if (userSide == WeddingSide.GROOM_SIDE && taskScope == VisibilityScope.GROOM_PRIVATE) return true;
        return false;
    }
}
