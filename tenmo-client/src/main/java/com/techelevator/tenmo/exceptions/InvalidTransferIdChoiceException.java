package com.techelevator.tenmo.exceptions;

public class InvalidTransferIdChoiceException extends Exception{
    public InvalidTransferIdChoiceException() {
        super("Invalid Transfer Id, please choose another Id.");
    }

}
