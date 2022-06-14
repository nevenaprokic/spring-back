package com.booking.ISAbackend.service;

import com.booking.ISAbackend.dto.InstructorProfileData;
import com.booking.ISAbackend.dto.OfferSearchParamsDTO;
import com.booking.ISAbackend.exceptions.InvalidPhoneNumberException;
import com.booking.ISAbackend.model.Instructor;

import java.io.IOException;
import java.util.List;

public interface InstructorService {
    List<InstructorProfileData> searchInstructors(String firstName, String lastName, String address, String phoneNumber) throws InvalidPhoneNumberException, IOException;

    List<InstructorProfileData> findAll() throws IOException;

    boolean sendDeleteRequest(String email, String reason);

    List<InstructorProfileData> searchInstructorsClient(OfferSearchParamsDTO params) throws IOException;
}
