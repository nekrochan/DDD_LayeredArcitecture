package bank.command.command;

import java.util.UUID;

public class TransferMoneyCommand implements Command {
    private final String commandId;
    private final String sourceAccountId;
    private final String targetAccountId;
    private final double amount;
    private final String description;

    public TransferMoneyCommand(String sourceAccountId, String targetAccountId, double amount, String description) {
        this.commandId = UUID.randomUUID().toString();
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.description = description;
    }

    @Override
    public String getCommandId() {
        return commandId;
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
