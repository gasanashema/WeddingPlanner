package rw.ac.auca.vendor;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.auca.user.User;
import rw.ac.auca.vendor.dto.CreateVendorRequest;
import rw.ac.auca.vendor.dto.UpdateVendorRequest;
import rw.ac.auca.vendor.dto.VendorResponse;
import rw.ac.auca.vendor.dto.VendorSummaryResponse;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingMember;
import rw.ac.auca.wedding.WeddingMemberRepository;
import rw.ac.auca.wedding.WeddingRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VendorService {

    private final VendorRepository vendorRepository;
    private final WeddingRepository weddingRepository;
    private final WeddingMemberRepository weddingMemberRepository;

    public VendorResponse createVendor(CreateVendorRequest request, User currentUser) {
        Wedding wedding = resolveWeddingForUser(request.getWeddingId(), currentUser);
        validateUserMembership(wedding.getId(), currentUser);

        BigDecimal cost = request.getCostRwf() != null ? request.getCostRwf() : BigDecimal.ZERO;
        if (cost.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cost cannot be negative.");
        }

        Vendor vendor = Vendor.builder()
                .wedding(wedding)
                .name(request.getName())
                .category(request.getCategory())
                .contactPhone(request.getContactPhone())
                .contactEmail(request.getContactEmail())
                .costRwf(cost)
                .bookingStatus(request.getBookingStatus() != null ? request.getBookingStatus() : BookingStatus.INQUIRY)
                .paymentStatus(request.getPaymentStatus() != null ? request.getPaymentStatus() : VendorPaymentStatus.UNPAID)
                .notes(request.getNotes())
                .build();

        Vendor saved = vendorRepository.save(vendor);
        return mapToVendorResponse(saved);
    }

    public VendorResponse updateVendor(Long vendorId, UpdateVendorRequest request, User currentUser) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found with id: " + vendorId));

        validateUserMembership(vendor.getWedding().getId(), currentUser);

        BigDecimal cost = request.getCostRwf() != null ? request.getCostRwf() : BigDecimal.ZERO;
        if (cost.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cost cannot be negative.");
        }

        vendor.setName(request.getName());
        vendor.setCategory(request.getCategory());
        vendor.setContactPhone(request.getContactPhone());
        vendor.setContactEmail(request.getContactEmail());
        vendor.setCostRwf(cost);
        if (request.getBookingStatus() != null) vendor.setBookingStatus(request.getBookingStatus());
        if (request.getPaymentStatus() != null) vendor.setPaymentStatus(request.getPaymentStatus());
        vendor.setNotes(request.getNotes());

        Vendor updated = vendorRepository.save(vendor);
        return mapToVendorResponse(updated);
    }

    public void deleteVendor(Long vendorId, User currentUser) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found with id: " + vendorId));

        validateUserMembership(vendor.getWedding().getId(), currentUser);
        vendorRepository.delete(vendor);
    }

    @Transactional(readOnly = true)
    public VendorSummaryResponse getVendorsByWedding(Long requestedWeddingId, User currentUser) {
        Wedding wedding = resolveWeddingForUser(requestedWeddingId, currentUser);
        validateUserMembership(wedding.getId(), currentUser);

        List<Vendor> vendors = vendorRepository.findByWeddingId(wedding.getId());

        int totalVendors = vendors.size();
        int bookedCount = 0;
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalPaid = BigDecimal.ZERO;

        List<VendorResponse> vendorResponses = vendors.stream()
                .map(this::mapToVendorResponse)
                .collect(Collectors.toList());

        for (Vendor v : vendors) {
            if (v.getBookingStatus() == BookingStatus.BOOKED || v.getBookingStatus() == BookingStatus.COMPLETED) {
                bookedCount++;
            }
            if (v.getCostRwf() != null) {
                totalCost = totalCost.add(v.getCostRwf());
                if (v.getPaymentStatus() == VendorPaymentStatus.PAID_IN_FULL) {
                    totalPaid = totalPaid.add(v.getCostRwf());
                } else if (v.getPaymentStatus() == VendorPaymentStatus.DEPOSIT_PAID) {
                    totalPaid = totalPaid.add(v.getCostRwf().multiply(new BigDecimal("0.5")).setScale(2, java.math.RoundingMode.HALF_UP));
                }
            }
        }

        return VendorSummaryResponse.builder()
                .totalVendors(totalVendors)
                .bookedVendorsCount(bookedCount)
                .totalVendorCostRwf(totalCost)
                .totalPaidCostRwf(totalPaid)
                .vendors(vendorResponses)
                .build();
    }

    private VendorResponse mapToVendorResponse(Vendor vendor) {
        return VendorResponse.builder()
                .id(vendor.getId())
                .weddingId(vendor.getWedding().getId())
                .name(vendor.getName())
                .category(vendor.getCategory())
                .contactPhone(vendor.getContactPhone())
                .contactEmail(vendor.getContactEmail())
                .costRwf(vendor.getCostRwf())
                .bookingStatus(vendor.getBookingStatus())
                .paymentStatus(vendor.getPaymentStatus())
                .notes(vendor.getNotes())
                .build();
    }

    private Wedding resolveWeddingForUser(Long requestedWeddingId, User currentUser) {
        if (requestedWeddingId != null) {
            return weddingRepository.findById(requestedWeddingId)
                    .orElseThrow(() -> new IllegalArgumentException("Wedding not found with id: " + requestedWeddingId));
        }
        List<WeddingMember> memberships = weddingMemberRepository.findByUserId(currentUser.getId());
        if (memberships.isEmpty()) {
            throw new IllegalArgumentException("User has no active wedding workspace.");
        }
        return memberships.get(0).getWedding();
    }

    private void validateUserMembership(Long weddingId, User currentUser) {
        if (currentUser == null) return;
        boolean isMember = weddingMemberRepository.findByWeddingIdAndUserId(weddingId, currentUser.getId()).isPresent();
        if (!isMember) {
            throw new AccessDeniedException("Forbidden: You are not a member of this wedding.");
        }
    }
}
