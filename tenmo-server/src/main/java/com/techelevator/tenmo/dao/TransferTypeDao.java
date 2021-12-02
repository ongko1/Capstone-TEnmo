package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferType;

public interface TransferTypeDao {
    TransferType getTransferTypeFromDescription(String description);
    TransferType getTransferTypeFromId(int transferId);
}
