package com.techelevator.tenmo.model;

import com.techelevator.tenmo.exceptions.InsufficientFundsException;

import java.math.BigDecimal;

public class Account {
    private int accountId;
    private int userId;
    private BigDecimal balance;

    public Account(int accountId, int userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() { return balance; }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void sendMoney(BigDecimal amount) throws InsufficientFundsException {
        BigDecimal newBalance = getBalance().subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
            setBalance(newBalance);
        } else {
            throw new InsufficientFundsException();
        }

    }

    public void receiveMoney(BigDecimal amount) {
        setBalance(getBalance().add(amount));
    }

}
