package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.exceptions.InsufficientFunds;
import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {
    private final int TRANSFER_TYPE_REQUEST = 1;
    private final int TRANSFER_TYPE_SEND = 2;

    private final int TRANSFER_STATUS_PENDING = 1;
    private final int TRANSFER_STATUS_APPROVED = 2;
    private final int TRANSFER_STATUS_REJECTED = 3;

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private  TransferDao transferDao;

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {
        System.out.println(principal.getName());
        return accountDao.getBalance(principal.getName());
    }

    @RequestMapping(path="/users", method = RequestMethod.GET)
    public List<User> getUsers() {
        return userDao.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path="/transfers/{id}", method = RequestMethod.POST)
    public void addTransfer(@RequestBody Transfer transfer, @PathVariable int id) throws InsufficientFunds {

        BigDecimal amountToTransfer = transfer.getAmount();
        Account accountFrom = accountDao.getAccountByAccountID(transfer.getAccountFrom());
        Account accountTo = accountDao.getAccountByAccountID(transfer.getAccountTo());

        if(transfer.getTransferStatusId()==2) {
            // check balance
            accountFrom.sendMoney(amountToTransfer);
            accountTo.receiveMoney(amountToTransfer);
        }
        transferDao.createTransfer(transfer);

        // update balance
        accountDao.updateAccount(accountFrom);
        accountDao.updateAccount(accountTo);
    }

    @RequestMapping(path="/account/user/{id}", method = RequestMethod.GET)
    public Account getAccountByUserId(@PathVariable int id) {
        return accountDao.getAccountByUserID(id);
    }

    @RequestMapping(path="/account/{id}", method = RequestMethod.GET)
    public Account getAccountFromAccountId(@PathVariable int id) {
        return accountDao.getAccountByAccountID(id);
    }

    @RequestMapping(path="/transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id) {
        return transferDao.getTransferByTransferId(id);
    }

    @RequestMapping(path="/transfers", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers() {
        return transferDao.getAllTransfers();
    }

    @RequestMapping(path="/transfers/user/{userId}", method = RequestMethod.GET)
    public List<Transfer> getTransferByUserId(@PathVariable int userId) {
        return transferDao.getTransferByUserId(userId);
    }

    @RequestMapping(path="/users/{id}", method = RequestMethod.GET)
    public User getUserByUserId(@PathVariable int id) {
        return userDao.getUserByUserId(id);
    }

    @RequestMapping(path="/transfers/user/{userId}/pending", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfersByUserId(@PathVariable int userId) {
        return transferDao.getPendingTransfers(userId);
    }

    @RequestMapping(path="/transfers/{id}", method = RequestMethod.PUT)
    public void updateTransferStatus(@RequestBody Transfer transfer, @PathVariable int id) throws InsufficientFunds {

        // only go through with the transfer if it is approved
        if(transfer.getTransferStatusId() == TRANSFER_STATUS_APPROVED) {

            BigDecimal amountToTransfer = transfer.getAmount();
            Account accountFrom = accountDao.getAccountByAccountID(transfer.getAccountFrom());
            Account accountTo = accountDao.getAccountByAccountID(transfer.getAccountTo());

            // UPDATE BALANCE
            accountFrom.sendMoney(amountToTransfer);
            accountTo.receiveMoney(amountToTransfer);

            transferDao.updateTransfer(transfer);

            accountDao.updateAccount(accountFrom);
            accountDao.updateAccount(accountTo);
        } else {
            transferDao.updateTransfer(transfer);
        }

    }
}
