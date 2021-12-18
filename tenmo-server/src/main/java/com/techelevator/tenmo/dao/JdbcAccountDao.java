package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.InsufficientFundsException;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public BigDecimal getBalance(int userId) {
        String sql = "SELECT balance FROM accounts  WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        BigDecimal balance = null;

        if (results.next()) {
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
    @Transactional
    public void checkAndUpdateBalance(BigDecimal amount, int accountIdFrom, int accountIdTo) throws InsufficientFundsException{
        checkBalance(amount, accountIdFrom);
        String sql = "UPDATE accounts " +
                "SET balance = balance - ? " + // new updated sender balance
                "WHERE account_id = ?; ";
        jdbcTemplate.update(sql, amount, accountIdFrom);

        sql = "UPDATE accounts " +
                "SET balance = balance + ? " + // new updated sender balance
                "WHERE account_id = ?; ";
        jdbcTemplate.update(sql, amount, accountIdTo);
    }

    private void checkBalance(BigDecimal amount, int accountIdFrom) throws InsufficientFundsException {
        String sql = "SELECT (balance >= ?) as is_valid " +
                "FROM accounts " +
                "WHERE account_id = ?;";
        SqlRowSet isValid = jdbcTemplate.queryForRowSet(sql, amount , accountIdFrom);
        if (isValid.next()) {
            if (!isValid.getBoolean("is_valid"))
                throw new InsufficientFundsException();
        }

    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;

    }
}
