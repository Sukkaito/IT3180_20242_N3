package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import vn.edu.hust.nmcnpm_20242_n3.constant.RoleEnum;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.role.name = :role ORDER BY u.createdAt ASC")
    List<User> findByRole_Name(RoleEnum role);
    List<User> findByEmail(String email);
    Optional<User> findByUserName(String userName);
}

