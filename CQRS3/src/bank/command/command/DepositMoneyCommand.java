package bank.command.command;

import java.util.UUID;

public class DepositMoneyCommand implements Command {
    private final String commandId;
    private final String accountId;
    private final double amount;
    private final String description;

    public DepositMoneyCommand(String accountId, double amount, String description) {
        this.commandId = UUID.randomUUID().toString();
        this.accountId = accountId;
        this.amount = amount;
        this.description = description;
    }

    @Override
    public String getCommandId() {
        return commandId;
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
