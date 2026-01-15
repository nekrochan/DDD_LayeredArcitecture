package bank.query.repository;

import bank.query.model.TransactionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionViewRepository {
    private final Map<String, TransactionView> transactions = new HashMap<>();

    public void save(TransactionView transaction) {
        transactions.put(transaction.getId(), transaction);
    }

    public List<TransactionView> findByAccountId(String accountId) {
        return transactions.values().stream()
                .filter(t -> t.getAccountId().equals(accountId))
                .collect(Collectors.toList());
    }

    public List<TransactionView> findAll() {
        return new ArrayList<>(transactions.values());
    }
}
