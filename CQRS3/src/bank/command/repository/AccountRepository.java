package bank.command.repository;

import bank.command.model.Account;
import bank.common.exception.AccountNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AccountRepository {
    private final Map<String, Account> accounts = new HashMap<>();

    public void save(Account account) {
        accounts.put(account.getId(), account);
    }

    public Account findById(String id) {
        return Optional.ofNullable(accounts.get(id))
                .orElseThrow(() -> new AccountNotFoundException("Счет не найден: " + id));
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accounts.values().stream()
                .filter(a -> a.getAccountNumber().equals(accountNumber))
                .findFirst();
    }
}

