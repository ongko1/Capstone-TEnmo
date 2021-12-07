package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {
    BigDecimal getBalance(String user);
    Account getAccountByUserID(int userId);
    Account getAccountByAccountID(int accountId);

    void updateAccount(Account accountToUpdate);

}

