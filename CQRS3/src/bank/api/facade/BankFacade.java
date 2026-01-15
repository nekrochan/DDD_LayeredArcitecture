package bank.api.facade;

import bank.command.command.CreateAccountCommand;
import bank.command.command.DepositMoneyCommand;
import bank.command.command.TransferMoneyCommand;
import bank.command.command.WithdrawMoneyCommand;
import bank.command.handler.CommandBus;
import bank.query.dto.AccountDTO;
import bank.query.dto.AccountStatisticsDTO;
import bank.query.dto.TransactionDTO;
import bank.query.service.AccountQueryService;

import java.util.List;

public class BankFacade {
    private final CommandBus commandBus;
    private final AccountQueryService queryService;

    public BankFacade(CommandBus commandBus, AccountQueryService queryService) {
        this.commandBus = commandBus;
        this.queryService = queryService;
    }

    // Команды (запись)
    public void createAccount(String accountNumber, String ownerName, double initialBalance) {
        commandBus.dispatch(new CreateAccountCommand(accountNumber, ownerName, initialBalance));
    }

    public void deposit(String accountId, double amount, String description) {
        commandBus.dispatch(new DepositMoneyCommand(accountId, amount, description));
    }

    public void withdraw(String accountId, double amount, String description) {
        commandBus.dispatch(new WithdrawMoneyCommand(accountId, amount, description));
    }

    public void transfer(String sourceAccountId, String targetAccountId, double amount, String description) {
        commandBus.dispatch(new TransferMoneyCommand(sourceAccountId, targetAccountId, amount, description));
    }

    // Запросы (чтение)
    public AccountDTO getAccount(String accountId) {
        return queryService.getAccountById(accountId);
    }

    public List<AccountDTO> getAllAccounts() {
        return queryService.getAllAccounts();
    }

    public List<TransactionDTO> getAccountTransactions(String accountId) {
        return queryService.getAccountTransactions(accountId);
    }

    public AccountStatisticsDTO getAccountStatistics(String accountId) {
        return queryService.getAccountStatistics(accountId);
    }
}