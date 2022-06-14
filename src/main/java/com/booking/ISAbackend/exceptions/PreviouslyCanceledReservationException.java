package com.booking.ISAbackend.exceptions;

public class PreviouslyCanceledReservationException extends Exception{
    public PreviouslyCanceledReservationException(String s) {
        super(s);
    }
}
