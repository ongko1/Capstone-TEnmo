package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.InsufficientFundsException;
import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {
    BigDecimal getBalance(int userId);
    Account getAccountByUserID(int userId);
    Account getAccountByAccountID(int accountId);

    void checkAndUpdateBalance(BigDecimal amount, int accountIdFrom, int accountIdTo) throws InsufficientFundsException;

}

