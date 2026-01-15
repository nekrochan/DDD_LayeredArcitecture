package bank.query.dto;

import java.time.LocalDateTime;

public class AccountDTO {
    private String id;
    private String accountNumber;
    private String ownerName;
    private double currentBalance;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

    public AccountDTO(String id, String accountNumber, String ownerName, double currentBalance,
                      LocalDateTime createdAt, LocalDateTime lastUpdatedAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.currentBalance = currentBalance;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }
}
