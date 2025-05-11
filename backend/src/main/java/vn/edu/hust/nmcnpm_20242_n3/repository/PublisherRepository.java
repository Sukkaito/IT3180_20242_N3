package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.entity.Publisher;

import java.util.Optional;

@Repository
public interface PublisherRepository extends CrudRepository<Publisher, Integer> {
    Optional<Publisher> findByName(String name);
    boolean existsByName(String name);
    void deleteById(Integer id);
    Optional<Publisher> findById(Integer id);
}
