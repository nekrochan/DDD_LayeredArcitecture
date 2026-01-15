package bank.query.repository;

import bank.common.exception.AccountNotFoundException;
import bank.query.model.AccountView;

import java.util.*;

public class AccountViewRepository {
    private final Map<String, AccountView> accounts = new HashMap<>();

    public void save(AccountView accountView) {
        accounts.put(accountView.getId(), accountView);
    }

    public AccountView findById(String id) {
        return Optional.ofNullable(accounts.get(id))
                .orElseThrow(() -> new AccountNotFoundException("Счет не найден: " + id));
    }

    public Optional<AccountView> findByAccountNumber(String accountNumber) {
        return accounts.values().stream()
                .filter(a -> a.getAccountNumber().equals(accountNumber))
                .findFirst();
    }

    public List<AccountView> findAll() {
        return new ArrayList<>(accounts.values());
    }
}
