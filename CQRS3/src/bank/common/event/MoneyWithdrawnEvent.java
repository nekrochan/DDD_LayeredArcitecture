package bank.common.event;

public class MoneyWithdrawnEvent extends Event {
    private final String accountId;
    private final double amount;
    private final String description;

    public MoneyWithdrawnEvent(String accountId, double amount, String description) {
        super();
        this.accountId = accountId;
        this.amount = amount;
        this.description = description;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}