package rw.ac.auca.vendor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import rw.ac.auca.user.Role;
import rw.ac.auca.user.User;
import rw.ac.auca.vendor.dto.CreateVendorRequest;
import rw.ac.auca.vendor.dto.UpdateVendorRequest;
import rw.ac.auca.vendor.dto.VendorResponse;
import rw.ac.auca.vendor.dto.VendorSummaryResponse;
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
class VendorServiceTest {

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private WeddingRepository weddingRepository;

    @Mock
    private WeddingMemberRepository weddingMemberRepository;

    @InjectMocks
    private VendorService vendorService;

    private User currentUser;
    private Wedding wedding;
    private WeddingMember member;
    private Vendor vendor;

    @BeforeEach
    void setUp() {
        currentUser = User.builder()
                .email("planner@example.com")
                .role(Role.ROLE_BRIDE)
                .build();
        currentUser.setId(1L);

        wedding = Wedding.builder()
                .title("Alice & Bob Wedding")
                .build();
        wedding.setId(10L);

        member = WeddingMember.builder()
                .wedding(wedding)
                .user(currentUser)
                .side(WeddingSide.BRIDE_SIDE)
                .build();
        member.setId(100L);

        vendor = Vendor.builder()
                .wedding(wedding)
                .name("Kigali Marriott Catering")
                .category(VendorCategory.CATERING)
                .costRwf(new BigDecimal("5000000.00"))
                .bookingStatus(BookingStatus.BOOKED)
                .paymentStatus(VendorPaymentStatus.DEPOSIT_PAID)
                .build();
        vendor.setId(500L);
    }

    @Test
    void createVendor_Success() {
        CreateVendorRequest req = CreateVendorRequest.builder()
                .weddingId(10L)
                .name("Kigali Marriott Catering")
                .category(VendorCategory.CATERING)
                .costRwf(new BigDecimal("5000000.00"))
                .bookingStatus(BookingStatus.BOOKED)
                .paymentStatus(VendorPaymentStatus.DEPOSIT_PAID)
                .build();

        when(weddingRepository.findById(10L)).thenReturn(Optional.of(wedding));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member));
        when(vendorRepository.save(any(Vendor.class))).thenAnswer(i -> {
            Vendor v = i.getArgument(0);
            v.setId(501L);
            return v;
        });

        VendorResponse response = vendorService.createVendor(req, currentUser);

        assertNotNull(response);
        assertEquals(501L, response.getId());
        assertEquals("Kigali Marriott Catering", response.getName());
        assertEquals(VendorCategory.CATERING, response.getCategory());
        assertEquals(new BigDecimal("5000000.00"), response.getCostRwf());
    }

    @Test
    void createVendor_NegativeCost_ThrowsException() {
        CreateVendorRequest req = CreateVendorRequest.builder()
                .weddingId(10L)
                .name("Invalid Vendor")
                .category(VendorCategory.PHOTOGRAPHY)
                .costRwf(new BigDecimal("-500.00"))
                .build();

        when(weddingRepository.findById(10L)).thenReturn(Optional.of(wedding));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                vendorService.createVendor(req, currentUser)
        );
        assertTrue(ex.getMessage().contains("negative"));
    }

    @Test
    void updateVendor_Success() {
        UpdateVendorRequest req = UpdateVendorRequest.builder()
                .name("Kigali Marriott Catering Updated")
                .category(VendorCategory.CATERING)
                .costRwf(new BigDecimal("6000000.00"))
                .bookingStatus(BookingStatus.COMPLETED)
                .paymentStatus(VendorPaymentStatus.PAID_IN_FULL)
                .build();

        when(vendorRepository.findById(500L)).thenReturn(Optional.of(vendor));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(vendor);

        VendorResponse response = vendorService.updateVendor(500L, req, currentUser);

        assertNotNull(response);
        assertEquals("Kigali Marriott Catering Updated", response.getName());
        assertEquals(new BigDecimal("6000000.00"), response.getCostRwf());
    }

    @Test
    void deleteVendor_Success() {
        when(vendorRepository.findById(500L)).thenReturn(Optional.of(vendor));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member));

        vendorService.deleteVendor(500L, currentUser);

        verify(vendorRepository, times(1)).delete(vendor);
    }

    @Test
    void getVendorsByWedding_SummaryCalculation() {
        Vendor v2 = Vendor.builder()
                .wedding(wedding)
                .name("Studio Photography")
                .category(VendorCategory.PHOTOGRAPHY)
                .costRwf(new BigDecimal("2000000.00"))
                .bookingStatus(BookingStatus.INQUIRY)
                .paymentStatus(VendorPaymentStatus.UNPAID)
                .build();
        v2.setId(502L);

        when(weddingRepository.findById(10L)).thenReturn(Optional.of(wedding));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.of(member));
        when(vendorRepository.findByWeddingId(10L)).thenReturn(List.of(vendor, v2));

        VendorSummaryResponse summary = vendorService.getVendorsByWedding(10L, currentUser);

        assertNotNull(summary);
        assertEquals(2, summary.getTotalVendors());
        assertEquals(1, summary.getBookedVendorsCount());
        assertEquals(new BigDecimal("7000000.00"), summary.getTotalVendorCostRwf());
        assertEquals(new BigDecimal("2500000.00"), summary.getTotalPaidCostRwf()); // 50% deposit of 5M
    }

    @Test
    void createVendor_NotMember_ThrowsException() {
        CreateVendorRequest req = CreateVendorRequest.builder()
                .weddingId(10L)
                .name("Unauthorized Vendor")
                .category(VendorCategory.MUSIC)
                .costRwf(new BigDecimal("100000.00"))
                .build();

        when(weddingRepository.findById(10L)).thenReturn(Optional.of(wedding));
        when(weddingMemberRepository.findByWeddingIdAndUserId(10L, 1L)).thenReturn(Optional.empty());

        assertThrows(AccessDeniedException.class, () ->
                vendorService.createVendor(req, currentUser)
        );
    }
}
