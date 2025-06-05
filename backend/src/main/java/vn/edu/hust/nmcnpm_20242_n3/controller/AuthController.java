package vn.edu.hust.nmcnpm_20242_n3.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.nmcnpm_20242_n3.constant.RoleEnum;
import vn.edu.hust.nmcnpm_20242_n3.dto.RegisterRequest;
import vn.edu.hust.nmcnpm_20242_n3.entity.Role;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.repository.RoleRepository;
import vn.edu.hust.nmcnpm_20242_n3.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        Role defaultRole = roleRepository.findByName(RoleEnum.USER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = new User();
        user.setName(request.getName());
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(defaultRole);

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
}
