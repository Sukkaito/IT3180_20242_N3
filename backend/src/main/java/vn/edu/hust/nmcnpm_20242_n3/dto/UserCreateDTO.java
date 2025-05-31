package vn.edu.hust.nmcnpm_20242_n3.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserCreateDTO {
    private String name;
    private String userName;
    private String email;
    private String password;
    private String roleName;
}
