package com.booking.ISAbackend.service;

import com.booking.ISAbackend.dto.NewQuickReservationDTO;
import com.booking.ISAbackend.dto.QuickReservationDTO;
import com.booking.ISAbackend.exceptions.InvalidPriceException;
import com.booking.ISAbackend.exceptions.RequiredFiledException;

import java.util.HashMap;
import java.util.List;

public interface QuickReservationService {
    List<QuickReservationDTO> findQuickReservationByOfferId(Integer id);
    Boolean checkQuickReservationByOfferId(Integer offerId, String startDate, Integer dateNumber);
    Integer addNewQuickReservation(NewQuickReservationDTO dto) throws InterruptedException;
    void addAdditionalServices(List<HashMap<String, String>> additionalServiceDTOs, Integer quickId) throws InvalidPriceException, RequiredFiledException;
}
