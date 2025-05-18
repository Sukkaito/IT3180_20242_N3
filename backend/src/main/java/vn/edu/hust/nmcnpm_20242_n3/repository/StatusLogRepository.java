package vn.edu.hust.nmcnpm_20242_n3.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hust.nmcnpm_20242_n3.entity.StatusLog;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatusLogRepository extends CrudRepository<StatusLog, Long> {
    List<StatusLog> findByComponentOrderByTimestampDesc(String component);
    List<StatusLog> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime start, LocalDateTime end);
    List<StatusLog> findByComponentAndTimestampBetweenOrderByTimestampDesc(String component, LocalDateTime start, LocalDateTime end);
    List<StatusLog> findTop10ByOrderByTimestampDesc();
}