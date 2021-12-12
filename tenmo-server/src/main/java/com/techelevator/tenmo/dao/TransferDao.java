package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    void createTransfer(Transfer transfer2);

    List<Transfer> getTransferByUserId(int userId);

    Transfer getTransferByTransferId(int id);

    List<Transfer> getAllTransfers();

    List<Transfer> getPendingTransfersByUserId(int userId);

    void updateTransfer(Transfer transfer2);
}
