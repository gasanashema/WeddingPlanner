package rw.ac.auca.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import rw.ac.auca.task.dto.CreateTaskRequest;
import rw.ac.auca.task.dto.TaskResponse;

import rw.ac.auca.user.Role;
import rw.ac.auca.user.User;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;
import rw.ac.auca.wedding.WeddingSide;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private WeddingRepository weddingRepository;

    @Mock
    private WeddingMemberRepository weddingMemberRepository;

    @InjectMocks
    private TaskService taskService;

    private User brideUser;
    private User groomUser;
    private Wedding wedding;
    private Task brideTask;
    private Task groomTask;
    private Task sharedTask;

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
                .bride(brideUser)
                .groom(groomUser)
                .build();
        wedding.setId(10L);

        brideTask = Task.builder()
                .wedding(wedding)
                .title("Bride Gown Fitting Private")
                .visibilityScope(VisibilityScope.BRIDE_PRIVATE)
                .build();
        brideTask.setId(101L);

        groomTask = Task.builder()
                .wedding(wedding)
                .title("Groom Suit Fitting Private")
                .visibilityScope(VisibilityScope.GROOM_PRIVATE)
                .build();
        groomTask.setId(102L);

        sharedTask = Task.builder()
                .wedding(wedding)
                .title("Church Venue Confirmation")
                .visibilityScope(VisibilityScope.SHARED)
                .build();
        sharedTask.setId(103L);
    }

    @Test
    void brideUser_Gets_Only_BridePrivate_And_SharedTasks() {
        when(weddingMemberRepository.findByUserId(1L)).thenReturn(List.of(
                WeddingMember.builder().wedding(wedding).user(brideUser).side(WeddingSide.BRIDE_SIDE).build()
        ));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(
                WeddingMember.builder().wedding(wedding).user(brideUser).side(WeddingSide.BRIDE_SIDE).build()
        ));
        when(taskRepository.findByWeddingIdAndVisibilityScopeInAndDeletedAtIsNull(eq(10L), anyList()))
                .thenReturn(List.of(brideTask, sharedTask));

        List<TaskResponse> tasks = taskService.getTasksForUser(brideUser);

        assertEquals(2, tasks.size());
        assertTrue(tasks.stream().allMatch(t -> t.getVisibilityScope() != VisibilityScope.GROOM_PRIVATE));
    }

    @Test
    void groomUser_Attempting_To_Access_BridePrivateTask_ThrowsAccessDeniedException() {
        when(taskRepository.findByIdAndDeletedAtIsNull(101L)).thenReturn(Optional.of(brideTask));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 2L)).thenReturn(Optional.of(
                WeddingMember.builder().wedding(wedding).user(groomUser).side(WeddingSide.GROOM_SIDE).build()
        ));

        assertThrows(AccessDeniedException.class, () -> taskService.getTaskById(101L, groomUser));
    }

    @Test
    void softDeleteTask_SetsDeletedAtTimestamp() {
        when(taskRepository.findByIdAndDeletedAtIsNull(103L)).thenReturn(Optional.of(sharedTask));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(
                WeddingMember.builder().wedding(wedding).user(brideUser).side(WeddingSide.BRIDE_SIDE).build()
        ));

        taskService.deleteTask(103L, brideUser);

        verify(taskRepository, times(1)).save(sharedTask);
        assertNotNull(sharedTask.getDeletedAt());
    }

    @Test
    void restoreTask_ClearsDeletedAtTimestamp() {
        sharedTask.setDeletedAt(java.time.LocalDateTime.now());
        when(taskRepository.findById(103L)).thenReturn(Optional.of(sharedTask));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(
                WeddingMember.builder().wedding(wedding).user(brideUser).side(WeddingSide.BRIDE_SIDE).build()
        ));
        when(taskRepository.save(sharedTask)).thenReturn(sharedTask);

        TaskResponse restored = taskService.restoreTask(103L, brideUser);

        assertNotNull(restored);
        assertNull(sharedTask.getDeletedAt());
    }
}
