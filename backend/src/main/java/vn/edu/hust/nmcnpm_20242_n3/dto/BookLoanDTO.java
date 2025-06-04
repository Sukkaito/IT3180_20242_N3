package vn.edu.hust.nmcnpm_20242_n3.dto;

import lombok.Data;
import java.util.Date;

@Data
public class BookLoanDTO {
    private String id;
    private Date loanDate;
    private Date returnDate;
    private Date actualReturnDate;
    private String status;
    private String currentBookRequestId;
    private Integer bookCopyId;
}

