package vn.edu.hust.nmcnpm_20242_n3.dto;

import java.util.List;

public class RoleDTO {
    private Integer id;
    private String name;
    private List<String> permissions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}