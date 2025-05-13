package vn.edu.hust.nmcnpm_20242_n3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.entity.Fine;
import vn.edu.hust.nmcnpm_20242_n3.repository.FineRepository;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FineService {

    private final FineRepository fineRepository;

    @Autowired
    public FineService(FineRepository fineRepository) {
        this.fineRepository = fineRepository;
    }

    public List<Fine> getAllFines() {
        return fineRepository.findAll();
    }

    public Fine getFineById(String id) {
        return fineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Fine not found with ID: " + id));
    }

    public Fine addFine(Fine fine) {
        return fineRepository.save(fine);
    }

    public Fine updateFine(String id, Fine fine) {
        Fine existingFine = getFineById(id);
        existingFine.setAmount(fine.getAmount());
        existingFine.setDescription(fine.getDescription());
        existingFine.setUser(fine.getUser());
        existingFine.setBookLoan(fine.getBookLoan());
        return fineRepository.save(existingFine);
    }

    public void deleteFine(String id) {
        if (!fineRepository.existsById(id)) {
            throw new NoSuchElementException("Fine not found with ID: " + id);
        }
        fineRepository.deleteById(id);
    }

    public List<Fine> getFinesByUserId(String userId) {
        return fineRepository.findByUser_Id(userId);
    }

    public List<Fine> getFinesByDateRange(Date startDate, Date endDate) {
        return fineRepository.findByCreatedAtBetween(startDate, endDate);
    }

    public List<Fine> getFinesByAmountGreaterThan(double amount) {
        return fineRepository.findByAmountGreaterThan(amount);
    }
}
