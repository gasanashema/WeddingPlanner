package rw.ac.auca.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.auca.admin.dto.AdminUserResponse;
import rw.ac.auca.admin.dto.AdminWeddingResponse;
import rw.ac.auca.admin.dto.UpdateUserRoleRequest;
import rw.ac.auca.user.Role;
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final WeddingRepository weddingRepository;
    private final WeddingMemberRepository weddingMemberRepository;

    @Transactional(readOnly = true)
    public List<AdminUserResponse> getAllUsers(User currentUser) {
        validateAdminRole(currentUser);
        List<User> users = userRepository.findAll();
        return users.stream().map(u -> {
            int memberships = weddingMemberRepository.findByUserId(u.getId()).size();
            return AdminUserResponse.builder()
                    .id(u.getId())
                    .fullName(getUserFullName(u))
                    .email(u.getEmail())
                    .phone(u.getPhoneNumber())
                    .role(u.getRole())
                    .createdAt(u.getCreatedAt())
                    .weddingMembershipsCount(memberships)
                    .build();
        }).collect(Collectors.toList());
    }

    public AdminUserResponse updateUserRole(Long userId, UpdateUserRoleRequest request, User currentUser) {
        validateAdminRole(currentUser);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        user.setRole(request.getRole());
        User saved = userRepository.save(user);

        int memberships = weddingMemberRepository.findByUserId(saved.getId()).size();
        return AdminUserResponse.builder()
                .id(saved.getId())
                .fullName(getUserFullName(saved))
                .email(saved.getEmail())
                .phone(saved.getPhoneNumber())
                .role(saved.getRole())
                .createdAt(saved.getCreatedAt())
                .weddingMembershipsCount(memberships)
                .build();
    }

    @Transactional(readOnly = true)
    public List<AdminWeddingResponse> getAllWeddings(User currentUser) {
        validateAdminRole(currentUser);
        List<Wedding> weddings = weddingRepository.findAll();
        return weddings.stream().map(w -> {
            List<WeddingMember> members = weddingMemberRepository.findByWeddingId(w.getId());
            return AdminWeddingResponse.builder()
                    .id(w.getId())
                    .title(w.getTitle())
                    .partnerCode(w.getPartnerCode())
                    .familyCode(w.getFamilyCode())
                    .targetBudget(w.getTargetBudget())
                    .brideName(w.getBride() != null ? getUserFullName(w.getBride()) : null)
                    .groomName(w.getGroom() != null ? getUserFullName(w.getGroom()) : null)
                    .totalMembers(members.size())
                    .createdAt(w.getCreatedAt())
                    .build();
        }).collect(Collectors.toList());
    }

    public void deleteUser(Long userId, User currentUser) {
        validateAdminRole(currentUser);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (currentUser != null && currentUser.getId().equals(user.getId())) {
            throw new IllegalArgumentException("Admin cannot delete their own account.");
        }

        userRepository.delete(user);
    }

    private String getUserFullName(User user) {
        if (user == null) return null;
        String fn = user.getFirstName() != null ? user.getFirstName() : "";
        String ln = user.getLastName() != null ? user.getLastName() : "";
        return (fn + " " + ln).trim();
    }

    private void validateAdminRole(User currentUser) {
        if (currentUser == null || currentUser.getRole() != Role.ROLE_ADMIN) {
            throw new AccessDeniedException("Forbidden: System Administrator governance rights required.");
        }
    }
}
