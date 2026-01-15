package bank;

import bank.api.console.ConsoleInterface;
import bank.api.facade.BankFacade;
import bank.command.command.TransferMoneyCommand;
import bank.command.command.CreateAccountCommand;
import bank.command.command.DepositMoneyCommand;
import bank.command.command.WithdrawMoneyCommand;
import bank.command.handler.*;
import bank.command.repository.AccountRepository;
import bank.common.event.EventBus;
import bank.query.repository.AccountViewRepository;
import bank.query.repository.TransactionViewRepository;
import bank.query.service.AccountQueryService;
import bank.query.service.EventHandler;

public class BankApplication {
    public static void main(String[] args) {
        // Инициализация репозиториев
        AccountRepository commandAccountRepository = new AccountRepository();
        AccountViewRepository queryAccountRepository = new AccountViewRepository();
        TransactionViewRepository transactionRepository = new TransactionViewRepository();

        // Инициализация обработчиков событий
        EventHandler eventHandler = new EventHandler(queryAccountRepository, transactionRepository);
        EventBus.getInstance().register(eventHandler);

        // Инициализация командной шины и обработчиков команд
        CommandBus commandBus = new CommandBus();
        commandBus.register(CreateAccountCommand.class, new CreateAccountCommandHandler(commandAccountRepository));
        commandBus.register(DepositMoneyCommand.class, new DepositMoneyCommandHandler(commandAccountRepository));
        commandBus.register(WithdrawMoneyCommand.class, new WithdrawMoneyCommandHandler(commandAccountRepository));
        commandBus.register(TransferMoneyCommand.class, new TransferMoneyCommandHandler(commandAccountRepository));

        // Инициализация сервиса запросов
        AccountQueryService queryService = new AccountQueryService(queryAccountRepository, transactionRepository);

        // Инициализация фасада
        BankFacade bankFacade = new BankFacade(commandBus, queryService);

        // Создание тестовых данных
        try {
            bankFacade.createAccount("40817810123456789012", "Иванов Иван Иванович", 1000);
            bankFacade.createAccount("40817810987654321098", "Петров Петр Петрович", 500);

            // Получаем ID созданных счетов
            String account1Id = bankFacade.getAllAccounts().get(0).getId();
            String account2Id = bankFacade.getAllAccounts().get(1).getId();

            // Выполняем несколько операций
            bankFacade.deposit(account1Id, 500, "Зарплата");
            bankFacade.withdraw(account2Id, 100, "Покупка продуктов");
            bankFacade.transfer(account1Id, account2Id, 200, "Возврат долга");

            System.out.println("Тестовые данные успешно созданы");
        } catch (Exception e) {
            System.out.println("Ошибка при создании тестовых данных: " + e.getMessage());
        }

        // Запуск консольного интерфейса
        ConsoleInterface consoleUI = new ConsoleInterface(bankFacade);
        consoleUI.start();
    }
}
