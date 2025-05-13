package vn.edu.hust.nmcnpm_20242_n3.repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.edu.hust.nmcnpm_20242_n3.entity.BookRequest;


@Repository
public interface BookRequestRepository extends CrudRepository<BookRequest, String> {

    @Query("SELECT b FROM BookRequest b WHERE b.bookCopy.id = :bookCopyId and b.user.id = :userId and b.status = PENDING")
    List<BookRequest> checkIfUserHasRequest(@Param("bookCopyId") String bookCopyIdd,@Param("userId") String userId);
    @Query("SELECT b FROM BookRequest b WHERE b.id = :bookRequestId and b.user.id = :userId")
    List<BookRequest> listAllRequestsFromUser(@Param("userId") String userId);
}
