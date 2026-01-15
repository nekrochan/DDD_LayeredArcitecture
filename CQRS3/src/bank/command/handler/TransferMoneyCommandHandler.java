package bank.command.handler;

import bank.command.command.TransferMoneyCommand;
import bank.command.model.Account;
import bank.command.repository.AccountRepository;

public class TransferMoneyCommandHandler implements CommandHandler<TransferMoneyCommand> {
    private final AccountRepository accountRepository;

    public TransferMoneyCommandHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void handle(TransferMoneyCommand command) {
        // Получение счетов
        Account sourceAccount = accountRepository.findById(command.getSourceAccountId());
        Account targetAccount = accountRepository.findById(command.getTargetAccountId());

        // Перевод средств
        sourceAccount.transferTo(targetAccount, command.getAmount(), command.getDescription());

        // Сохранение изменений
        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);
    }
}
