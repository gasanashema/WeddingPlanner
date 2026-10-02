package rw.ac.auca.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlacklistService {

    private final StringRedisTemplate redisTemplate;
    private final Set<String> fallbackBlacklist = ConcurrentHashMap.newKeySet();

    public void blacklistToken(String token, long expirationMillis) {
        try {
            if (redisTemplate != null) {
                redisTemplate.opsForValue().set("blacklisted_token:" + token, "true", Duration.ofMillis(expirationMillis));
            }
        } catch (Exception e) {
            log.warn("Redis unavailable, using fallback memory blacklist: {}", e.getMessage());
        }
        fallbackBlacklist.add(token);
    }

    public boolean isBlacklisted(String token) {
        if (fallbackBlacklist.contains(token)) {
            return true;
        }
        try {
            if (redisTemplate != null) {
                Boolean hasKey = redisTemplate.hasKey("blacklisted_token:" + token);
                return Boolean.TRUE.equals(hasKey);
            }
        } catch (Exception e) {
            log.warn("Redis check failed: {}", e.getMessage());
        }
        return false;
    }
}
