package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.repository.CrudRepository;

import vn.edu.hust.nmcnpm_20242_n3.entity.User;

public interface UserRepository extends CrudRepository<User, String> {

}
