package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class JdbcTransferStatusDao implements TransferStatusDao {
    private JdbcTemplate jdbcTemplate;


    public JdbcTransferStatusDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }

    @Override
    public TransferStatus getTransferStatusByDesc(String description) {
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_statuses WHERE transfer_status_desc = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, description);
        TransferStatus transferStatus = null;
        if (result.next()) {
            int transferStatusID = result.getInt("transfer_status_id");
            String transferStatusDesc = result.getString("transfer_status_desc");
            transferStatus = new TransferStatus(transferStatusID, transferStatusDesc);

        }

        return transferStatus;
    }

    @Override
    public TransferStatus getTransferStatusById(int transferStatusId) {
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_statuses WHERE transfer_status_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferStatusId);
        TransferStatus transferStatus = null;
        if(result.next()) {
            int transferStatusId2 = result.getInt("transfer_status_id");
            String transferStatusDesc = result.getString(("transfer_status_desc"));
            transferStatus = new TransferStatus(transferStatusId2, transferStatusDesc);

        }

        return transferStatus;
    }

}
