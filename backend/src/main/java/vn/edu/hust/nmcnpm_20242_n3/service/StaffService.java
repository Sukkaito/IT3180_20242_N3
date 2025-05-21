package vn.edu.hust.nmcnpm_20242_n3.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.dto.RoleDTO;
import vn.edu.hust.nmcnpm_20242_n3.dto.StaffDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.entity.Role;
import vn.edu.hust.nmcnpm_20242_n3.repository.UserRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.RoleRepository;
import vn.edu.hust.nmcnpm_20242_n3.constant.RoleEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public StaffService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // Convert User to StaffDTO
    private StaffDTO convertToDTO(User user) {
        StaffDTO dto = new StaffDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setUserName(user.getUserName());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        if (user.getRole() != null) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(user.getRole().getId());
            roleDTO.setName(user.getRole().getName().toString());
            dto.setRole(roleDTO);
        }
        return dto;
    }

    // Convert StaffDTO to User
    private User convertToEntity(StaffDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setUserName(dto.getUserName());
        user.setPassword(dto.getPassword());
        user.setCreatedAt(dto.getCreatedAt());
        user.setUpdatedAt(dto.getUpdatedAt());
        return user;
    }

    private Role resolveRole(RoleDTO roleDTO) {
        Role role = null;
        if (roleDTO != null && (roleDTO.getId() != null || roleDTO.getName() != null)) {
            if (roleDTO.getId() != null) {
                role = roleRepository.findById(roleDTO.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleDTO.getId()));
            } else if (roleDTO.getName() != null) {
                RoleEnum roleEnum;
                try {
                    roleEnum = RoleEnum.valueOf(roleDTO.getName());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid role name: " + roleDTO.getName());
                }
                role = roleRepository.findByName(roleEnum)
                        .orElseThrow(() -> new IllegalArgumentException("Role " + roleDTO.getName() + " not found in database"));
            }

            if (!RoleEnum.STAFF.equals(role.getName()) && !RoleEnum.ADMIN.equals(role.getName())) {
                throw new IllegalArgumentException("Role must be STAFF or ADMIN");
            }
            if (roleDTO.getId() != null && roleDTO.getName() != null && !roleDTO.getName().equals(role.getName().toString())) {
                throw new IllegalArgumentException("Role ID " + roleDTO.getId() + " does not match role name " + roleDTO.getName());
            }
        }
        return role != null ? role : roleRepository.findByName(RoleEnum.STAFF)
                .orElseThrow(() -> new IllegalArgumentException("Default role STAFF not found in database"));
    }

    // Get all Staff (STAFF and ADMIN)
    public List<StaffDTO> getAllStaff() {
        List<User> staffList = new ArrayList<>();
        staffList.addAll(userRepository.findByRole_Name(RoleEnum.STAFF));
        staffList.addAll(userRepository.findByRole_Name(RoleEnum.ADMIN));
        return staffList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get Staff By ID
    public StaffDTO findById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + id));
        if (user.getRole() == null || (!RoleEnum.STAFF.equals(user.getRole().getName()) && !RoleEnum.ADMIN.equals(user.getRole().getName()))) {
            throw new IllegalArgumentException("User is not a staff or admin");
        }
        return convertToDTO(user);
    }

    // Add new Staff
    @Transactional
    public StaffDTO addStaff(StaffDTO staffDTO) {
        if (staffDTO.getName() == null || staffDTO.getEmail() == null || staffDTO.getPassword() == null || staffDTO.getUserName() == null ||
            staffDTO.getName().trim().isEmpty() || staffDTO.getEmail().trim().isEmpty() ||
            staffDTO.getPassword().trim().isEmpty() || staffDTO.getUserName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name, email, userName and password are required");
        }

        if (userRepository.findByEmail(staffDTO.getEmail()).stream().anyMatch(u -> u.getRole() != null &&
            (RoleEnum.STAFF.equals(u.getRole().getName()) || RoleEnum.ADMIN.equals(u.getRole().getName())))) {
            throw new IllegalArgumentException("Staff with email " + staffDTO.getEmail() + " already exists");
        }

        // Check if userName already exists
        if (userRepository.findByUserName(staffDTO.getUserName()).isPresent()) {
            throw new IllegalArgumentException("UserName " + staffDTO.getUserName() + " already exists");
        }

        User user = convertToEntity(staffDTO);
        user.setRole(resolveRole(staffDTO.getRole()));

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    // Update Staff
    @Transactional
    public StaffDTO updateStaff(String id, StaffDTO staffDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + id));
        if (existingUser.getRole() == null || (!RoleEnum.STAFF.equals(existingUser.getRole().getName()) && !RoleEnum.ADMIN.equals(existingUser.getRole().getName()))) {
            throw new IllegalArgumentException("User is not a staff or admin");
        }

        if (staffDTO.getName() != null && !staffDTO.getName().trim().isEmpty()) {
            existingUser.setName(staffDTO.getName());
        }
        if (staffDTO.getEmail() != null && !staffDTO.getEmail().trim().isEmpty()) {
            existingUser.setEmail(staffDTO.getEmail());
        }
        if (staffDTO.getUserName() != null && !staffDTO.getUserName().trim().isEmpty()) {
            existingUser.setUserName(staffDTO.getUserName());
        }
        if (staffDTO.getPassword() != null && !staffDTO.getPassword().trim().isEmpty()) {
            existingUser.setPassword(staffDTO.getPassword());
        }
        if (staffDTO.getRole() != null && (staffDTO.getRole().getId() != null || staffDTO.getRole().getName() != null)) {
            Role role = null;
            if (staffDTO.getRole().getId() != null) {
                role = roleRepository.findById(staffDTO.getRole().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + staffDTO.getRole().getId()));
            } else if (staffDTO.getRole().getName() != null) {
                RoleEnum roleEnum;
                try {
                    roleEnum = RoleEnum.valueOf(staffDTO.getRole().getName());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid role name: " + staffDTO.getRole().getName());
                }
                role = roleRepository.findByName(roleEnum)
                        .orElseThrow(() -> new IllegalArgumentException("Role " + staffDTO.getRole().getName() + " not found in database"));
            }

            if (!RoleEnum.STAFF.equals(role.getName()) && !RoleEnum.ADMIN.equals(role.getName())) {
                throw new IllegalArgumentException("Role must be STAFF or ADMIN");
            }
            if (staffDTO.getRole().getId() != null && staffDTO.getRole().getName() != null &&
                !staffDTO.getRole().getName().equals(role.getName().toString())) {
                throw new IllegalArgumentException("Role ID " + staffDTO.getRole().getId() + " does not match role name " + staffDTO.getRole().getName());
            }
            existingUser.setRole(role);
        }

        User updatedUser = userRepository.save(existingUser);
        return convertToDTO(updatedUser);
    }

    // Delete Staff
    @Transactional
    public void deleteStaff(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + id));
        if (user.getRole() == null || (!RoleEnum.STAFF.equals(user.getRole().getName()) && !RoleEnum.ADMIN.equals(user.getRole().getName()))) {
            throw new IllegalArgumentException("User is not a staff or admin");
        }
        userRepository.delete(user);
    }
}