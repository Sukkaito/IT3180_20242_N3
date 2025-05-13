package vn.edu.hust.nmcnpm_20242_n3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.entity.Fine;
import vn.edu.hust.nmcnpm_20242_n3.repository.FineRepository;

import java.util.Date;
import java.util.List;

@Service
public class FineService {

    private final FineRepository fineRepository;

    @Autowired
    public FineService(FineRepository fineRepository) {
        this.fineRepository = fineRepository;
    }

    public List<Fine> getAllFines() {
        return (List<Fine>) fineRepository.findAll();
    }

    public Fine getFineById(String id) {
        return fineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fine not found with ID: " + id));
    }

    public Fine addFine(Fine fine) {
        return fineRepository.save(fine);
    }

    public Fine updateFine(String id, Fine fine) {
        Fine existingFine = fineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fine not found with ID: " + id));
        existingFine.setAmount(fine.getAmount());
        existingFine.setDescription(fine.getDescription());
        existingFine.setUpdatedAt(new Date());
        return fineRepository.save(existingFine);
    }

    public void deleteFine(String id) {
        if (!fineRepository.existsById(id)) {
            throw new IllegalArgumentException("Fine not found with ID: " + id);
        }
        fineRepository.deleteById(id);
    }

    public List<Fine> getFinesByUserId(String userId) {
        return fineRepository.findByUserId(userId);
    }

    public List<Fine> getFinesByDateRange(Date startDate, Date endDate) {
        return fineRepository.findByCreatedAtBetween(startDate, endDate);
    }
}