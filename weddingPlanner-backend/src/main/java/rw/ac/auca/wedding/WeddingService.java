package rw.ac.auca.wedding;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.auca.user.User;
import rw.ac.auca.user.UserRepository;
import rw.ac.auca.wedding.dto.CreateWeddingRequest;
import rw.ac.auca.wedding.dto.UpdateWeddingRequest;
import rw.ac.auca.wedding.dto.WeddingResponse;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WeddingService {

    private final WeddingRepository weddingRepository;
    private final UserRepository userRepository;

    public WeddingResponse createWedding(CreateWeddingRequest request) {
        User bride = null;
        if (request.getBrideId() != null) {
            bride = userRepository.findById(request.getBrideId())
                    .orElseThrow(() -> new RuntimeException("Bride User not found with id: " + request.getBrideId()));
        }

        User groom = null;
        if (request.getGroomId() != null) {
            groom = userRepository.findById(request.getGroomId())
                    .orElseThrow(() -> new RuntimeException("Groom User not found with id: " + request.getGroomId()));
        }

        Wedding wedding = Wedding.builder()
                .title(request.getTitle())
                .targetBudget(request.getTargetBudget())
                .partnerCode(UUID.randomUUID().toString())
                .familyCode(UUID.randomUUID().toString())
                .bride(bride)
                .groom(groom)
                .build();

        Wedding savedWedding = weddingRepository.save(wedding);
        return WeddingResponse.fromEntity(savedWedding);
    }

    @Transactional(readOnly = true)
    public List<WeddingResponse> getAllWeddings() {
        return weddingRepository.findAll().stream()
                .map(WeddingResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public WeddingResponse getWeddingById(Long id) {
        Wedding wedding = weddingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wedding not found with id: " + id));
        return WeddingResponse.fromEntity(wedding);
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
        return WeddingResponse.fromEntity(updatedWedding);
    }

    public void deleteWedding(Long id) {
        if (!weddingRepository.existsById(id)) {
            throw new RuntimeException("Wedding not found with id: " + id);
        }
        weddingRepository.deleteById(id);
    }
}
