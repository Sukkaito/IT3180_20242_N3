package vn.edu.hust.nmcnpm_20242_n3.dto;

import lombok.Data;
import java.util.Date;

@Data
public class FineDTO {
    private String id;
    private double amount;
    private Date createdAt;
    private Date updatedAt;
    private String bookLoanId;
}

