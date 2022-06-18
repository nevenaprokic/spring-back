package com.booking.ISAbackend.controller;

import com.booking.ISAbackend.dto.MarkDTO;
import com.booking.ISAbackend.service.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("mark")
public class MarkController {
    @Autowired
    private MarkService markService;

    @GetMapping("get-all-cottage")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<Double> getMarkByCottageOwnerEmail(@RequestParam String email){
        return ResponseEntity.ok(markService.getMarkByCottageOwnerEmail(email));
    }

    @GetMapping("get-all-ship")
    @PreAuthorize("hasAuthority('SHIP_OWNER')")
    public ResponseEntity<Double> getMarkShipOwnerEmail(@RequestParam String email){
        return ResponseEntity.ok(markService.getMarkByShipOwnerEmail(email));
    }

    @GetMapping("get-all-adventure")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<Double> getMarkInstructorEmail(@RequestParam String email){
        return ResponseEntity.ok(markService.getMarkByInstructorEmail(email));
    }

    @GetMapping("all-unchecked")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<MarkDTO>> getAllUncheckedMarks(){
        try{
            List<MarkDTO> marks = markService.getAllUncheckesMarks();
            return new ResponseEntity<>(marks,  HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/accept/{markId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> acceptMark(@PathVariable int markId){
        try{
            markService.acceptMark(markId);
            return ResponseEntity.ok("Successfully accepted mark");
        }catch (ObjectOptimisticLockingFailureException ex){
            return ResponseEntity.status(400).body("The other admin has just responded to this mark so you can't.");
        }catch (Exception e){
            return ResponseEntity.status(400).body("Something went wrong, please try again later");
        }
    }

    @PutMapping("/discard/{markId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> discardMark(@PathVariable int markId){
        try{
            markService.discardMark(markId);
            return ResponseEntity.ok("Successfully reject mark");
        }catch (ObjectOptimisticLockingFailureException ex){
            return ResponseEntity.status(400).body("The other admin has just responded to this mark so you can't.");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(400).body("Something went wrong, please try again later");
        }
    }

    @GetMapping("offer-marks")
    public ResponseEntity<List<MarkDTO>> getAllMarksForOffer(@RequestParam int offerId){
        try{
            return ResponseEntity.ok().body(markService.getAllMarksForOffer(offerId));
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        }
    }



}
