package vn.edu.hust.nmcnpm_20242_n3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookLoanStatusEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link vn.edu.hust.nmcnpm_20242_n3.entity.BookLoan}
 */
@AllArgsConstructor
@Getter
public class BookLoanDTO implements Serializable {
    private final String id;
    private final int bookCopyId;
    private final String bookCopyOriginalBookTitle;
    private final String userUserName;
    private final Date loanDate;
    private final Date DueDate;
    private final Date actualReturnDate;
    private final String status;
    private final Date LoanedAt;
    private final Date UpdatedAt;
}
