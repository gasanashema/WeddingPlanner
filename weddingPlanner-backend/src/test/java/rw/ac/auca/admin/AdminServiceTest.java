package rw.ac.auca.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import rw.ac.auca.admin.dto.AdminUserResponse;
import rw.ac.auca.admin.dto.AdminWeddingResponse;
import rw.ac.auca.admin.dto.UpdateUserRoleRequest;
import rw.ac.auca.user.Role;
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WeddingRepository weddingRepository;

    @Mock
    private WeddingMemberRepository weddingMemberRepository;

    @InjectMocks
    private AdminService adminService;

    private User adminUser;
    private User normalUser;

    @BeforeEach
    void setUp() {
        adminUser = User.builder()
                .email("admin@wedplan.rw")
                .role(Role.ROLE_ADMIN)
                .build();
        adminUser.setId(1L);

        normalUser = User.builder()
                .email("user@wedplan.rw")
                .role(Role.ROLE_BRIDE)
                .build();
        normalUser.setId(2L);
    }

    @Test
    void getAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(List.of(adminUser, normalUser));
        when(weddingMemberRepository.findByUserId(anyLong())).thenReturn(List.of());

        List<AdminUserResponse> users = adminService.getAllUsers(adminUser);

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("admin@wedplan.rw", users.get(0).getEmail());
        assertEquals("user@wedplan.rw", users.get(1).getEmail());
    }

    @Test
    void updateUserRole_Success() {
        UpdateUserRoleRequest req = UpdateUserRoleRequest.builder()
                .role(Role.ROLE_ADMIN)
                .build();

        when(userRepository.findById(2L)).thenReturn(Optional.of(normalUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(weddingMemberRepository.findByUserId(2L)).thenReturn(List.of());

        AdminUserResponse response = adminService.updateUserRole(2L, req, adminUser);

        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals(Role.ROLE_ADMIN, response.getRole());
    }

    @Test
    void getAllWeddings_Success() {
        Wedding w = Wedding.builder().title("System Wedding Test").build();
        w.setId(10L);

        when(weddingRepository.findAll()).thenReturn(List.of(w));
        when(weddingMemberRepository.findByWeddingId(10L)).thenReturn(List.of());

        List<AdminWeddingResponse> weddings = adminService.getAllWeddings(adminUser);

        assertNotNull(weddings);
        assertEquals(1, weddings.size());
        assertEquals("System Wedding Test", weddings.get(0).getTitle());
    }

    @Test
    void getAllUsers_NotAdmin_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                adminService.getAllUsers(normalUser)
        );
    }
}
