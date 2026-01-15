package bank.query.service;

import bank.query.dto.AccountDTO;
import bank.query.dto.AccountStatisticsDTO;
import bank.query.dto.TransactionDTO;
import bank.query.model.AccountView;
import bank.query.model.TransactionType;
import bank.query.model.TransactionView;
import bank.query.repository.AccountViewRepository;
import bank.query.repository.TransactionViewRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AccountQueryService {
    private final AccountViewRepository accountRepository;
    private final TransactionViewRepository transactionRepository;

    public AccountQueryService(AccountViewRepository accountRepository,
                               TransactionViewRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public AccountDTO getAccountById(String accountId) {
        AccountView account = accountRepository.findById(accountId);

        return new AccountDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getOwnerName(),
                account.getCurrentBalance(),
                account.getCreatedAt(),
                account.getLastUpdatedAt()
        );
    }

    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(account -> new AccountDTO(
                        account.getId(),
                        account.getAccountNumber(),
                        account.getOwnerName(),
                        account.getCurrentBalance(),
                        account.getCreatedAt(),
                        account.getLastUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getAccountTransactions(String accountId) {
        accountRepository.findById(accountId); // проверка существования счета

        return transactionRepository.findByAccountId(accountId).stream()
                .map(tx -> new TransactionDTO(
                        tx.getId(),
                        tx.getAccountId(),
                        tx.getType(),
                        tx.getAmount(),
                        tx.getDescription(),
                        tx.getTimestamp(),
                        tx.getRelatedAccountId()
                ))
                .sorted(Comparator.comparing(TransactionDTO::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public AccountStatisticsDTO getAccountStatistics(String accountId) {
        AccountView account = accountRepository.findById(accountId);
        List<TransactionView> transactions = transactionRepository.findByAccountId(accountId);

        double averageAmount = transactions.isEmpty() ? 0 :
                transactions.stream().mapToDouble(TransactionView::getAmount).average().orElse(0);

        double largestDeposit = transactions.stream()
                .filter(tx -> tx.getType() == TransactionType.DEPOSIT || tx.getType() == TransactionType.TRANSFER_IN)
                .mapToDouble(TransactionView::getAmount)
                .max().orElse(0);

        double largestWithdrawal = transactions.stream()
                .filter(tx -> tx.getType() == TransactionType.WITHDRAWAL || tx.getType() == TransactionType.TRANSFER_OUT)
                .mapToDouble(TransactionView::getAmount)
                .max().orElse(0);

        return new AccountStatisticsDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getCurrentBalance(),
                transactions.size(),
                averageAmount,
                largestDeposit,
                largestWithdrawal
        );
    }
}
