package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.entity.Staff;
import vn.edu.hust.nmcnpm_20242_n3.service.StaffService;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping // Get All
    public List<Staff> getAllStaff() {
        return staffService.getAllStaff();
    }

    @GetMapping("/search/{email}") // Get By Email
    public Staff getStaffByEmail(@PathVariable String email) {
        return staffService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Staff with email " + email + " not found"));
    }

    @PostMapping // Add New
    public Staff addStaff(@RequestBody Staff staff) {
        return staffService.addStaff(staff);
    }

    @PutMapping("/update/{id}") // Update By Id
    public Staff updateStaff(@PathVariable String id, @RequestBody Staff staff) {
        return staffService.updateById(id, staff);
    }

    @DeleteMapping("/delete/{id}") // Delete By Id
    public void deleteStaff(@PathVariable String id) {
        staffService.deleteById(id);
    }
}