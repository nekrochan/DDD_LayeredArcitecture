package bank.command.handler;

import bank.command.command.DepositMoneyCommand;
import bank.command.model.Account;
import bank.command.repository.AccountRepository;

public class DepositMoneyCommandHandler implements CommandHandler<DepositMoneyCommand> {
    private final AccountRepository accountRepository;

    public DepositMoneyCommandHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void handle(DepositMoneyCommand command) {
        // Получение счета
        Account account = accountRepository.findById(command.getAccountId());

        // Пополнение счета
        account.deposit(command.getAmount(), command.getDescription());

        // Сохранение изменений
        accountRepository.save(account);
    }
}
