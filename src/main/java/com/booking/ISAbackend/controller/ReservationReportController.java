package com.booking.ISAbackend.controller;

import com.booking.ISAbackend.dto.NewReservationReportDTO;
import com.booking.ISAbackend.dto.OfferForReportDTO;
import com.booking.ISAbackend.dto.ReservationReportAdminDTO;
import com.booking.ISAbackend.service.ReservationReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reservation-report")
public class ReservationReportController {

    @Autowired
    private ReservationReportService reservationReportService;

    @GetMapping("get-all-by-cottage-owner")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<List<Integer>> getReservationReportCottageOwner(@RequestParam String ownerEmail, @RequestParam String role){
        try{

            List<Integer> reservationsWithNoReport = reservationReportService.getReservationReportCottageOwner(ownerEmail);
            return ResponseEntity.ok().body(reservationsWithNoReport);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("get-all-by-ship-owner")
    @PreAuthorize("hasAuthority('SHIP_OWNER')")
    public ResponseEntity<List<Integer>> getReservationReportShipOwner(@RequestParam String ownerEmail, @RequestParam String role){
        try{
            List<Integer> reservationsWithNoReport = reservationReportService.getReservationReportShipOwner(ownerEmail);
            return ResponseEntity.ok().body(reservationsWithNoReport);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("add")
    @PreAuthorize("hasAnyAuthority('COTTAGE_OWNER','INSTRUCTOR','SHIP_OWNER')")
    public ResponseEntity<String> addReservationReport(@RequestBody NewReservationReportDTO dto){
        try{
            reservationReportService.addReservationReport(dto);
            return ResponseEntity.ok("Reservation report was successful!");
        }catch(Exception ex){
            return ResponseEntity.status(400).body("Something went wrong. Try again.");
        }
    }

    @GetMapping("get-report-income-statement-cottage")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<List<OfferForReportDTO>> getReportIncomeStatementCottage(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String email){
        try{
            List<OfferForReportDTO> dto = reservationReportService.getReportIncomeStatementCottage(startDate,endDate,email);
            return ResponseEntity.ok(dto);
        }catch(Exception ex){
            return ResponseEntity.status(400).body(null);
        }
    }
    @GetMapping("get-report-income-statement-ship")
    @PreAuthorize("hasAuthority('SHIP_OWNER')")
    public ResponseEntity<List<OfferForReportDTO>> getReportIncomeStatementShip(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String email){
        try{
            List<OfferForReportDTO> dto = reservationReportService.getReportIncomeStatementShip(startDate,endDate,email);
            return ResponseEntity.ok(dto);
        }catch(Exception ex){
            return ResponseEntity.status(400).body(null);
        }
    }
    @GetMapping("get-report-income-statement-adventure")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<List<OfferForReportDTO>> getReportIncomeStatementAdventure(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String email){
        try{
            List<OfferForReportDTO> dto = reservationReportService.getReportIncomeStatementAdventure(startDate,endDate,email);
            return ResponseEntity.ok(dto);
        }catch(Exception ex){
            return ResponseEntity.status(400).body(null);
        }
    }

    //all-by-instructor
    @GetMapping("all-by-instructor/{email}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<List<Integer>> getReservationReportInstructor(@PathVariable("email") String email){
        try{
            List<Integer> reservationsWithNoReport = reservationReportService.getNotReportedReservationsInstructor(email);
            return ResponseEntity.ok().body(reservationsWithNoReport);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("all-not-reviewed")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ReservationReportAdminDTO>> getAllNotReviewedReports(){
        try{
            List<ReservationReportAdminDTO> reports = reservationReportService.getAllNotReviewedWIthPenaltyOption();
            return new ResponseEntity<>(reports, reports.size() != 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("add-penalty/{reportId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addPenaltyToClient(@PathVariable Integer reportId){
        try{
            reservationReportService.addPenaltyToClient(reportId);
            return ResponseEntity.ok().body("Successfully added penalty to client");
        }catch(Exception e){
            return  ResponseEntity.status(400).body("Something went wrong. Please try again");
        }
    }

    @PutMapping("reject-penalty/{reportId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> rejectPenaltyToClient(@PathVariable Integer reportId){
        try{
            reservationReportService.rejectPenaltyOption(reportId);
            return ResponseEntity.ok().body("Successfully rejected penalty option to client");
        }catch(Exception e){
            e.printStackTrace();
            return  ResponseEntity.status(400).body("Something went wrong. Please try again");
        }
    }

}
