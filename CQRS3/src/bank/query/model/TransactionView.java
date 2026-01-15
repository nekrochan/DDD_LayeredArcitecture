package bank.query.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionView {
    private String id;
    private String accountId;
    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime timestamp;
    private String relatedAccountId; // для переводов

    public TransactionView(String accountId, TransactionType type, double amount,
                           String description, LocalDateTime timestamp) {
        this.id = UUID.randomUUID().toString();
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
    }

    public TransactionView(String accountId, TransactionType type, double amount,
                           String description, LocalDateTime timestamp, String relatedAccountId) {
        this(accountId, type, amount, description, timestamp);
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
