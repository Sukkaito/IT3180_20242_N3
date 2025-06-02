package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String userName);
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);
    @Query("SELECT u FROM User u " +
            "WHERE (:id IS NULL OR u.id LIKE %:id%) " +
            "AND (:email IS NULL OR u.email LIKE %:email%) " +
            "AND (:username IS NULL OR u.userName LIKE %:username%)")
    List<User> searchUsers(@Param("id") String id,
                           @Param("email") String email,
                           @Param("username") String username);

}
