package vn.edu.hust.nmcnpm_20242_n3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.dto.FineDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import vn.edu.hust.nmcnpm_20242_n3.entity.Fine;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookLoanRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.FineRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.UserRepository;

import java.util.Date;
import java.util.List;

@Service
public class FineService {

    private final FineRepository fineRepository;
    private final UserRepository userRepository;
    private final BookLoanRepository bookLoanRepository;

    @Autowired
    public FineService(FineRepository fineRepository, UserRepository userRepository, BookLoanRepository bookLoanRepository) {
        this.fineRepository = fineRepository;
        this.userRepository = userRepository;
        this.bookLoanRepository = bookLoanRepository;
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

    public Fine updateFine(String id, FineDTO fineDTO) {
        Fine fine = fineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fine not found with ID: " + id));
        fine.setId(id);
        fine.setAmount(fineDTO.getAmount());
        fine.setDescription(fineDTO.getDescription());
        System.out.println(" Test " + fineDTO.getUsername());
        if (fineDTO.getUsername() != null) {
            User user = userRepository.findByUserName(fineDTO.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            fine.setUser(user);
        }
        if (fineDTO.getBookLoanId() != null) {
            BookLoan bookLoan = bookLoanRepository.findById(fineDTO.getBookLoanId())
                    .orElseThrow(() -> new IllegalArgumentException("BookLoan not found"));
            fine.setBookLoan(bookLoan);
        }
        return fineRepository.save(fine);
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