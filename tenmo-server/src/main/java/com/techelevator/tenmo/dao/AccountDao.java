package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;

public interface AccountDao {
    Balance getBalance(String user);
    Account getAccountByUserID(int userId);
    Account getAccountByAccountID(int accountId);

    void updateAccount(Account accountToUpdate);
}
