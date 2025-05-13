package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.entity.Fine;
import vn.edu.hust.nmcnpm_20242_n3.service.FineService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/fines")
public class FineController {

    private final FineService fineService;

    @Autowired
    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @GetMapping
    public ResponseEntity<List<Fine>> getAllFines() {
        return ResponseEntity.ok(fineService.getAllFines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fine> getFineById(@PathVariable String id) {
        return ResponseEntity.ok(fineService.getFineById(id));
    }

    @PostMapping
    public ResponseEntity<Fine> addFine(@RequestBody Fine fine) {
        return new ResponseEntity<>(fineService.addFine(fine), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fine> updateFine(@PathVariable String id, @RequestBody Fine fine) {
        return ResponseEntity.ok(fineService.updateFine(id, fine));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFine(@PathVariable String id) {
        fineService.deleteFine(id);
        return ResponseEntity.ok("Fine deleted successfully");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Fine>> getFinesByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(fineService.getFinesByUserId(userId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Fine>> getFinesByDateRange(
            @RequestParam Date startDate,
            @RequestParam Date endDate) {
        return ResponseEntity.ok(fineService.getFinesByDateRange(startDate, endDate));
    }

    @GetMapping("/amount-greater-than")
    public ResponseEntity<List<Fine>> getFinesByAmount(@RequestParam double amount) {
        return ResponseEntity.ok(fineService.getFinesByAmountGreaterThan(amount));
    }
}
