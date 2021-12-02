package com.techelevator.tenmo.model;

public class Account {
    private int account_id;
    private int user_id;
    private Balance balance;

    public Account(int account_id, int user_id, Balance balance) {
        this.account_id = account_id;
        this.user_id = user_id;
        this.balance = balance;
    }

    public int getAccount_id() { return account_id;     }

    public void setAccount_id(int account_id) { this.account_id = account_id; }

    public int getUser_id() { return user_id; }

    public void setUser_id(int user_id) { this.user_id = user_id; }

    public Balance getBalance() { return balance; }

    public void setBalance(Balance balance) {this.balance = balance; }
}
