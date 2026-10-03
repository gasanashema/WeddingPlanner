package rw.ac.auca.homeprep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import rw.ac.auca.homeprep.dto.CreateHomePrepRequest;
import rw.ac.auca.homeprep.dto.HomePrepResponse;
import rw.ac.auca.user.Role;
import rw.ac.auca.user.User;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;
import rw.ac.auca.wedding.WeddingSide;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomePrepServiceTest {

    @Mock
    private HomePrepRepository homePrepRepository;

    @Mock
    private WeddingRepository weddingRepository;

    @Mock
    private WeddingMemberRepository weddingMemberRepository;

    @InjectMocks
    private HomePrepService homePrepService;

    private User brideUser;
    private User groomUser;
    private Wedding wedding;
    private HomePreparation brideItem;
    private HomePreparation groomItem;
    private HomePreparation sharedItem;

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

        wedding = Wedding.builder()
                .title("Divine & Jean Ubukwe")
                .bride(brideUser)
                .groom(groomUser)
                .build();
        wedding.setId(10L);

        brideItem = HomePreparation.builder()
                .wedding(wedding)
                .category(HomePrepCategory.BEDDING)
                .itemName("Master Bed & Linens")
                .side(WeddingSide.BRIDE_SIDE)
                .budgetRwf(new BigDecimal("850000.00"))
                .isCompleted(false)
                .build();
        brideItem.setId(101L);

        groomItem = HomePreparation.builder()
                .wedding(wedding)
                .category(HomePrepCategory.APPLIANCES)
                .itemName("Smart TV")
                .side(WeddingSide.GROOM_SIDE)
                .budgetRwf(new BigDecimal("650000.00"))
                .isCompleted(false)
                .build();
        groomItem.setId(102L);

        sharedItem = HomePreparation.builder()
                .wedding(wedding)
                .category(HomePrepCategory.RENT_UTILITIES)
                .itemName("Rent Deposit")
                .side(WeddingSide.SHARED)
                .budgetRwf(new BigDecimal("500000.00"))
                .isCompleted(false)
                .build();
        sharedItem.setId(103L);
    }

    @Test
    void brideUser_Gets_Only_BrideSide_And_SharedItems() {
        when(weddingMemberRepository.findByUserId(1L)).thenReturn(List.of(
                WeddingMember.builder().wedding(wedding).user(brideUser).side(WeddingSide.BRIDE_SIDE).build()
        ));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(
                WeddingMember.builder().wedding(wedding).user(brideUser).side(WeddingSide.BRIDE_SIDE).build()
        ));
        when(homePrepRepository.findByWeddingIdAndSideIn(eq(10L), anyList()))
                .thenReturn(List.of(brideItem, sharedItem));

        List<HomePrepResponse> items = homePrepService.getHomePrepsForUser(brideUser);

        assertEquals(2, items.size());
        assertTrue(items.stream().allMatch(i -> i.getSide() != WeddingSide.GROOM_SIDE));
    }

    @Test
    void groomUser_Attempting_To_Access_BrideItem_ThrowsAccessDeniedException() {
        when(homePrepRepository.findById(101L)).thenReturn(Optional.of(brideItem));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 2L)).thenReturn(Optional.of(
                WeddingMember.builder().wedding(wedding).user(groomUser).side(WeddingSide.GROOM_SIDE).build()
        ));

        assertThrows(AccessDeniedException.class, () -> homePrepService.getHomePrepById(101L, groomUser));
    }

    @Test
    void createHomePrepItem_SavesAndReturnsResponse() {
        CreateHomePrepRequest req = CreateHomePrepRequest.builder()
                .weddingId(10L)
                .category(HomePrepCategory.KITCHENWARE)
                .itemName("Cookware Set")
                .side(WeddingSide.BRIDE_SIDE)
                .budgetRwf(new BigDecimal("200000.00"))
                .build();

        when(weddingRepository.findById(10L)).thenReturn(Optional.of(wedding));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(
                WeddingMember.builder().wedding(wedding).user(brideUser).side(WeddingSide.BRIDE_SIDE).build()
        ));
        when(homePrepRepository.save(any(HomePreparation.class))).thenAnswer(i -> {
            HomePreparation p = i.getArgument(0);
            p.setId(200L);
            return p;
        });

        HomePrepResponse resp = homePrepService.createHomePrep(req, brideUser);

        assertNotNull(resp);
        assertEquals("Cookware Set", resp.getItemName());
        assertEquals(HomePrepCategory.KITCHENWARE, resp.getCategory());
        assertEquals(WeddingSide.BRIDE_SIDE, resp.getSide());
    }
}
