package com.booking.ISAbackend.service;

import com.booking.ISAbackend.dto.OwnerRegistrationRequestDTO;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.Owner;
import com.booking.ISAbackend.model.Reservation;

import java.util.List;

public interface RegistrationRequestService {
    boolean save(OwnerRegistrationRequestDTO request) throws InvalidAddressException, InvalidEmail, InvalidCredential, InvalidPhoneNumber, InvalidPasswordException;
    List<OwnerRegistrationRequestDTO> getAll();
    void acceptRegistrationRequest(int id) throws InterruptedException;
    void discardRegistrationRequest(int id, String message) throws InterruptedException;


}
