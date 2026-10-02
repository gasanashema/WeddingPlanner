package rw.ac.auca.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RateLimitingFilterTest {

    private RateLimitingFilter rateLimitingFilter;

    @BeforeEach
    void setUp() {
        rateLimitingFilter = new RateLimitingFilter(new ObjectMapper());
    }

    @Test
    void rateLimiting_AllowsUpToTenRequests_ThenReturns429() throws Exception {
        String endpoint = "/api/v1/auth/login";

        // First 10 requests should succeed (200 OK passed down chain)
        for (int i = 0; i < 10; i++) {
            MockHttpServletRequest request = new MockHttpServletRequest("POST", endpoint);
            request.setRemoteAddr("192.168.1.100");
            MockHttpServletResponse response = new MockHttpServletResponse();
            MockFilterChain chain = new MockFilterChain();

            rateLimitingFilter.doFilter(request, response, chain);
            assertEquals(200, response.getStatus());
        }

        // 11th request exceeds limit and should return 429 Too Many Requests
        MockHttpServletRequest request11 = new MockHttpServletRequest("POST", endpoint);
        request11.setRemoteAddr("192.168.1.100");
        MockHttpServletResponse response11 = new MockHttpServletResponse();
        MockFilterChain chain11 = new MockFilterChain();

        rateLimitingFilter.doFilter(request11, response11, chain11);
        assertEquals(429, response11.getStatus());
    }

    @Test
    void nonRateLimitedEndpoint_AlwaysSucceeds() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/weddings");
        request.setRemoteAddr("192.168.1.100");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        rateLimitingFilter.doFilter(request, response, chain);
        assertEquals(200, response.getStatus());
    }
}
