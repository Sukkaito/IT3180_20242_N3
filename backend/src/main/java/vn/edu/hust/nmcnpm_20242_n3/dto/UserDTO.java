package vn.edu.hust.nmcnpm_20242_n3.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String name;
    private String userName;
    private String email;
    private String password;
    private String roleName;
    private Date createdAt;
    private Date updatedAt;

}
