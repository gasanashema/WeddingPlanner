package rw.ac.auca;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import rw.ac.auca.ceremony.CeremonyType;
import rw.ac.auca.ceremony.dto.CreateCeremonyRequest;
import rw.ac.auca.ceremony.dto.UpdateCeremonyRequest;
import rw.ac.auca.user.Role;
import rw.ac.auca.user.dto.CreateUserRequest;
import rw.ac.auca.user.dto.UpdateUserRequest;
import rw.ac.auca.wedding.dto.CreateWeddingRequest;
import rw.ac.auca.wedding.dto.UpdateWeddingRequest;
import rw.ac.auca.weddingPlanner.WeddingPlannerApplication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(classes = WeddingPlannerApplication.class)
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
public class WeddingPlannerApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testUserWeddingAndCeremonyCrudFlow() throws Exception {
        // 1. Create Bride User
        CreateUserRequest brideRequest = CreateUserRequest.builder()
                .firstName("Keza")
                .lastName("Divine")
                .email("keza@example.com")
                .phoneNumber("+250788111222")
                .role(Role.ROLE_BRIDE)
                .build();

        String brideJson = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brideRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.firstName").value("Keza"))
                .andReturn().getResponse().getContentAsString();

        Long brideId = objectMapper.readTree(brideJson).get("data").get("id").asLong();

        // 2. Create Groom User
        CreateUserRequest groomRequest = CreateUserRequest.builder()
                .firstName("Shema")
                .lastName("Alain")
                .email("shema@example.com")
                .phoneNumber("+250788333444")
                .role(Role.ROLE_GROOM)
                .build();

        String groomJson = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groomRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.firstName").value("Shema"))
                .andReturn().getResponse().getContentAsString();

        Long groomId = objectMapper.readTree(groomJson).get("data").get("id").asLong();

        // 3. Get All Users
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2));

        // 4. Update User
        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .phoneNumber("+250789999999")
                .build();

        mockMvc.perform(put("/api/v1/users/" + brideId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.phoneNumber").value("+250789999999"));

        // 5. Create Wedding
        CreateWeddingRequest weddingRequest = CreateWeddingRequest.builder()
                .title("Keza & Shema Royal Wedding")
                .targetBudget(new BigDecimal("15000000.00"))
                .brideId(brideId)
                .groomId(groomId)
                .build();

        String weddingJson = mockMvc.perform(post("/api/v1/weddings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(weddingRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value("Keza & Shema Royal Wedding"))
                .andExpect(jsonPath("$.data.brideId").value(brideId))
                .andExpect(jsonPath("$.data.groomId").value(groomId))
                .andReturn().getResponse().getContentAsString();

        Long weddingId = objectMapper.readTree(weddingJson).get("data").get("id").asLong();

        // 6. Get Wedding by ID
        mockMvc.perform(get("/api/v1/weddings/" + weddingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Keza & Shema Royal Wedding"));

        // 7. Update Wedding
        UpdateWeddingRequest updateWeddingRequest = UpdateWeddingRequest.builder()
                .title("Keza & Shema Grand Wedding")
                .targetBudget(new BigDecimal("20000000.00"))
                .build();

        mockMvc.perform(put("/api/v1/weddings/" + weddingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateWeddingRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Keza & Shema Grand Wedding"));

        // 8. Create Traditional Ceremony (Gusaba)
        CreateCeremonyRequest gusabaRequest = CreateCeremonyRequest.builder()
                .weddingId(weddingId)
                .ceremonyType(CeremonyType.TRADITIONAL)
                .name("Gusaba & Gukora Irembo")
                .ceremonyDate(LocalDate.of(2026, 11, 15))
                .startTime(LocalTime.of(10, 0))
                .venueLocation("Kigali Cultural Village")
                .description("Traditional dowry & introduction ceremony")
                .build();

        String ceremonyJson = mockMvc.perform(post("/api/v1/ceremonies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gusabaRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Gusaba & Gukora Irembo"))
                .andExpect(jsonPath("$.data.ceremonyType").value("TRADITIONAL"))
                .andReturn().getResponse().getContentAsString();

        Long ceremonyId = objectMapper.readTree(ceremonyJson).get("data").get("id").asLong();

        // 9. Create Reception Ceremony
        CreateCeremonyRequest receptionRequest = CreateCeremonyRequest.builder()
                .weddingId(weddingId)
                .ceremonyType(CeremonyType.RECEPTION)
                .name("Grand Evening Reception")
                .ceremonyDate(LocalDate.of(2026, 11, 20))
                .startTime(LocalTime.of(18, 0))
                .venueLocation("Intare Conference Arena")
                .description("Evening reception party with dining & dancing")
                .build();

        mockMvc.perform(post("/api/v1/ceremonies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receptionRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Grand Evening Reception"));

        // 10. List Ceremonies by Wedding ID (4 default seeded + 2 newly added = 6)
        mockMvc.perform(get("/api/v1/ceremonies?weddingId=" + weddingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(6));

        // 11. Update Ceremony
        UpdateCeremonyRequest updateCeremonyRequest = UpdateCeremonyRequest.builder()
                .venueLocation("Kigali Marriott Hotel - Grand Ballroom")
                .build();

        mockMvc.perform(put("/api/v1/ceremonies/" + ceremonyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCeremonyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.venueLocation").value("Kigali Marriott Hotel - Grand Ballroom"));

        // 12. Delete Ceremony
        mockMvc.perform(delete("/api/v1/ceremonies/" + ceremonyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Verify remaining ceremony count (6 - 1 = 5)
        mockMvc.perform(get("/api/v1/ceremonies?weddingId=" + weddingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(5));
    }
}
