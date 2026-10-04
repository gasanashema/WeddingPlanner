package rw.ac.auca.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.ac.auca.user.Role;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
    private Instant createdAt;
    private int weddingMembershipsCount;
}
