package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.constant.PermissionEnum;
import vn.edu.hust.nmcnpm_20242_n3.dto.UserCreateDTO;
import vn.edu.hust.nmcnpm_20242_n3.dto.UserDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan;
import vn.edu.hust.nmcnpm_20242_n3.entity.Fine;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.service.PermissionService;
import vn.edu.hust.nmcnpm_20242_n3.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    private User getCurrentUser(String userId) {
        return userService.getUserEntityById(userId);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserCreateDTO dto,
                                        @RequestHeader("X-User-Id") String userId) {
        User currentUser = getCurrentUser(userId);
        if (!permissionService.hasPermission(currentUser, PermissionEnum.MANAGE_USERS)) {
            return ResponseEntity.status(403).body("Access denied: missing MANAGE_USERS permission");
        }
        try {
            UserDTO created = userService.createUser(dto);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestParam String currentUserId) {
        User currentUser = userService.getUserEntityById(currentUserId);
        if (!permissionService.hasPermission(currentUser, PermissionEnum.MANAGE_USERS)) {
            return ResponseEntity.status(403).body("Forbidden: No permission");
        }

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id, @RequestParam String currentUserId) {
        User currentUser = userService.getUserEntityById(currentUserId);
        if (!permissionService.hasPermission(currentUser, PermissionEnum.MANAGE_USERS)) {
            return ResponseEntity.status(403).body("Forbidden: No permission");
        }
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchUser(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username,
            @RequestParam String currentUserId
    ) {
        User currentUser = userService.getUserEntityById(currentUserId);
        if (!permissionService.hasPermission(currentUser, PermissionEnum.MANAGE_USERS)) {
            return ResponseEntity.status(403).body("Forbidden: No permission");
        }
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
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserDTO dto, @RequestParam String currentUserId) {
        User currentUser = userService.getUserEntityById(currentUserId);
        if (!permissionService.hasPermission(currentUser, PermissionEnum.MANAGE_USERS)) {
            return ResponseEntity.status(403).body("Forbidden: No permission");
        }

        try {
            UserDTO updated = userService.updateUser(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id, @RequestParam String currentUserId) {
        User currentUser = userService.getUserEntityById(currentUserId);
        if (!permissionService.hasPermission(currentUser, PermissionEnum.MANAGE_USERS)) {
            return ResponseEntity.status(403).body("Forbidden: No permission");
        }

        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }
    @GetMapping("/{userId}/book-loans")
    public ResponseEntity<?> getBookLoansByUserId(@PathVariable String userId, @RequestParam String currentUserId) {
        User currentUser = userService.getUserEntityById(currentUserId);
        if (!permissionService.hasPermission(currentUser, PermissionEnum.MANAGE_USERS)
                && !currentUser.getId().equals(userId)) {
            return ResponseEntity.status(403).body("Forbidden: No permission");
        }

        List<BookLoan> loans = userService.getBookLoansByUserId(userId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{userId}/fines")
    public ResponseEntity<?> getFinesByUserId(@PathVariable String userId, @RequestParam String currentUserId) {
        User currentUser = userService.getUserEntityById(currentUserId);
        if (!permissionService.hasPermission(currentUser, PermissionEnum.MANAGE_USERS)
                && !currentUser.getId().equals(userId)) {
            return ResponseEntity.status(403).body("Forbidden: No permission");
        }

        List<Fine> fines = userService.getFinesByUserId(userId);
        return ResponseEntity.ok(fines);
    }

}
