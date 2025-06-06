package vn.edu.hust.nmcnpm_20242_n3.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.dto.UserDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.*;
import vn.edu.hust.nmcnpm_20242_n3.constant.RoleEnum;
import vn.edu.hust.nmcnpm_20242_n3.repository.RoleRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    public UserDTO createUser(UserDTO dto) throws IllegalArgumentException {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByUserName(dto.getUserName())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // Encode password

        RoleEnum roleEnum = RoleEnum.valueOf(dto.getRoleName());
        Role role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RuntimeException("Role not found: " + dto.getRoleName()));
        user.setRole(role);

        User saved = userRepository.save(user);
        return mapToDTO(saved);
    }
    public Optional<UserDTO> getUserById(String id) {
        return userRepository.findById(id).map(this::mapToDTO);
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::mapToDTO);
    }

    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUserName(username).map(this::mapToDTO);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setUserName(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPassword(null);
        dto.setRoleName(user.getRole().getName().name());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
    public UserDTO updateUser(String id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (!user.getUsername().equals(dto.getUserName()) && userRepository.existsByUserName(dto.getUserName())) {
            throw new IllegalArgumentException("Username already exists");
        }
        user.setName(dto.getName());
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());

        RoleEnum roleEnum = RoleEnum.valueOf(dto.getRoleName());
        Role role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);

        User updated = userRepository.save(user);
        return mapToDTO(updated);
    }

    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1. Delete fines first
        user.getFines().clear();

        // 2. Delete book requests
        user.getBookRequests().clear();

        // 3. Delete book loans
        user.getBookLoans().clear();

        // 4. Delete subscriptions
        user.getSubscriptions().clear();

        // 5. Delete the user
        userRepository.delete(user);
    }

    public List<UserDTO> searchUsers(String id, String email, String username) {
        List<User> users = userRepository.searchUsers(id, email, username);
        return users.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public User loadUserByUsername(String username) throws EntityNotFoundException {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
    }

    public UserDTO createUser(String name, String username, String password, String email) {
        UserDTO dto = new UserDTO();
        dto.setName(name);
        dto.setUserName(username);
        dto.setPassword(password);
        dto.setEmail(email);
        dto.setRoleName(RoleEnum.USER.name()); // Default role
        return createUser(dto);
    }
}
