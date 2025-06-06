package vn.edu.hust.nmcnpm_20242_n3.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.dto.UserDTO;
import vn.edu.hust.nmcnpm_20242_n3.service.AuthenticationService;
import vn.edu.hust.nmcnpm_20242_n3.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {

    private final AuthenticationManager authManager;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            authManager.authenticate(authToken);

            // Fetch user details to get the ID
            Optional<UserDTO> userDetails = userService.getUserByUsername(request.getUsername());
            if (userDetails.isEmpty()) {
                return ResponseEntity.status(401).body("User not found");
            }

            String userId = userDetails.get().getId();

            // Create a basic auth token (Base64 encoded userId:username:password)
            String basicAuthValue = Base64.getEncoder().encodeToString(
                    (request.getUsername() + ":" + request.getPassword()).getBytes(StandardCharsets.UTF_8));

            ResponseCookie userIdCookie = ResponseCookie.from("USERID", userId)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(3600 * 24 * 30) // 1 month
                    .httpOnly(false) // Allow client-side access for user ID
                    .build();

            ResponseCookie userNameCookie = ResponseCookie.from("USERNAME", request.getUsername())
                    .httpOnly(false) // Allow client-side access for username
                    .secure(true)
                    .path("/")
                    .maxAge(3600 * 24 * 30) // 1 month
                    .httpOnly(false)
                    .build();

            // Add cookie to response headers
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, userIdCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, userNameCookie.toString())
                    .body(basicAuthValue);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Add cookie to response headers
        return ResponseEntity.ok()
                .body("Logout successful!");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            UserDTO userDTO = userService.createUser(
                    request.getName(),
                    request.getUsername(),
                    request.getPassword(),
                    request.getEmail()
            );
            return ResponseEntity.ok("User registered successfully: " + userDTO.getUserName());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Basic ")) {
                return ResponseEntity.status(401).body("Invalid authorization header");
            }

            String base64Credentials = authHeader.substring("Basic ".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
            String[] values = credentials.split(":", 2);

            if (values.length != 2) {
                return ResponseEntity.status(401).body("Invalid credentials format");
            }

            String username = values[0];
            String password = values[1];

            // Authenticate user
            var authToken = new UsernamePasswordAuthenticationToken(username, password);
            authManager.authenticate(authToken);

            // Fetch user details to get the ID
            Optional<UserDTO> userDetails = userService.getUserByUsername(username);
            if (userDetails.isEmpty()) {
                return ResponseEntity.status(401).body("User not found");
            }

            return ResponseEntity.ok(userDetails.get());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Verification failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class RegisterRequest {
        private String name;
        private String username;
        private String password;
        private String email;
    }
}