package com.booking.ISAbackend.exceptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String mess){
        super(mess);
    }
}
