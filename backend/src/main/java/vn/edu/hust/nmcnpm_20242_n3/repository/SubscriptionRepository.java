package vn.edu.hust.nmcnpm_20242_n3.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.entity.Subscription;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    Optional<Subscription> findByBookCopyIdAndUserId(int bookCopyId, String userId);

    List<Subscription> findAllByBookCopyIdAndActive(int bookCopyId, boolean active);

    List<Subscription> findAllByActive(boolean b);
    List<Subscription> findAllByUserId(String userId);

    List<Subscription> findAllByBookCopyId(int bookCopyId);
}