package vn.edu.hust.nmcnpm_20242_n3.dto;

import java.util.Date;

import vn.edu.hust.nmcnpm_20242_n3.entity.Fine;

public class FineDTO {
    private String id;
    private double amount;
    private String description;
    private String userId;
    private String bookLoanId;
    private Date createdAt;
    private Date updatedAt;

    // Constructors
    public FineDTO() {
    }

    public FineDTO(String id, double amount, String description, String userId, String bookLoanId, Date createdAt,
            Date updatedAt) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.userId = userId;
        this.bookLoanId = bookLoanId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static FineDTO fromEntity(Fine fine) {
        FineDTO dto = new FineDTO();
        dto.setId(fine.getId());
        dto.setAmount(fine.getAmount());
        dto.setDescription(fine.getDescription());
        dto.setCreatedAt(fine.getCreatedAt());
        dto.setUpdatedAt(fine.getUpdatedAt());
        if (fine.getUser() != null)
            dto.setUserId(fine.getUser().getId());
        if (fine.getBookLoan() != null)
            dto.setBookLoanId(fine.getBookLoan().getId());
        return dto;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookLoanId() {
        return bookLoanId;
    }

    public void setBookLoanId(String bookLoanId) {
        this.bookLoanId = bookLoanId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}