package rw.ac.auca.ceremony;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.auca.ceremony.dto.CeremonyResponse;
import rw.ac.auca.ceremony.dto.CreateCeremonyRequest;
import rw.ac.auca.ceremony.dto.UpdateCeremonyRequest;
import rw.ac.auca.wedding.Wedding;
import rw.ac.auca.wedding.WeddingRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WeddingCeremonyService {

    private final WeddingCeremonyRepository ceremonyRepository;
    private final WeddingRepository weddingRepository;

    public CeremonyResponse createCeremony(CreateCeremonyRequest request) {
        Wedding wedding = weddingRepository.findById(request.getWeddingId())
                .orElseThrow(() -> new RuntimeException("Wedding not found with id: " + request.getWeddingId()));

        CeremonyType type = request.getCeremonyType() != null ? request.getCeremonyType() : CeremonyType.GENERAL;

        WeddingCeremony ceremony = WeddingCeremony.builder()
                .wedding(wedding)
                .ceremonyType(type)
                .name(request.getName())
                .ceremonyDate(request.getCeremonyDate())
                .startTime(request.getStartTime())
                .venueLocation(request.getVenueLocation())
                .description(request.getDescription())
                .build();

        WeddingCeremony savedCeremony = ceremonyRepository.save(ceremony);
        return CeremonyResponse.fromEntity(savedCeremony);
    }

    @Transactional(readOnly = true)
    public List<CeremonyResponse> getAllCeremonies(Long weddingId) {
        if (weddingId != null) {
            return ceremonyRepository.findByWeddingId(weddingId).stream()
                    .map(CeremonyResponse::fromEntity)
                    .toList();
        }
        return ceremonyRepository.findAll().stream()
                .map(CeremonyResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public CeremonyResponse getCeremonyById(Long id) {
        WeddingCeremony ceremony = ceremonyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ceremony not found with id: " + id));
        return CeremonyResponse.fromEntity(ceremony);
    }

    public CeremonyResponse updateCeremony(Long id, UpdateCeremonyRequest request) {
        WeddingCeremony ceremony = ceremonyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ceremony not found with id: " + id));

        if (request.getCeremonyType() != null) ceremony.setCeremonyType(request.getCeremonyType());
        if (request.getName() != null) ceremony.setName(request.getName());
        if (request.getCeremonyDate() != null) ceremony.setCeremonyDate(request.getCeremonyDate());
        if (request.getStartTime() != null) ceremony.setStartTime(request.getStartTime());
        if (request.getVenueLocation() != null) ceremony.setVenueLocation(request.getVenueLocation());
        if (request.getDescription() != null) ceremony.setDescription(request.getDescription());

        WeddingCeremony updatedCeremony = ceremonyRepository.save(ceremony);
        return CeremonyResponse.fromEntity(updatedCeremony);
    }

    public void deleteCeremony(Long id) {
        if (!ceremonyRepository.existsById(id)) {
            throw new RuntimeException("Ceremony not found with id: " + id);
        }
        ceremonyRepository.deleteById(id);
    }
}
