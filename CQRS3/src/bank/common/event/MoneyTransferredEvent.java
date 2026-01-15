package bank.common.event;

public class MoneyTransferredEvent extends Event {
    private final String sourceAccountId;
    private final String targetAccountId;
    private final double amount;
    private final String description;

    public MoneyTransferredEvent(String sourceAccountId, String targetAccountId, double amount, String description) {
        super();
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.description = description;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public String getTargetAccountId() {
        return targetAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
