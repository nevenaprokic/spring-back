package com.booking.ISAbackend.exceptions;

public class AlreadyExitingUsernameException extends Exception{

    public AlreadyExitingUsernameException(String mess){
        super(mess);
    }
}
