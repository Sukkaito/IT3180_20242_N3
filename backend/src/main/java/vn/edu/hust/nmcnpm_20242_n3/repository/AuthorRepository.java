package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.entity.Author;

import java.util.Optional;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Integer> {
    Optional<Author> findByName(String name);
    boolean existsByName(String name);
    Optional<Author> findById(Integer id);
    void deleteById(Integer id);
}