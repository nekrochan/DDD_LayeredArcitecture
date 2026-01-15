package bank.command.handler;

import bank.command.command.WithdrawMoneyCommand;
import bank.command.model.Account;
import bank.command.repository.AccountRepository;

public class WithdrawMoneyCommandHandler implements CommandHandler<WithdrawMoneyCommand> {
    private final AccountRepository accountRepository;

    public WithdrawMoneyCommandHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void handle(WithdrawMoneyCommand command) {
        // Получение счета
        Account account = accountRepository.findById(command.getAccountId());

        // Снятие средств
        account.withdraw(command.getAmount(), command.getDescription());

        // Сохранение изменений
        accountRepository.save(account);
    }
}
