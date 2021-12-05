package com.techelevator.tenmo.exceptions;

public class InvalidTransferIdChoice extends Exception{
    public InvalidTransferIdChoice() {
        super("Invalid Transfer Id, please choose another Id.");
    }

}
