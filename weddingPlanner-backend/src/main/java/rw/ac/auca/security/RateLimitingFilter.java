package rw.ac.auca.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rw.ac.auca.common.ApiResponse;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public RateLimitingFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper != null 
                ? objectMapper.copy().findAndRegisterModules() 
                : new ObjectMapper().findAndRegisterModules();
    }

    // Rate Limit Config: 10 requests per minute per IP
    private static final int CAPACITY = 10;
    private static final int REFILL_TOKENS = 10;
    private static final Duration REFILL_DURATION = Duration.ofMinutes(1);

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(CAPACITY, Refill.greedy(REFILL_TOKENS, REFILL_DURATION));
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket getBucket(String ipKey) {
        return buckets.computeIfAbsent(ipKey, k -> createNewBucket());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Target rate limiting on sensitive auth and public RSVP endpoints
        if (path.startsWith("/api/v1/auth") || path.startsWith("/api/v1/public") || path.startsWith("/api/public")) {
            String clientIp = getClientIp(request);
            String ipKey = clientIp + ":" + path;
            Bucket bucket = getBucket(ipKey);

            if (!bucket.tryConsume(1)) {
                log.warn("Rate limit exceeded for IP {} on endpoint {}", clientIp, path);
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                
                ApiResponse<Void> apiResponse = ApiResponse.error("Rate limit exceeded. Maximum 10 requests per minute allowed.");
                response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
