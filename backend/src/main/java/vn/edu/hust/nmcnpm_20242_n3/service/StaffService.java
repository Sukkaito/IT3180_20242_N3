package vn.edu.hust.nmcnpm_20242_n3.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.entity.Staff;
import vn.edu.hust.nmcnpm_20242_n3.repository.StaffRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {

    private final StaffRepository staffRepository;

    @Autowired
    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public List<Staff> getAllStaff() {
        return (List<Staff>) staffRepository.findAll();
    }

    public Optional<Staff> findByEmail(String email) {
        return staffRepository.findByEmail(email);
    }

    public Staff findById(String id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + id));
    }

    public Staff addStaff(Staff staff) {
        if (staffRepository.existsByEmail(staff.getEmail())) {
            throw new IllegalArgumentException("Staff with email " + staff.getEmail() + " already exists");
        }
        return staffRepository.save(staff);
    }

    public Staff updateById(String id, Staff staff) {
        Staff existingStaff = staffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found"));
        existingStaff.setName(staff.getName());
        existingStaff.setEmail(staff.getEmail());
        existingStaff.setRole(staff.getRole());
        return staffRepository.save(existingStaff);
    }

    @Transactional
    public void deleteById(String id) {
        if (!staffRepository.existsById(id)) {
            throw new IllegalArgumentException("Staff with ID " + id + " does not exist");
        }
        staffRepository.deleteById(id);
    }
}