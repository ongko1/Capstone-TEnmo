package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
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
    public List<Transfer> getTransfersByUserId(int userId) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfers " +
                "JOIN accounts ON accounts.account_id = transfers.account_from OR accounts.account_id = transfers.account_to " +
                "WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        List<Transfer> transfers = new ArrayList<>();

        while(results.next()) {
            transfers.add(mapResultToTransfer(results));
        }

        return transfers;
    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfers WHERE transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);
        Transfer transfer = null;

        if(result.next()){
            transfer = mapResultToTransfer(result);
        }

        return transfer;
    }

    @Override
    public List<Transfer> getAllTransfers() {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfers";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        List<Transfer> transfers = new ArrayList<>();

        while(results.next()){
            transfers.add(mapResultToTransfer(results));
        }

        return transfers;
    }

    @Override
    public List<Transfer> getPendingTransfers(int userId) {
        String sql = "SELECT transfer_id, transfer_type_id, transfers.transfer_status_id, account_from, account_to, amount " +
                "FROM transfers " +
                "JOIN accounts ON accounts.account_id = transfers.account_from " +
                "JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id " +
                "WHERE user_id = ? AND transfer_status_desc = 'Pending'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        List<Transfer> transfers = new ArrayList<>();

        while(results.next()) {
            transfers.add(mapResultToTransfer(results));
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

    private Transfer mapResultToTransfer(SqlRowSet result) {
        int transferId = result.getInt("transfer_id");
        int transferTypeId = result.getInt("transfer_type_id");
        int transferStatusId = result.getInt("transfer_status_id");
        int accountFrom = result.getInt("account_from");
        int accountTo = result.getInt("account_to");
        String amountDouble = result.getString("amount");

        Transfer transfer = new Transfer(transferId, transferTypeId, transferStatusId, accountFrom, accountTo, new BigDecimal(amountDouble));
        return transfer;
    }


}
