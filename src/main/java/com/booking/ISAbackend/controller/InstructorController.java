package com.booking.ISAbackend.controller;


import com.booking.ISAbackend.dto.InstructorNewDataDTO;
import com.booking.ISAbackend.dto.InstructorProfileData;
import com.booking.ISAbackend.dto.OfferSearchParamsDTO;
import com.booking.ISAbackend.dto.ShipDTO;
import com.booking.ISAbackend.exceptions.InvalidAddressException;
import com.booking.ISAbackend.exceptions.InvalidPhoneNumberException;
import com.booking.ISAbackend.exceptions.OnlyLettersAndSpacesException;
import com.booking.ISAbackend.model.Instructor;
import com.booking.ISAbackend.model.Ship;
import com.booking.ISAbackend.service.CottageService;
import com.booking.ISAbackend.service.InstructorService;
import com.booking.ISAbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("instructor")
public class InstructorController {

    @Autowired
    private UserService userService;

    @Autowired
    private InstructorService instructorService;

    @GetMapping("search")
    public ResponseEntity<List<InstructorProfileData>> searchInstructors(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String address, @RequestParam String phoneNumber){
        try{
            List<InstructorProfileData> instructors  = instructorService.searchInstructors(firstName, lastName, address, phoneNumber);
            return ResponseEntity.ok(instructors);
        }catch  (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("search-client")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<InstructorProfileData>> searchInstructorsClient(@RequestBody OfferSearchParamsDTO params){
        try{
            List<InstructorProfileData> instructors  = instructorService.searchInstructorsClient(params);
            return ResponseEntity.ok(instructors);
        }catch  (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get-all")
    public ResponseEntity<List<InstructorProfileData>> getAll(){
        try{
            List<InstructorProfileData> instructors  = instructorService.findAll();
            return ResponseEntity.ok(instructors);
        }catch  (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("delete-profile-request")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<String> sendDeleteRequestInstructor(@RequestParam String email, @RequestBody HashMap<String, String> data) {
        try{
            if(instructorService.sendDeleteRequest(email, data.get("reason")))
                return ResponseEntity.ok("Successfully created request to delete the order.");
            else
                return ResponseEntity.status(400).body("Unable to send request to delete the order, user's offers have reservations.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Unable to send request to delete the order.");
        }
    }

    @PostMapping("change-data")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<String> changeInstructorData(@RequestBody InstructorNewDataDTO newData){
        try{
            userService.changeInstrctorData(newData);
            return ResponseEntity.ok("Successfully changed your data");
        } catch (OnlyLettersAndSpacesException | InvalidPhoneNumberException | InvalidAddressException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (Exception  e) {
            return ResponseEntity.status(400).body("Something went wrong, please try again.");
        }
    }

    @GetMapping("profile-info")
    public ResponseEntity<InstructorProfileData> getInstructorProfileInfo(@RequestParam String email){
        //odraditi autentifikaciju i autorizaciju
        InstructorProfileData instructor =  userService.getInstructorDataByEmail(email);
        if(instructor != null){
            return ResponseEntity.ok(instructor);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
