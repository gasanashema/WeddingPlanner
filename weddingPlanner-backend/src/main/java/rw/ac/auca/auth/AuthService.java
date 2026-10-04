package rw.ac.auca.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.auca.auth.dto.AuthResponse;
import rw.ac.auca.auth.dto.ChangePasswordRequest;
import rw.ac.auca.auth.dto.LoginRequest;
import rw.ac.auca.auth.dto.RegisterRequest;
import rw.ac.auca.messaging.EventPublisher;
import rw.ac.auca.messaging.dto.EmailInvitationEvent;
import rw.ac.auca.security.JwtTokenProvider;
import rw.ac.auca.user.Role;
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;
import rw.ac.auca.user.dto.UserResponse;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;
import rw.ac.auca.wedding.WeddingSide;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final WeddingRepository weddingRepository;
    private final WeddingMemberRepository weddingMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EventPublisher eventPublisher;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User with email " + request.getEmail() + " already exists.");
        }

        Role userRole = request.getRole() != null ? request.getRole() : Role.ROLE_BRIDE;
        if (userRole != Role.ROLE_BRIDE && userRole != Role.ROLE_GROOM) {
            userRole = Role.ROLE_BRIDE;
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(userRole)
                .enabled(true)
                .mustChangePassword(false)
                .build();

        User savedUser = userRepository.save(user);

        // Check partner creation
        if (request.getPartnerEmail() != null && !request.getPartnerEmail().isBlank()) {
            String partnerEmail = request.getPartnerEmail().trim();
            if (userRepository.existsByEmail(partnerEmail)) {
                log.warn("Partner email {} already exists in system.", partnerEmail);
            } else {
                Role partnerRole = (userRole == Role.ROLE_BRIDE) ? Role.ROLE_GROOM : Role.ROLE_BRIDE;
                String partnerFirstName = (request.getPartnerFirstName() != null && !request.getPartnerFirstName().isBlank())
                        ? request.getPartnerFirstName() : "Partner";
                String partnerLastName = (request.getPartnerLastName() != null && !request.getPartnerLastName().isBlank())
                        ? request.getPartnerLastName() : request.getLastName();

                String tempPassword = "Partner123!";

                User partnerUser = User.builder()
                        .firstName(partnerFirstName)
                        .lastName(partnerLastName)
                        .email(partnerEmail)
                        .password(passwordEncoder.encode(tempPassword))
                        .phoneNumber(request.getPartnerPhone())
                        .role(partnerRole)
                        .enabled(true)
                        .mustChangePassword(true)
                        .build();

                User savedPartner = userRepository.save(partnerUser);

                // Create Wedding Workspace
                User bride = (userRole == Role.ROLE_BRIDE) ? savedUser : savedPartner;
                User groom = (userRole == Role.ROLE_GROOM) ? savedUser : savedPartner;

                Wedding wedding = Wedding.builder()
                        .title(savedUser.getFirstName() + " & " + savedPartner.getFirstName() + "'s Wedding")
                        .bride(bride)
                        .groom(groom)
                        .partnerCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                        .familyCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                        .build();

                Wedding savedWedding = weddingRepository.save(wedding);

                weddingMemberRepository.save(WeddingMember.builder()
                        .wedding(savedWedding)
                        .user(savedUser)
                        .role(savedUser.getRole().name())
                        .side(WeddingSide.SHARED)
                        .build());

                weddingMemberRepository.save(WeddingMember.builder()
                        .wedding(savedWedding)
                        .user(savedPartner)
                        .role(savedPartner.getRole().name())
                        .side(WeddingSide.SHARED)
                        .build());

                // Publish partner invitation email via RabbitMQ
                eventPublisher.publishEmailInvitation(EmailInvitationEvent.builder()
                        .invitationToken(savedWedding.getPartnerCode())
                        .guestEmail(partnerEmail)
                        .guestName(partnerFirstName)
                        .weddingTitle(savedWedding.getTitle())
                        .shareableUrl("Temporary Login Password: " + tempPassword + " (Please update upon login)")
                        .build());
            }
        }

        String token = jwtTokenProvider.generateToken(savedUser.getEmail(), savedUser.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .user(UserResponse.fromEntity(savedUser))
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .user(UserResponse.fromEntity(user))
                .build();
    }

    public void changePassword(ChangePasswordRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect current password.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setMustChangePassword(false);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return UserResponse.fromEntity(user);
    }
}

