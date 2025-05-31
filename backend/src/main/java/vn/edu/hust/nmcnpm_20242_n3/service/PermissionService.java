package vn.edu.hust.nmcnpm_20242_n3.service;

import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.constant.PermissionEnum;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
@Service
public class PermissionService {
    public boolean hasPermission(User user, PermissionEnum permission) {
        if (user == null || user.getRole() == null) return false;
        return user.getRole().getPermissions().stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(permission.name()));
    }
}
