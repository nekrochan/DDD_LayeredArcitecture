package bank.query.service;

import bank.common.event.*;
import bank.query.model.AccountView;
import bank.query.model.TransactionType;
import bank.query.model.TransactionView;
import bank.query.repository.AccountViewRepository;
import bank.query.repository.TransactionViewRepository;

public class EventHandler implements EventBus.EventHandler {
    private final AccountViewRepository accountRepository;
    private final TransactionViewRepository transactionRepository;

    public EventHandler(AccountViewRepository accountRepository, TransactionViewRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void handle(Event event) {
        if (event instanceof AccountCreatedEvent) {
            handleAccountCreated((AccountCreatedEvent) event);
        } else if (event instanceof MoneyDepositedEvent) {
            handleMoneyDeposited((MoneyDepositedEvent) event);
        } else if (event instanceof MoneyWithdrawnEvent) {
            handleMoneyWithdrawn((MoneyWithdrawnEvent) event);
        } else if (event instanceof MoneyTransferredEvent) {
            handleMoneyTransferred((MoneyTransferredEvent) event);
        }
    }

    private void handleAccountCreated(AccountCreatedEvent event) {
        AccountView accountView = new AccountView(
                event.getAccountId(),
                event.getAccountNumber(),
                event.getOwnerName(),
                event.getInitialBalance(),
                event.getTimestamp()
        );

        accountRepository.save(accountView);

        // Если начальный баланс > 0, создаем транзакцию пополнения
        if (event.getInitialBalance() > 0) {
            TransactionView transaction = new TransactionView(
                    event.getAccountId(),
                    TransactionType.DEPOSIT,
                    event.getInitialBalance(),
                    "Начальный баланс",
                    event.getTimestamp()
            );

            transactionRepository.save(transaction);
            accountView.incrementTransactionCount();
            accountRepository.save(accountView);
        }
    }

    private void handleMoneyDeposited(MoneyDepositedEvent event) {
        AccountView accountView = accountRepository.findById(event.getAccountId());
        accountView.setCurrentBalance(accountView.getCurrentBalance() + event.getAmount());
        accountView.incrementTransactionCount();
        accountRepository.save(accountView);

        TransactionView transaction = new TransactionView(
                event.getAccountId(),
                TransactionType.DEPOSIT,
                event.getAmount(),
                event.getDescription(),
                event.getTimestamp()
        );

        transactionRepository.save(transaction);
    }

    private void handleMoneyWithdrawn(MoneyWithdrawnEvent event) {
        AccountView accountView = accountRepository.findById(event.getAccountId());
        accountView.setCurrentBalance(accountView.getCurrentBalance() - event.getAmount());
        accountView.incrementTransactionCount();
        accountRepository.save(accountView);

        TransactionView transaction = new TransactionView(
                event.getAccountId(),
                TransactionType.WITHDRAWAL,
                event.getAmount(),
                event.getDescription(),
                event.getTimestamp()
        );

        transactionRepository.save(transaction);
    }

    private void handleMoneyTransferred(MoneyTransferredEvent event) {
        // Обработка для счета-отправителя
        AccountView sourceAccount = accountRepository.findById(event.getSourceAccountId());
        sourceAccount.incrementTransactionCount();
        accountRepository.save(sourceAccount);

        TransactionView outTransaction = new TransactionView(
                event.getSourceAccountId(),
                TransactionType.TRANSFER_OUT,
                event.getAmount(),
                event.getDescription(),
                event.getTimestamp(),
                event.getTargetAccountId()
        );

        transactionRepository.save(outTransaction);

        // Обработка для счета-получателя
        AccountView targetAccount = accountRepository.findById(event.getTargetAccountId());
        targetAccount.incrementTransactionCount();
        accountRepository.save(targetAccount);

        TransactionView inTransaction = new TransactionView(
                event.getTargetAccountId(),
                TransactionType.TRANSFER_IN,
                event.getAmount(),
                "Перевод от " + sourceAccount.getAccountNumber(),
                event.getTimestamp(),
                event.getSourceAccountId()
        );

        transactionRepository.save(inTransaction);
    }
}
