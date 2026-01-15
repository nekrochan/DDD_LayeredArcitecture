package bank.query.dto;

import bank.query.model.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {
    private String id;
    private String accountId;
    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime timestamp;
    private String relatedAccountId;

    public TransactionDTO(String id, String accountId, TransactionType type, double amount,
                          String description, LocalDateTime timestamp, String relatedAccountId) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
        this.relatedAccountId = relatedAccountId;
    }

    public String getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getRelatedAccountId() {
        return relatedAccountId;
    }
}
