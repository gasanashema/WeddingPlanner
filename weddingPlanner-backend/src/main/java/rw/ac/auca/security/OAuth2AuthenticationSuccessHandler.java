package rw.ac.auca.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import rw.ac.auca.user.Role;
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = extractEmail(attributes);
        String name = extractName(attributes);

        if (email == null || email.isEmpty()) {
            log.error("OAuth2 user does not contain an email address");
            getRedirectStrategy().sendRedirect(request, response, "/login?error=no_email");
            return;
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            String[] nameParts = name != null ? name.split(" ", 2) : new String[]{"OAuth", "User"};
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "";

            user = User.builder()
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .password(UUID.randomUUID().toString()) // Dummy random password for OAuth2 users
                    .role(Role.ROLE_BRIDE)
                    .enabled(true)
                    .build();
            user = userRepository.save(user);
            log.info("Registered new user via OAuth2 Social Login: {}", email);
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().name());
        String targetUrl = "/?token=" + token;

        log.info("Successfully authenticated OAuth2 user {}. Issuing JWT.", email);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String extractEmail(Map<String, Object> attributes) {
        if (attributes.containsKey("email")) {
            return (String) attributes.get("email");
        }
        return null;
    }

    private String extractName(Map<String, Object> attributes) {
        if (attributes.containsKey("name")) {
            return (String) attributes.get("name");
        } else if (attributes.containsKey("login")) {
            return (String) attributes.get("login");
        }
        return "OAuth User";
    }
}
