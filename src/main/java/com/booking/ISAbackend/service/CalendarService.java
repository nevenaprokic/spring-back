package com.booking.ISAbackend.service;

import com.booking.ISAbackend.dto.CalendarItem;
import com.booking.ISAbackend.dto.QuickReservationDTO;
import com.booking.ISAbackend.dto.ReservationDTO;
import com.booking.ISAbackend.dto.UnavailableDateDTO;
import com.booking.ISAbackend.exceptions.BusyDateIntervalException;
import com.booking.ISAbackend.exceptions.InvalidDateInterval;
import com.booking.ISAbackend.exceptions.PassedDateException;
import com.booking.ISAbackend.exceptions.UnavailableDatesAlreadyDefine;

import java.io.IOException;
import java.util.List;

public interface CalendarService {
    List<CalendarItem> getCalendarInfo(String ownerEmail,  int offerId);
    ReservationDTO getReservationDetails(int reservationId) throws IOException;
    void addOffersUnavailableDates(UnavailableDateDTO unavailableDates) throws BusyDateIntervalException, InvalidDateInterval, UnavailableDatesAlreadyDefine, PassedDateException;

    QuickReservationDTO getActionDetails(int actionId) throws IOException;
}
