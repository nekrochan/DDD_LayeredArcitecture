package bank.common.event;

public class AccountCreatedEvent extends Event {
    private final String accountId;
    private final String accountNumber;
    private final String ownerName;
    private final double initialBalance;

    public AccountCreatedEvent(String accountId, String accountNumber, String ownerName, double initialBalance) {
        super();
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.initialBalance = initialBalance;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public double getInitialBalance() {
        return initialBalance;
    }
}

