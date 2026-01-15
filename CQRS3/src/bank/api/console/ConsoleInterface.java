package bank.api.console;

import bank.api.facade.BankFacade;
import bank.common.exception.AccountNotFoundException;
import bank.common.exception.InsufficientFundsException;
import bank.query.dto.AccountDTO;
import bank.query.dto.AccountStatisticsDTO;
import bank.query.dto.TransactionDTO;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ConsoleInterface {
    private final BankFacade bankFacade;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public ConsoleInterface(BankFacade bankFacade) {
        this.bankFacade = bankFacade;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int choice;
        do {
            showMainMenu();
            choice = readIntInput();
            scanner.nextLine(); // очистка буфера
            handleMainMenuChoice(choice);
        } while (choice != 0);
    }

    private void showMainMenu() {
        System.out.println("\n===== Банковская система =====");
        System.out.println("1. Создать новый счет");
        System.out.println("2. Показать все счета");
        System.out.println("3. Информация о счете");
        System.out.println("4. Пополнить счет");
        System.out.println("5. Снять со счета");
        System.out.println("6. Перевести средства");
        System.out.println("7. Показать транзакции");
        System.out.println("8. Статистика счета");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private void handleMainMenuChoice(int choice) {
        try {
            switch (choice) {
                case 0:
                    System.out.println("Выход из программы...");
                    break;
                case 1:
                    createAccount();
                    break;
                case 2:
                    showAllAccounts();
                    break;
                case 3:
                    showAccountDetails();
                    break;
                case 4:
                    depositMoney();
                    break;
                case 5:
                    withdrawMoney();
                    break;
                case 6:
                    transferMoney();
                    break;
                case 7:
                    showTransactions();
                    break;
                case 8:
                    showAccountStatistics();
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        } catch (AccountNotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (InsufficientFundsException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
    }

    private void createAccount() {
        System.out.print("Введите номер счета: ");
        String accountNumber = scanner.nextLine().trim();

        System.out.print("Введите имя владельца: ");
        String ownerName = scanner.nextLine().trim();

        System.out.print("Введите начальный баланс: ");
        double initialBalance = readDoubleInput();
        scanner.nextLine(); // очистка буфера

        try {
            bankFacade.createAccount(accountNumber, ownerName, initialBalance);
            System.out.println("Счет успешно создан!");
        } catch (Exception e) {
            System.out.println("Ошибка при создании счета: " + e.getMessage());
        }
    }

    private void showAllAccounts() {
        List<AccountDTO> accounts = bankFacade.getAllAccounts();

        if (accounts.isEmpty()) {
            System.out.println("Нет доступных счетов.");
            return;
        }

        System.out.println("\n=== Все счета ===");
        System.out.printf("%-36s %-15s %-25s %-15s%n", "ID", "Номер счета", "Владелец", "Баланс");
        System.out.println("------------------------------------------------------------------------------------------------");

        for (AccountDTO account : accounts) {
            System.out.printf("%-36s %-15s %-25s %,.2f руб.%n",
                    account.getId(),
                    account.getAccountNumber(),
                    account.getOwnerName(),
                    account.getCurrentBalance());
        }
    }

    private void showAccountDetails() {
        System.out.print("Введите ID счета: ");
        String accountId = scanner.nextLine().trim();

        AccountDTO account = bankFacade.getAccount(accountId);

        System.out.println("\n=== Информация о счете ===");
        System.out.println("ID счета: " + account.getId());
        System.out.println("Номер счета: " + account.getAccountNumber());
        System.out.println("Владелец: " + account.getOwnerName());
        System.out.println("Текущий баланс: " + String.format("%,.2f руб.", account.getCurrentBalance()));
        System.out.println("Создан: " + account.getCreatedAt().format(dateFormatter));
        System.out.println("Последнее обновление: " + account.getLastUpdatedAt().format(dateFormatter));
    }

    private void depositMoney() {
        System.out.print("Введите ID счета: ");
        String accountId = scanner.nextLine().trim();

        System.out.print("Введите сумму пополнения: ");
        double amount = readDoubleInput();
        scanner.nextLine(); // очистка буфера

        System.out.print("Введите описание (или оставьте пустым): ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            description = "Пополнение счета";
        }

        bankFacade.deposit(accountId, amount, description);
        System.out.println("Счет успешно пополнен на " + String.format("%,.2f руб.", amount));
    }

    private void withdrawMoney() {
        System.out.print("Введите ID счета: ");
        String accountId = scanner.nextLine().trim();

        System.out.print("Введите сумму снятия: ");
        double amount = readDoubleInput();
        scanner.nextLine(); // очистка буфера

        System.out.print("Введите описание (или оставьте пустым): ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            description = "Снятие со счета";
        }

        bankFacade.withdraw(accountId, amount, description);
        System.out.println("Со счета успешно снято " + String.format("%,.2f руб.", amount));
    }

    private void transferMoney() {
        System.out.print("Введите ID счета отправителя: ");
        String sourceAccountId = scanner.nextLine().trim();

        System.out.print("Введите ID счета получателя: ");
        String targetAccountId = scanner.nextLine().trim();

        System.out.print("Введите сумму перевода: ");
        double amount = readDoubleInput();
        scanner.nextLine(); // очистка буфера

        System.out.print("Введите описание (или оставьте пустым): ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            description = "Перевод средств";
        }

        bankFacade.transfer(sourceAccountId, targetAccountId, amount, description);
        System.out.println("Перевод на сумму " + String.format("%,.2f руб.", amount) + " успешно выполнен");
    }

    private void showTransactions() {
        System.out.print("Введите ID счета: ");
        String accountId = scanner.nextLine().trim();

        List<TransactionDTO> transactions = bankFacade.getAccountTransactions(accountId);

        if (transactions.isEmpty()) {
            System.out.println("Для данного счета нет транзакций.");
            return;
        }

        System.out.println("\n=== Транзакции счета ===");
        System.out.printf("%-23s %-12s %-15s %-30s%n", "Дата", "Тип", "Сумма", "Описание");
        System.out.println("--------------------------------------------------------------------------------");

        for (TransactionDTO transaction : transactions) {
            String typeStr;
            switch (transaction.getType()) {
                case DEPOSIT:
                    typeStr = "Пополнение";
                    break;
                case WITHDRAWAL:
                    typeStr = "Снятие";
                    break;
                case TRANSFER_OUT:
                    typeStr = "Перевод (исх.)";
                    break;
                case TRANSFER_IN:
                    typeStr = "Перевод (вх.)";
                    break;
                default:
                    typeStr = transaction.getType().toString();
            }

            System.out.printf("%-23s %-12s %,15.2f %-30s%n",
                    transaction.getTimestamp().format(dateFormatter),
                    typeStr,
                    transaction.getAmount(),
                    transaction.getDescription());
        }
    }

    private void showAccountStatistics() {
        System.out.print("Введите ID счета: ");
        String accountId = scanner.nextLine().trim();

        AccountStatisticsDTO stats = bankFacade.getAccountStatistics(accountId);

        System.out.println("\n=== Статистика счета ===");
        System.out.println("Номер счета: " + stats.getAccountNumber());
        System.out.println("Текущий баланс: " + String.format("%,.2f руб.", stats.getCurrentBalance()));
        System.out.println("Количество транзакций: " + stats.getTransactionCount());
        System.out.println("Средняя сумма транзакции: " + String.format("%,.2f руб.", stats.getAverageTransactionAmount()));
        System.out.println("Наибольшее пополнение: " + String.format("%,.2f руб.", stats.getLargestDeposit()));
        System.out.println("Наибольшее снятие: " + String.format("%,.2f руб.", stats.getLargestWithdrawal()));
    }

    private int readIntInput() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            return -1;
        }
    }

    private double readDoubleInput() {
        try {
            return scanner.nextDouble();
        } catch (Exception e) {
            return -1;
        }
    }
}
