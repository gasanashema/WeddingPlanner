package rw.ac.auca.wedding;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.auca.ceremony.CeremonyType;
import rw.ac.auca.ceremony.WeddingCeremony;
import rw.ac.auca.ceremony.WeddingCeremonyRepository;
import rw.ac.auca.ceremony.dto.CeremonyResponse;
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;
import rw.ac.auca.wedding.dto.CreateWeddingRequest;
import rw.ac.auca.wedding.dto.JoinWeddingRequest;
import rw.ac.auca.wedding.dto.UpdateWeddingRequest;
import rw.ac.auca.wedding.dto.WeddingResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WeddingService {

    private final WeddingRepository weddingRepository;
    private final WeddingMemberRepository weddingMemberRepository;
    private final WeddingCeremonyRepository ceremonyRepository;
    private final UserRepository userRepository;

    public WeddingResponse createWedding(CreateWeddingRequest request, User currentUser) {
        User creator = currentUser != null 
                ? currentUser 
                : (request.getBrideId() != null ? userRepository.findById(request.getBrideId()).orElse(null) : null);

        User bride = null;
        User groom = null;
        WeddingSide creatorSide = WeddingSide.BRIDE_SIDE;
        String creatorRole = "ROLE_BRIDE";

        if (request.getBrideId() != null) {
            bride = userRepository.findById(request.getBrideId()).orElse(null);
        }
        if (request.getGroomId() != null) {
            groom = userRepository.findById(request.getGroomId()).orElse(null);
        }

        if (creator != null) {
            String roleStr = creator.getRole() != null ? creator.getRole().name() : "";
            if (roleStr.contains("GROOM")) {
                groom = creator;
                creatorSide = WeddingSide.GROOM_SIDE;
                creatorRole = "ROLE_GROOM";
            } else {
                bride = creator;
                creatorSide = WeddingSide.BRIDE_SIDE;
                creatorRole = "ROLE_BRIDE";
            }
        }

        Wedding wedding = Wedding.builder()
                .title(request.getTitle() != null ? request.getTitle() : "Our Ubukwe Wedding")
                .targetBudget(request.getTargetBudget())
                .partnerCode(generateUniqueCode("P"))
                .familyCode(generateUniqueCode("F"))
                .bride(bride)
                .groom(groom)
                .build();

        Wedding savedWedding = weddingRepository.save(wedding);

        if (creator != null) {
            WeddingMember member = WeddingMember.builder()
                    .wedding(savedWedding)
                    .user(creator)
                    .role(creatorRole)
                    .side(creatorSide)
                    .build();
            weddingMemberRepository.save(member);
        }

        // Seed default Rwandan ceremonies
        seedDefaultCeremonies(savedWedding);

        List<WeddingMember> members = weddingMemberRepository.findByWeddingId(savedWedding.getId());
        List<CeremonyResponse> ceremonies = ceremonyRepository.findByWeddingId(savedWedding.getId())
                .stream().map(CeremonyResponse::fromEntity).toList();

        return WeddingResponse.fromEntity(savedWedding, members, ceremonies);
    }

    public WeddingResponse joinWedding(JoinWeddingRequest request, User currentUser) {
        if (currentUser == null) {
            throw new IllegalArgumentException("User must be authenticated to join a wedding.");
        }

        String code = request.getJoinCode().trim();

        // 1. Try Partner Code match
        Wedding wedding = weddingRepository.findByPartnerCode(code).orElse(null);
        if (wedding != null) {
            // Check if partner spot is available
            if (wedding.getBride() != null && wedding.getGroom() != null) {
                throw new IllegalArgumentException("This wedding already has both partner spots filled.");
            }

            WeddingSide side;
            String role;

            if (wedding.getBride() != null) {
                wedding.setGroom(currentUser);
                side = WeddingSide.GROOM_SIDE;
                role = "ROLE_GROOM";
            } else if (wedding.getGroom() != null) {
                wedding.setBride(currentUser);
                side = WeddingSide.BRIDE_SIDE;
                role = "ROLE_BRIDE";
            } else {
                String roleStr = currentUser.getRole() != null ? currentUser.getRole().name() : "";
                if (roleStr.contains("BRIDE")) {
                    wedding.setBride(currentUser);
                    side = WeddingSide.BRIDE_SIDE;
                    role = "ROLE_BRIDE";
                } else {
                    wedding.setGroom(currentUser);
                    side = WeddingSide.GROOM_SIDE;
                    role = "ROLE_GROOM";
                }
            }

            weddingRepository.save(wedding);

            // Add or update membership
            WeddingMember member = weddingMemberRepository.findByWeddingIdAndUserId(wedding.getId(), currentUser.getId())
                    .orElse(WeddingMember.builder()
                            .wedding(wedding)
                            .user(currentUser)
                            .build());
            member.setRole(role);
            member.setSide(side);
            weddingMemberRepository.save(member);

            List<WeddingMember> members = weddingMemberRepository.findByWeddingId(wedding.getId());
            List<CeremonyResponse> ceremonies = ceremonyRepository.findByWeddingId(wedding.getId())
                    .stream().map(CeremonyResponse::fromEntity).toList();

            return WeddingResponse.fromEntity(wedding, members, ceremonies);
        }

        // 2. Try Family Code match
        wedding = weddingRepository.findByFamilyCode(code).orElse(null);
        if (wedding != null) {
            WeddingSide targetSide = WeddingSide.BRIDE_SIDE;
            String userRoleStr = currentUser.getRole() != null ? currentUser.getRole().name() : "";
            String requestedRole = request.getRole() != null ? request.getRole().toUpperCase() : userRoleStr;

            if (requestedRole.contains("GROOM")) {
                targetSide = WeddingSide.GROOM_SIDE;
            } else if (requestedRole.contains("BRIDE")) {
                targetSide = WeddingSide.BRIDE_SIDE;
            }

            // Enforce Rule: Maximum 2 family support accounts per side
            long existingSupportCount = weddingMemberRepository.countSupportMembersByWeddingAndSide(wedding.getId(), targetSide);
            if (existingSupportCount >= 2) {
                throw new IllegalArgumentException("Family support account limit reached. Maximum 2 support accounts allowed per side for family assistance.");
            }

            String supportRole = (targetSide == WeddingSide.BRIDE_SIDE) ? "ROLE_BRIDE_FAMILY_SUPPORT" : "ROLE_GROOM_FAMILY_SUPPORT";

            WeddingMember member = weddingMemberRepository.findByWeddingIdAndUserId(wedding.getId(), currentUser.getId())
                    .orElse(WeddingMember.builder()
                            .wedding(wedding)
                            .user(currentUser)
                            .build());
            member.setRole(supportRole);
            member.setSide(targetSide);
            weddingMemberRepository.save(member);

            List<WeddingMember> members = weddingMemberRepository.findByWeddingId(wedding.getId());
            List<CeremonyResponse> ceremonies = ceremonyRepository.findByWeddingId(wedding.getId())
                    .stream().map(CeremonyResponse::fromEntity).toList();

            return WeddingResponse.fromEntity(wedding, members, ceremonies);
        }

        throw new IllegalArgumentException("Invalid join code provided. Please check the code and try again.");
    }

    @Transactional(readOnly = true)
    public WeddingResponse getCurrentUserWedding(User currentUser) {
        if (currentUser == null) return null;

        List<WeddingMember> userMemberships = weddingMemberRepository.findByUserId(currentUser.getId());
        if (userMemberships.isEmpty()) {
            return null;
        }

        Wedding wedding = userMemberships.get(0).getWedding();
        List<WeddingMember> members = weddingMemberRepository.findByWeddingId(wedding.getId());
        List<CeremonyResponse> ceremonies = ceremonyRepository.findByWeddingId(wedding.getId())
                .stream().map(CeremonyResponse::fromEntity).toList();

        return WeddingResponse.fromEntity(wedding, members, ceremonies);
    }

    @Transactional(readOnly = true)
    public List<WeddingResponse> getAllWeddings() {
        return weddingRepository.findAll().stream()
                .map(w -> {
                    List<WeddingMember> members = weddingMemberRepository.findByWeddingId(w.getId());
                    List<CeremonyResponse> ceremonies = ceremonyRepository.findByWeddingId(w.getId())
                            .stream().map(CeremonyResponse::fromEntity).toList();
                    return WeddingResponse.fromEntity(w, members, ceremonies);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public WeddingResponse getWeddingById(Long id) {
        Wedding wedding = weddingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wedding not found with id: " + id));
        List<WeddingMember> members = weddingMemberRepository.findByWeddingId(wedding.getId());
        List<CeremonyResponse> ceremonies = ceremonyRepository.findByWeddingId(wedding.getId())
                .stream().map(CeremonyResponse::fromEntity).toList();
        return WeddingResponse.fromEntity(wedding, members, ceremonies);
    }

    public WeddingResponse updateWedding(Long id, UpdateWeddingRequest request) {
        Wedding wedding = weddingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wedding not found with id: " + id));

        if (request.getTitle() != null) wedding.setTitle(request.getTitle());
        if (request.getTargetBudget() != null) wedding.setTargetBudget(request.getTargetBudget());

        if (request.getBrideId() != null) {
            User bride = userRepository.findById(request.getBrideId())
                    .orElseThrow(() -> new RuntimeException("Bride User not found with id: " + request.getBrideId()));
            wedding.setBride(bride);
        }

        if (request.getGroomId() != null) {
            User groom = userRepository.findById(request.getGroomId())
                    .orElseThrow(() -> new RuntimeException("Groom User not found with id: " + request.getGroomId()));
            wedding.setGroom(groom);
        }

        Wedding updatedWedding = weddingRepository.save(wedding);
        List<WeddingMember> members = weddingMemberRepository.findByWeddingId(updatedWedding.getId());
        List<CeremonyResponse> ceremonies = ceremonyRepository.findByWeddingId(updatedWedding.getId())
                .stream().map(CeremonyResponse::fromEntity).toList();
        return WeddingResponse.fromEntity(updatedWedding, members, ceremonies);
    }

    public void deleteWedding(Long id) {
        if (!weddingRepository.existsById(id)) {
            throw new RuntimeException("Wedding not found with id: " + id);
        }
        weddingRepository.deleteById(id);
    }

    private void seedDefaultCeremonies(Wedding wedding) {
        LocalDate now = LocalDate.now();
        List<WeddingCeremony> defaultCeremonies = List.of(
                WeddingCeremony.builder()
                        .wedding(wedding)
                        .name("Gusaba & Gukora Irembo")
                        .ceremonyType(CeremonyType.TRADITIONAL)
                        .ceremonyDate(now.plusMonths(2))
                        .startTime(LocalTime.of(10, 0))
                        .venueLocation("Kigali Traditional Grounds")
                        .description("Traditional dowry and introduction ceremony.")
                        .build(),
                WeddingCeremony.builder()
                        .wedding(wedding)
                        .name("Civil Registration")
                        .ceremonyType(CeremonyType.CIVIL)
                        .ceremonyDate(now.plusMonths(2).plusDays(5))
                        .startTime(LocalTime.of(9, 0))
                        .venueLocation("Nyarugenge District Office")
                        .description("Legal marriage contract signing.")
                        .build(),
                WeddingCeremony.builder()
                        .wedding(wedding)
                        .name("Religious Wedding Blessing")
                        .ceremonyType(CeremonyType.RELIGIOUS)
                        .ceremonyDate(now.plusMonths(2).plusDays(7))
                        .startTime(LocalTime.of(14, 0))
                        .venueLocation("St. Michel Cathedral Kigali")
                        .description("Church nuptial blessing ceremony.")
                        .build(),
                WeddingCeremony.builder()
                        .wedding(wedding)
                        .name("Evening Reception")
                        .ceremonyType(CeremonyType.RECEPTION)
                        .ceremonyDate(now.plusMonths(2).plusDays(7))
                        .startTime(LocalTime.of(18, 0))
                        .venueLocation("Kigali Convention Centre")
                        .description("Celebratory dinner and reception party.")
                        .build()
        );
        ceremonyRepository.saveAll(defaultCeremonies);
    }

    private String generateUniqueCode(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
