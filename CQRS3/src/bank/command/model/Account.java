package bank.command.model;

import bank.common.event.*;
import bank.common.exception.InsufficientFundsException;

import java.util.UUID;

public class Account {
    private final String id;
    private final String accountNumber;
    private final String ownerName;
    private double balance;

    public Account(String accountNumber, String ownerName, double initialBalance) {
        this.id = UUID.randomUUID().toString();
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = initialBalance;

        // Публикация события создания счета
        EventBus.getInstance().publish(
                new AccountCreatedEvent(id, accountNumber, ownerName, initialBalance)
        );
    }

    public Account(String id, String accountNumber, String ownerName, double balance) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
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

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }

        this.balance += amount;

        // Публикация события пополнения счета
        EventBus.getInstance().publish(
                new MoneyDepositedEvent(id, amount, description)
        );
    }

    public void withdraw(double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }

        if (balance < amount) {
            throw new InsufficientFundsException(
                    "Недостаточно средств: доступно " + balance + ", запрошено " + amount
            );
        }

        this.balance -= amount;

        // Публикация события снятия со счета
        EventBus.getInstance().publish(
                new MoneyWithdrawnEvent(id, amount, description)
        );
    }

    public void transferTo(Account targetAccount, double amount, String description) {
        if (targetAccount == null) {
            throw new IllegalArgumentException("Целевой счет не может быть null");
        }

        withdraw(amount, "Transfer to " + targetAccount.getAccountNumber());
        targetAccount.deposit(amount, "Transfer from " + this.accountNumber);

        // Публикация события перевода между счетами
        EventBus.getInstance().publish(
                new MoneyTransferredEvent(id, targetAccount.getId(), amount, description)
        );
    }
}

