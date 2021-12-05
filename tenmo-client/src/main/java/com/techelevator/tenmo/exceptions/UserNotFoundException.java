package com.techelevator.tenmo.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("User not found, enter a valid user ID.");
    }
}
