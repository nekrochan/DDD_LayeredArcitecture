package bank.query.dto;

public class AccountStatisticsDTO {
    private String accountId;
    private String accountNumber;
    private double currentBalance;
    private int transactionCount;
    private double averageTransactionAmount;
    private double largestDeposit;
    private double largestWithdrawal;

    public AccountStatisticsDTO(String accountId, String accountNumber, double currentBalance,
                                int transactionCount, double averageTransactionAmount,
                                double largestDeposit, double largestWithdrawal) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.currentBalance = currentBalance;
        this.transactionCount = transactionCount;
        this.averageTransactionAmount = averageTransactionAmount;
        this.largestDeposit = largestDeposit;
        this.largestWithdrawal = largestWithdrawal;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public double getAverageTransactionAmount() {
        return averageTransactionAmount;
    }

    public double getLargestDeposit() {
        return largestDeposit;
    }

    public double getLargestWithdrawal() {
        return largestWithdrawal;
    }
}