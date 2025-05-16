package vn.edu.hust.nmcnpm_20242_n3.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        dto.setPassword(user.getPassword());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        if (user.getRole() != null) {
            dto.setRoleId(user.getRole().getId());
            dto.setRoleName(user.getRole().getName().toString());
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
        if (staffDTO.getName() == null || staffDTO.getEmail() == null || staffDTO.getPassword() == null ||
            staffDTO.getName().trim().isEmpty() || staffDTO.getEmail().trim().isEmpty() || staffDTO.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Name, email, and password are required");
        }

        if (userRepository.findByEmail(staffDTO.getEmail()).stream().anyMatch(u -> u.getRole() != null &&
            (RoleEnum.STAFF.equals(u.getRole().getName()) || RoleEnum.ADMIN.equals(u.getRole().getName())))) {
            throw new IllegalArgumentException("Staff with email " + staffDTO.getEmail() + " already exists");
        }

        User user = convertToEntity(staffDTO);

        if (staffDTO.getRoleId() != null || staffDTO.getRoleName() != null) {
            Role role = null;
            if (staffDTO.getRoleId() != null) {
                role = roleRepository.findById(staffDTO.getRoleId())
                        .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + staffDTO.getRoleId()));
            } else if (staffDTO.getRoleName() != null) {
                RoleEnum roleEnum;
                try {
                    roleEnum = RoleEnum.valueOf(staffDTO.getRoleName());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid role name: " + staffDTO.getRoleName());
                }
                role = roleRepository.findByName(roleEnum)
                        .orElseThrow(() -> new IllegalArgumentException("Role " + staffDTO.getRoleName() + " not found in database"));
            }

            if (staffDTO.getRoleName() != null) {
                if (!RoleEnum.STAFF.equals(role.getName()) && !RoleEnum.ADMIN.equals(role.getName())) {
                    throw new IllegalArgumentException("Role must be STAFF or ADMIN");
                }
                if (staffDTO.getRoleId() != null && !staffDTO.getRoleName().equals(role.getName().toString())) {
                    throw new IllegalArgumentException("Role ID " + staffDTO.getRoleId() + " does not match role name " + staffDTO.getRoleName());
                }
            } else if (staffDTO.getRoleId() != null) {
                if (!RoleEnum.STAFF.equals(role.getName()) && !RoleEnum.ADMIN.equals(role.getName())) {
                    throw new IllegalArgumentException("Role must be STAFF or ADMIN");
                }
            }
            user.setRole(role);
        } else {
            Role defaultRole = roleRepository.findByName(RoleEnum.STAFF)
                    .orElseThrow(() -> new IllegalArgumentException("Default role STAFF not found in database"));
            user.setRole(defaultRole);
        }

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
        if (staffDTO.getRoleId() != null || staffDTO.getRoleName() != null) {
            Role role = null;
            if (staffDTO.getRoleId() != null) {
                role = roleRepository.findById(staffDTO.getRoleId())
                        .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + staffDTO.getRoleId()));
            } else if (staffDTO.getRoleName() != null) {
                RoleEnum roleEnum;
                try {
                    roleEnum = RoleEnum.valueOf(staffDTO.getRoleName());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid role name: " + staffDTO.getRoleName());
                }
                role = roleRepository.findByName(roleEnum)
                        .orElseThrow(() -> new IllegalArgumentException("Role " + staffDTO.getRoleName() + " not found in database"));
            }

            if (staffDTO.getRoleName() != null) {
                if (!RoleEnum.STAFF.equals(role.getName()) && !RoleEnum.ADMIN.equals(role.getName())) {
                    throw new IllegalArgumentException("Role must be STAFF or ADMIN");
                }
                if (staffDTO.getRoleId() != null && !staffDTO.getRoleName().equals(role.getName().toString())) {
                    throw new IllegalArgumentException("Role ID " + staffDTO.getRoleId() + " does not match role name " + staffDTO.getRoleName());
                }
            } else if (staffDTO.getRoleId() != null) {
                if (!RoleEnum.STAFF.equals(role.getName()) && !RoleEnum.ADMIN.equals(role.getName())) {
                    throw new IllegalArgumentException("Role must be STAFF or ADMIN");
                }
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