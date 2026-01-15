package bank.query.model;

import java.time.LocalDateTime;

public class AccountView {
    private String id;
    private String accountNumber;
    private String ownerName;
    private double currentBalance;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private int transactionCount;

    public AccountView(String id, String accountNumber, String ownerName, double currentBalance,
                       LocalDateTime createdAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.currentBalance = currentBalance;
        this.createdAt = createdAt;
        this.lastUpdatedAt = createdAt;
        this.transactionCount = 0;
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

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void incrementTransactionCount() {
        this.transactionCount++;
        this.lastUpdatedAt = LocalDateTime.now();
    }
}
