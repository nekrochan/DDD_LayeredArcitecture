package bank.command.command;

import java.util.UUID;

public class CreateAccountCommand implements Command {
    private final String commandId;
    private final String accountNumber;
    private final String ownerName;
    private final double initialBalance;

    public CreateAccountCommand(String accountNumber, String ownerName, double initialBalance) {
        this.commandId = UUID.randomUUID().toString();
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.initialBalance = initialBalance;
    }

    @Override
    public String getCommandId() {
        return commandId;
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
