package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;

public interface TransferStatusDao {
    TransferStatus getTransferStatusByDesc(String description);

    TransferStatus getTransferStatusById(int transferStatusId);
}
