package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookRequestTypeEnum;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRequestRepository extends CrudRepository<BookRequest, String> {
    @Query("SELECT b FROM BookRequest b WHERE b.bookCopy.id = :bookCopyId and b.user.id = :userId and b.status = PENDING and b.type=:type")
    List<BookRequest> checkForOverlappingRequest(@Param("bookCopyId") Integer bookCopyIdd,
            @Param("userId") String userId, @Param("type") BookRequestTypeEnum type);

    @Query("SELECT b FROM BookRequest b WHERE b.user.id = :userId")
    List<BookRequest> findByUserId(@Param("userId") String userId);

    List<BookRequest> findAll();
    Optional<BookRequest> findById(String id);
}