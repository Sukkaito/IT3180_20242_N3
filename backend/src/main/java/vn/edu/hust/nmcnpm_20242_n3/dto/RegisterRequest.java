package vn.edu.hust.nmcnpm_20242_n3.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String name;
    private String userName;
    private String email;
    private String password;
}
