package vn.edu.hust.nmcnpm_20242_n3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookRequestStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookRequestTypeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link vn.edu.hust.nmcnpm_20242_n3.entity.BookRequest}
 */
@AllArgsConstructor
@Getter
public class BookRequestDTO implements Serializable {
    private final String id;
    private final String bookLoanId;
    private final String bookName;
    private final String username;
    private final BookRequestStatusEnum status;
    private final BookRequestTypeEnum type;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}