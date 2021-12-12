package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public BigDecimal getBalance(String user) {
        String sql = "SELECT balance FROM accounts JOIN users ON accounts.user_id = users.user_id WHERE username = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user);
        BigDecimal balance = null;

        if (results.next()) {
            /*
            String accountBalance = results.getString("balance");
            balance = new BigDecimal(accountBalance);
             */
            balance = new BigDecimal(results.getString("balance"));
        }
        return balance;


    }

    @Override
    public Account getAccountByUserID(int userId) {
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        Account account = null;
        if(result.next()) {
            account = mapRowToAccount(result);
        }
        return account;
    }

    @Override
    public Account getAccountByAccountID(int accountId) {
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        Account account = null;
        if(result.next()) {
            account = mapRowToAccount(result);
        }
        return account;
    }

    @Override
    public void updateBalance(Account accountToUpdate) {
        String sql = "UPDATE accounts " +
                "SET balance = ? " +
                "WHERE account_id = ?";

        jdbcTemplate.update(sql, accountToUpdate.getBalance(), accountToUpdate.getAccountId());
    }


    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;

    }
}
