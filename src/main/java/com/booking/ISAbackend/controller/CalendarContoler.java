package com.booking.ISAbackend.controller;


import com.booking.ISAbackend.dto.CalendarItem;
import com.booking.ISAbackend.dto.QuickReservationDTO;
import com.booking.ISAbackend.dto.ReservationDTO;
import com.booking.ISAbackend.dto.UnavailableDateDTO;
import com.booking.ISAbackend.exceptions.BusyDateIntervalException;
import com.booking.ISAbackend.exceptions.InvalidDateInterval;
import com.booking.ISAbackend.exceptions.PassedDateException;
import com.booking.ISAbackend.exceptions.UnavailableDatesAlreadyDefine;
import com.booking.ISAbackend.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("calendar")
public class CalendarContoler {

    @Autowired
    CalendarService calendarService;

    @GetMapping("info")
    public ResponseEntity<List<CalendarItem>> getCalendarInfo(@RequestParam String ownerEmail, @RequestParam int offerId) {

        try {
            List<CalendarItem> calendarItems = calendarService.getCalendarInfo(ownerEmail, offerId);
            return ResponseEntity.ok(calendarItems);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("reservation-info")
    public ResponseEntity<ReservationDTO> getReservationDetails(@RequestParam int reservationId) {
        try {
            ReservationDTO reservation = calendarService.getReservationDetails(reservationId);
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("add-unavailable-dates")
    public ResponseEntity<String> addOffersUnavailableDates(@RequestBody UnavailableDateDTO unavailableDates) {
        try {
            calendarService.addOffersUnavailableDates(unavailableDates);
            return ResponseEntity.ok("Successfully added new unavailable interval");
        } catch (BusyDateIntervalException e) {
            return ResponseEntity.badRequest().body("There are reservations in the selected interval");
        } catch (InvalidDateInterval e) {
            return ResponseEntity.badRequest().body("End date need to be after start date");
        } catch (UnavailableDatesAlreadyDefine e) {
            return ResponseEntity.badRequest().body("It is allowed to choose only intervals in which there are no already defined unavailable dates");
        }catch (PassedDateException e){
            return ResponseEntity.badRequest().body("Selected dates have passed");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong. Please try again");
        }
    }

    @GetMapping("action-info")
    public ResponseEntity<QuickReservationDTO> getQuickActionsDetails(@RequestParam int actionId) {
        try {
            QuickReservationDTO action = calendarService.getActionDetails(actionId);
            return ResponseEntity.ok(action);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
