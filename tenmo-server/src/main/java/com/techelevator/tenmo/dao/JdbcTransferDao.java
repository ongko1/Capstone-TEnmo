package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private final int TRANSFER_TYPE_REQUEST = 1;
    private final int TRANSFER_TYPE_SEND = 2;

    private final int TRANSFER_STATUS_PENDING = 1;
    private final int TRANSFER_STATUS_APPROVED = 2;
    private final int TRANSFER_STATUS_REJECTED = 3;

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        int transfer_id = jdbcTemplate.queryForObject(sql, int.class,transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount() );
        transfer.setTransferId(transfer_id);
    }

    @Override
    public List<Transfer> getTransferByUserId(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfers.transfer_type_id AS transfer_type_id, transfers.transfer_status_id AS transfer_status_id, "+
                "tt.transfer_type_desc AS transfer_type_desc, ts.transfer_status_desc AS transfer_status_desc, " +
                "amount, user_from.username AS user_from, " +
                "user_to.username AS user_to, account_from.account_id AS account_from_id, account_to.account_id AS account_to_id " +
                "FROM transfers " +
                "JOIN accounts AS account_from ON transfers.account_from = account_from.account_id " +
                "JOIN accounts AS account_to ON transfers.account_to = account_to.account_id " +
                "JOIN users AS user_from ON account_from.user_id = user_from.user_id " +
                "JOIN users AS user_to ON account_to.user_id = user_to.user_id " +
                "JOIN transfer_types AS tt ON tt.transfer_type_id = transfers.transfer_type_id " +
                "JOIN transfer_statuses AS ts ON ts.transfer_status_id = transfers.transfer_status_id " +
                "WHERE account_from.user_id = ? OR account_to.user_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (rowSet.next()) {
            transfers.add(mapRowToTransfer(rowSet));
        }
        return transfers;
    }

    @Override
    public Transfer getTransferByTransferId(int id) {
        Transfer transfer;
        String sql = "SELECT transfer_id, transfers.transfer_type_id AS transfer_type_id, transfers.transfer_status_id AS transfer_status_id, "+
                "tt.transfer_type_desc AS transfer_type_desc, ts.transfer_status_desc AS transfer_status_desc, " +
                "amount, user_from.username AS user_from, " +
                "user_to.username AS user_to, account_from.account_id AS account_from_id, account_to.account_id AS account_to_id " +
                "FROM transfers " +
                "JOIN accounts AS account_from ON transfers.account_from = account_from.account_id " +
                "JOIN accounts AS account_to ON transfers.account_to = account_to.account_id " +
                "JOIN users AS user_from ON account_from.user_id = user_from.user_id " +
                "JOIN users AS user_to ON account_to.user_id = user_to.user_id " +
                "JOIN transfer_types AS tt ON tt.transfer_type_id = transfers.transfer_type_id " +
                "JOIN transfer_statuses AS ts ON ts.transfer_status_id = transfers.transfer_status_id " +
                "WHERE transfer_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            transfer = mapRowToTransfer(rowSet);
        } else {
            transfer = null;
        }
        return transfer;
    }

    @Override
    public List<Transfer> getAllTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfers.transfer_type_id AS transfer_type_id, transfers.transfer_status_id AS transfer_status_id, "+
                "tt.transfer_type_desc AS transfer_type_desc, ts.transfer_status_desc AS transfer_status_desc, " +
                "amount, user_from.username AS user_from, " +
                "user_to.username AS user_to, account_from.account_id AS account_from_id, account_to.account_id AS account_to_id " +
                "FROM transfers " +
                "JOIN accounts AS account_from ON transfers.account_from = account_from.account_id " +
                "JOIN accounts AS account_to ON transfers.account_to = account_to.account_id " +
                "JOIN users AS user_from ON account_from.user_id = user_from.user_id " +
                "JOIN users AS user_to ON account_to.user_id = user_to.user_id " +
                "JOIN transfer_types AS tt ON tt.transfer_type_id = transfers.transfer_type_id " +
                "JOIN transfer_statuses AS ts ON ts.transfer_status_id = transfers.transfer_status_id;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while (rowSet.next()) {
            transfers.add(mapRowToTransfer(rowSet));
        }
        return transfers;
    }

    @Override
    public List<Transfer> getPendingTransfers(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfers.transfer_type_id AS transfer_type_id, transfers.transfer_status_id AS transfer_status_id, "+
                "tt.transfer_type_desc AS transfer_type_desc, ts.transfer_status_desc AS transfer_status_desc, " +
                "amount, user_from.username AS user_from, " +
                "user_to.username AS user_to, account_from.account_id AS account_from_id, account_to.account_id AS account_to_id " +
                "FROM transfers " +
                "JOIN accounts AS account_from ON transfers.account_from = account_from.account_id " +
                "JOIN accounts AS account_to ON transfers.account_to = account_to.account_id " +
                "JOIN users AS user_from ON account_from.user_id = user_from.user_id " +
                "JOIN users AS user_to ON account_to.user_id = user_to.user_id " +
                "JOIN transfer_types AS tt ON tt.transfer_type_id = transfers.transfer_type_id " +
                "JOIN transfer_statuses AS ts ON ts.transfer_status_id = transfers.transfer_status_id " +
                "WHERE account_from.user_id = ? AND transfers.transfer_status_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId, TRANSFER_STATUS_PENDING);
        while (rowSet.next()) {
            transfers.add(mapRowToTransfer(rowSet));
        }
        return transfers;
    }

    @Override
    public void updateTransfer(Transfer transfer) {
        String sql = "UPDATE transfers " +
                "SET transfer_status_id = ? " +
                "WHERE transfer_id = ?";

        jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferId());
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from_id"));
        transfer.setAccountTo(rowSet.getInt("account_to_id"));
        transfer.setUserFrom(rowSet.getString("user_from"));
        transfer.setUserTo(rowSet.getString("user_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        transfer.setTransferTypeDesc(rowSet.getString("transfer_type_desc"));
        transfer.setTransferStatusDesc(rowSet.getString("transfer_status_desc"));

        return transfer;
    }
}
