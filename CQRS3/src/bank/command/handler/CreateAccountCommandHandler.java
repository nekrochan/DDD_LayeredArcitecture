package bank.command.handler;

import bank.command.command.CreateAccountCommand;
import bank.command.model.Account;
import bank.command.repository.AccountRepository;

public class CreateAccountCommandHandler implements CommandHandler<CreateAccountCommand> {
    private final AccountRepository accountRepository;

    public CreateAccountCommandHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void handle(CreateAccountCommand command) {
        // Проверка, что счет с таким номером не существует
        if (accountRepository.findByAccountNumber(command.getAccountNumber()).isPresent()) {
            throw new IllegalArgumentException("Счет с номером " + command.getAccountNumber() + " уже существует");
        }

        // Создание нового счета
        Account account = new Account(
                command.getAccountNumber(),
                command.getOwnerName(),
                command.getInitialBalance()
        );

        // Сохранение счета в репозитории
        accountRepository.save(account);
    }
}
