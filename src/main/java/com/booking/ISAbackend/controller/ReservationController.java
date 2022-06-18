package com.booking.ISAbackend.controller;

import com.booking.ISAbackend.dto.*;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.AdditionalService;
import com.booking.ISAbackend.model.Reservation;
import com.booking.ISAbackend.service.ReservationService;
import com.sun.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OptimisticLockException;
import javax.swing.text.StyledEditorKit;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("make")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<String> makeReservation(@RequestBody ReservationParamsDTO params){
        try {
            reservationService.makeReservation(params);
            return ResponseEntity.ok("Reservation was successful!");
        }catch (ObjectOptimisticLockingFailureException ex){
            return ResponseEntity.status(400).body("Someone has made reservation before you. Please choose another period.");
        }catch (OfferNotAvailableException ex){
            return ResponseEntity.status(400).body(ex.getMessage());
        }catch (NotAllowedToMakeReservationException ex){
            return ResponseEntity.status(400).body(ex.getMessage());
        }catch (PreviouslyCanceledReservationException ex){
            return ResponseEntity.status(400).body(ex.getMessage());
        }catch (ClientNotAvailableException ex){
            return ResponseEntity.status(400).body(ex.getMessage());
        }catch(Exception ex){
            return ResponseEntity.status(400).body("Something went wrong. Try again.");
        }
    }

    @PostMapping("confirm-email")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<String> sendCofirmationForReservation(@RequestBody ReservationParamsDTO params){
        try{
            reservationService.sendEmail(params);
            return ResponseEntity.ok("Reservation was successful!");
        }catch(Exception ex){
            return ResponseEntity.status(400).body("Something went wrong. Try again.");
        }
    }

    @GetMapping("get-all-by-cottage-owner")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<List<ReservationDTO>> getReservation(@RequestParam String ownerId,@RequestParam String role){
        try{
            List<ReservationDTO> reservations = reservationService.getAllReservation(ownerId, role);
            return ResponseEntity.ok().body(reservations);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("get-all-by-ship-owner")
    @PreAuthorize("hasAuthority('SHIP_OWNER')")
    public ResponseEntity<List<ReservationDTO>> getReservationByShipOwner(@RequestParam String ownerId,@RequestParam String role){
        try{
            List<ReservationDTO> reservations = reservationService.getAllReservation(ownerId, role);
            return ResponseEntity.ok().body(reservations);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("get-cottage-reservations-by-client")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<ReservationDTO>> getPastCottageReservationsClient(@RequestParam String email) {
        try {
            List<ReservationDTO> reservations = reservationService.getPastCottageReservationsByClient(email);
            return ResponseEntity.ok().body(reservations);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("available-offer")
    @PreAuthorize("hasAnyAuthority('COTTAGE_OWNER','INSTRUCTOR','SHIP_OWNER')")
    public ResponseEntity<Boolean> isAvailableOffer(@RequestParam String offerId, @RequestParam String startDate, @RequestParam String dayNum){
        try{
            Boolean check = reservationService.isAvailableOffer(Integer.parseInt(offerId), startDate, Integer.parseInt(dayNum));
            return ResponseEntity.ok().body(check);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("make-by-owner")
    @PreAuthorize("hasAnyAuthority('COTTAGE_OWNER','INSTRUCTOR','SHIP_OWNER')")
    public ResponseEntity<String> makeReservationOwner(@RequestBody NewReservationDTO dto){
        try {
            Integer reservationId = reservationService.makeReservationOwner(dto);
            return ResponseEntity.ok(reservationId.toString());
        }catch (ObjectOptimisticLockingFailureException ex){
            return ResponseEntity.status(400).body("Someone has made reservation at the same time. Please try again.");
        }catch(Exception ex){
            return ResponseEntity.status(400).body(null);
        }

    }

    @GetMapping("get-ship-reservations-by-client")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<ReservationDTO>> getPastShipReservationsClient(@RequestParam String email){
        try{
            List<ReservationDTO> reservations = reservationService.getPastShipReservationsByClient(email);
            return ResponseEntity.ok().body(reservations);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get-adventure-reservations-by-client")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<ReservationDTO>> getPastAdventureReservationsClient(@RequestParam String email){
        try{
            List<ReservationDTO> reservations = reservationService.getPastAdventureReservationsByClient(email);
            return ResponseEntity.ok().body(reservations);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("get-upcoming-cottage-reservations-by-client")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<ReservationDTO>> getUpcomingCottageReservationsClient(@RequestParam String email) {
        try {
            List<ReservationDTO> reservations = reservationService.getUpcomingCottageReservationsByClient(email);
            return ResponseEntity.ok().body(reservations);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get-upcoming-ship-reservations-by-client")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<ReservationDTO>> getUpcomingShipReservationsClient(@RequestParam String email){
        try{
            List<ReservationDTO> reservations = reservationService.getUpcomingShipReservationsByClient(email);
            return ResponseEntity.ok().body(reservations);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get-upcoming-adventure-reservations-by-client")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<ReservationDTO>> getUpcomingAdventureReservationsClient(@RequestParam String email){
        try{
            List<ReservationDTO> reservations = reservationService.getUpcomingAdventureReservationsByClient(email);
            return ResponseEntity.ok().body(reservations);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<String> cancelReservation(@PathVariable Integer id) {
        try {
            reservationService.cancelReservation(id);
            return new ResponseEntity<>("Reservation successfully canceled!", HttpStatus.OK);

        } catch (CancellingReservationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get-attendance-report-yearly-cottage")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<List<AttendanceReportDTO>> getAttendanceReportYearlyCottage(@RequestParam String email, @RequestParam String date){
        try{
            List<AttendanceReportDTO> report = reservationService.getAttendanceReportYearlyCottage(email, date);
            return ResponseEntity.ok().body(report);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("get-attendance-report-monthly-cottage")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<List<AttendanceReportDTO>> getAttendanceReportMonthlyCottage(@RequestParam String email, @RequestParam String date){
        try{
          List<AttendanceReportDTO> report = reservationService.getAttendanceReportMonthlyCottage(email, date);
          return ResponseEntity.ok().body(report);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get-attendance-report-weekly-cottage")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<List<AttendanceReportDTO>> getAttendanceReportWeeklyCottage(@RequestParam String email, @RequestParam String date){
        try{
          List<AttendanceReportDTO> report = reservationService.getAttendanceReportWeeklyCottage(email, date);
          return ResponseEntity.ok().body(report);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("get-attendance-report-yearly-ship")
    @PreAuthorize("hasAuthority('SHIP_OWNER')")
    public ResponseEntity<List<AttendanceReportDTO>> getAttendanceReportYearlyShip(@RequestParam String email, @RequestParam String date){
        try{
            List<AttendanceReportDTO> report = reservationService.getAttendanceReportYearlyShip(email, date);
            return ResponseEntity.ok().body(report);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("get-attendance-report-monthly-ship")
    @PreAuthorize("hasAuthority('SHIP_OWNER')")
    public ResponseEntity<List<AttendanceReportDTO>> getAttendanceReportMonthlyShip(@RequestParam String email, @RequestParam String date){
        try{
            List<AttendanceReportDTO> report = reservationService.getAttendanceReportMonthlyShip(email, date);
            return ResponseEntity.ok().body(report);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get-attendance-report-weekly-ship")
    @PreAuthorize("hasAuthority('SHIP_OWNER')")
    public ResponseEntity<List<AttendanceReportDTO>> getAttendanceReportWeeklyShip(@RequestParam String email, @RequestParam String date){
        try{
            List<AttendanceReportDTO> report = reservationService.getAttendanceReportWeeklyShip(email, date);
            return ResponseEntity.ok().body(report);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("get-attendance-report-yearly-adventure")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<List<AttendanceReportDTO>> getAttendanceReportYearlyAdventure(@RequestParam String email, @RequestParam String date){
        try{
            List<AttendanceReportDTO> report = reservationService.getAttendanceReportYearlyAdventure(email, date);
            return ResponseEntity.ok().body(report);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("get-attendance-report-monthly-adventure")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<List<AttendanceReportDTO>> getAttendanceReportMonthlyAdventure(@RequestParam String email, @RequestParam String date){
        try{
            List<AttendanceReportDTO> report = reservationService.getAttendanceReportMonthlyAdventure(email, date);
            return ResponseEntity.ok().body(report);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get-attendance-report-weekly-adventure")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<List<AttendanceReportDTO>> getAttendanceReportWeeklyAdventure(@RequestParam String email, @RequestParam String date){
        try{
            List<AttendanceReportDTO> report = reservationService.getAttendanceReportWeeklyAdventure(email, date);
            return ResponseEntity.ok().body(report);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("instructor-history/{email}/{role}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<List<ReservationDTO>> getReservationByInstructor(@PathVariable("email") String email, @PathVariable("role") String role){
        try{
            List<ReservationDTO> reservations = reservationService.getAllReservation(email, role);
            return ResponseEntity.ok().body(reservations);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

}
