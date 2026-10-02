package rw.ac.auca.wedding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rw.ac.auca.ceremony.WeddingCeremonyRepository;
import rw.ac.auca.user.Role;
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;
import rw.ac.auca.wedding.dto.CreateWeddingRequest;
import rw.ac.auca.wedding.dto.JoinWeddingRequest;
import rw.ac.auca.wedding.dto.WeddingResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeddingServiceTest {

    @Mock
    private WeddingRepository weddingRepository;

    @Mock
    private WeddingMemberRepository weddingMemberRepository;

    @Mock
    private WeddingCeremonyRepository ceremonyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WeddingService weddingService;

    private User brideUser;
    private User groomUser;
    private User supportUser1;
    private User supportUser2;
    private User supportUser3;
    private Wedding wedding;

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

        supportUser1 = User.builder()
                .firstName("Aline")
                .lastName("Uwase")
                .email("aline@wedding.rw")
                .role(Role.ROLE_BRIDE_FAMILY_SUPPORT)
                .build();
        supportUser1.setId(3L);

        supportUser2 = User.builder()
                .firstName("Bertin")
                .lastName("Keza")
                .email("bertin@wedding.rw")
                .role(Role.ROLE_BRIDE_FAMILY_SUPPORT)
                .build();
        supportUser2.setId(4L);

        supportUser3 = User.builder()
                .firstName("Chantal")
                .lastName("Umutoni")
                .email("chantal@wedding.rw")
                .role(Role.ROLE_BRIDE_FAMILY_SUPPORT)
                .build();
        supportUser3.setId(5L);

        wedding = Wedding.builder()
                .title("Divine & Jean Ubukwe")
                .targetBudget(new BigDecimal("15000000.00"))
                .partnerCode("P-12345678")
                .familyCode("F-87654321")
                .bride(brideUser)
                .build();
        wedding.setId(100L);
    }

    @Test
    void createWedding_Success_CreatesCodesAndSeedsCeremonies() {
        CreateWeddingRequest request = CreateWeddingRequest.builder()
                .title("Divine & Jean Ubukwe")
                .targetBudget(new BigDecimal("15000000.00"))
                .build();

        when(weddingRepository.save(any(Wedding.class))).thenReturn(wedding);
        when(weddingMemberRepository.findByWeddingId(100L)).thenReturn(List.of(
                WeddingMember.builder().wedding(wedding).user(brideUser).role("ROLE_BRIDE").side(WeddingSide.BRIDE_SIDE).build()
        ));
        when(ceremonyRepository.findByWeddingId(100L)).thenReturn(List.of());

        WeddingResponse response = weddingService.createWedding(request, brideUser);

        assertNotNull(response);
        assertEquals("Divine & Jean Ubukwe", response.getTitle());
        assertEquals("P-12345678", response.getPartnerCode());
        assertEquals("F-87654321", response.getFamilyCode());
        verify(ceremonyRepository, times(1)).saveAll(anyList());
        verify(weddingMemberRepository, times(1)).save(any(WeddingMember.class));
    }

    @Test
    void joinWedding_PartnerCode_Success() {
        JoinWeddingRequest request = JoinWeddingRequest.builder()
                .joinCode("P-12345678")
                .build();

        when(weddingRepository.findByPartnerCode("P-12345678")).thenReturn(Optional.of(wedding));
        when(weddingMemberRepository.findByWeddingIdAndUserId(100L, 2L)).thenReturn(Optional.empty());

        WeddingResponse response = weddingService.joinWedding(request, groomUser);

        assertNotNull(response);
        verify(weddingRepository, times(1)).save(wedding);
        verify(weddingMemberRepository, times(1)).save(any(WeddingMember.class));
        assertEquals(groomUser, wedding.getGroom());
    }

    @Test
    void joinWedding_FamilyCode_Success() {
        JoinWeddingRequest request = JoinWeddingRequest.builder()
                .joinCode("F-87654321")
                .role("BRIDE_SUPPORT")
                .build();

        when(weddingRepository.findByPartnerCode("F-87654321")).thenReturn(Optional.empty());
        when(weddingRepository.findByFamilyCode("F-87654321")).thenReturn(Optional.of(wedding));
        when(weddingMemberRepository.countSupportMembersByWeddingAndSide(100L, WeddingSide.BRIDE_SIDE)).thenReturn(1L);

        WeddingResponse response = weddingService.joinWedding(request, supportUser1);

        assertNotNull(response);
        verify(weddingMemberRepository, times(1)).save(any(WeddingMember.class));
    }

    @Test
    void joinWedding_FamilySupportLimitExceeded_ThrowsException() {
        JoinWeddingRequest request = JoinWeddingRequest.builder()
                .joinCode("F-87654321")
                .role("BRIDE_SUPPORT")
                .build();

        when(weddingRepository.findByPartnerCode("F-87654321")).thenReturn(Optional.empty());
        when(weddingRepository.findByFamilyCode("F-87654321")).thenReturn(Optional.of(wedding));
        // Simulate existing 2 support members on BRIDE_SIDE
        when(weddingMemberRepository.countSupportMembersByWeddingAndSide(100L, WeddingSide.BRIDE_SIDE)).thenReturn(2L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> weddingService.joinWedding(request, supportUser3));

        assertTrue(exception.getMessage().contains("Maximum 2 support accounts allowed per side"));
        verify(weddingMemberRepository, never()).save(any(WeddingMember.class));
    }
}
