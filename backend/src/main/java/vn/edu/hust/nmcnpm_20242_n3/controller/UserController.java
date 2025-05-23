package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.dto.BookLoanDTO;
import vn.edu.hust.nmcnpm_20242_n3.dto.FineDTO;
import vn.edu.hust.nmcnpm_20242_n3.dto.UserCreateDTO;
import vn.edu.hust.nmcnpm_20242_n3.dto.UserDTO;
import vn.edu.hust.nmcnpm_20242_n3.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserCreateDTO dto) {
        try {
            UserDTO created = userService.createUser(dto);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchUser(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username
    ) {
        Optional<UserDTO> result = Optional.empty();

        if (id != null) {
            result = userService.getUserById(id);
        } else if (email != null) {
            result = userService.getUserByEmail(email);
        } else if (username != null) {
            result = userService.getUserByUsername(username);
        }

        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserDTO dto) {
        try {
            UserDTO updated = userService.updateUser(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{userId}/book-loans")
    public ResponseEntity<?> getBookLoansByUserId(@PathVariable String userId) {
        List<BookLoanDTO> loans = userService.getBookLoansByUserId(userId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{userId}/fines")
    public ResponseEntity<?> getFinesByUserId(@PathVariable String userId) {
        List<FineDTO> fines = userService.getFinesByUserId(userId);
        return ResponseEntity.ok(fines);
    }

}
