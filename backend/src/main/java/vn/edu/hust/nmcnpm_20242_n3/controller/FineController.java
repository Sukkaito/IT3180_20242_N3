package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import vn.edu.hust.nmcnpm_20242_n3.entity.Fine;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import vn.edu.hust.nmcnpm_20242_n3.repository.UserRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookLoanRepository;
import vn.edu.hust.nmcnpm_20242_n3.service.FineService;
import vn.edu.hust.nmcnpm_20242_n3.dto.FineDTO;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fines")
public class FineController {

    private final FineService fineService;
    private final UserRepository userRepository;
    private final BookLoanRepository bookLoanRepository;

    @Autowired
    public FineController(FineService fineService, UserRepository userRepository,
            BookLoanRepository bookLoanRepository) {
        this.fineService = fineService;
        this.userRepository = userRepository;
        this.bookLoanRepository = bookLoanRepository;
    }

    @GetMapping
    public ResponseEntity<List<FineDTO>> getAllFines() {
        List<FineDTO> dtos = fineService.getAllFines()
                .stream()
                .map(FineDTO::fromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFineById(@PathVariable String id) {
        try {
            Fine fine = fineService.getFineById(id);
            return new ResponseEntity<>(FineDTO.fromEntity(fine), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PostMapping
    public ResponseEntity<?> addFine(@RequestBody FineDTO fineDTO) {
        try {
            if (fineDTO.getUserId() == null) {
                return ResponseEntity.badRequest().body("User information is missing or invalid.");
            }
            if (fineDTO.getBookLoanId() == null) {
                return ResponseEntity.badRequest().body("Book loan information is missing or invalid.");
            }
            if (fineDTO.getAmount() <= 0) {
                return ResponseEntity.badRequest().body("Fine amount must be greater than zero.");
            }
            if (fineDTO.getDescription() == null || fineDTO.getDescription().isEmpty()) {
                return ResponseEntity.badRequest().body("Description cannot be null or empty.");
            }
            User user = userRepository.findById(fineDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            BookLoan bookLoan = bookLoanRepository.findById(fineDTO.getBookLoanId())
                    .orElseThrow(() -> new IllegalArgumentException("BookLoan not found"));
            Fine fine = new Fine();
            fine.setAmount(fineDTO.getAmount());
            fine.setDescription(fineDTO.getDescription());
            fine.setUser(user);
            fine.setBookLoan(bookLoan);
            Fine savedFine = fineService.addFine(fine);
            return new ResponseEntity<>(FineDTO.fromEntity(savedFine), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFine(@PathVariable String id, @RequestBody FineDTO fineDTO) {
        try {
            User user = userRepository.findById(fineDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            BookLoan bookLoan = bookLoanRepository.findById(fineDTO.getBookLoanId())
                    .orElseThrow(() -> new IllegalArgumentException("BookLoan not found"));
            Fine fine = new Fine();
            fine.setId(id);
            fine.setAmount(fineDTO.getAmount());
            fine.setDescription(fineDTO.getDescription());
            fine.setUser(user);
            fine.setBookLoan(bookLoan);
            Fine updatedFine = fineService.updateFine(id, fine);
            return new ResponseEntity<>(FineDTO.fromEntity(updatedFine), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFine(@PathVariable(name = "id") String id) {
        try {
            fineService.deleteFine(id);
            return new ResponseEntity<>("Fine deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FineDTO>> getFinesByUserId(@PathVariable(name = "userId") String userId) {
        List<FineDTO> dtos = fineService.getFinesByUserId(userId)
                .stream()
                .map(FineDTO::fromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<FineDTO>> getFinesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        List<FineDTO> dtos = fineService.getFinesByDateRange(startDate, endDate)
                .stream()
                .map(FineDTO::fromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}