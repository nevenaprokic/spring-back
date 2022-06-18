package com.booking.ISAbackend.controller;


import com.booking.ISAbackend.dto.ShipOwnerNewDataDTO;
import com.booking.ISAbackend.dto.ShipOwnerProfileInfoDTO;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.service.RegistrationRequestService;
import com.booking.ISAbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("ship-owner")
public class ShipOwnerController {
    @Autowired
    private UserService userService;
    @Autowired
    private RegistrationRequestService registrationRequestService;

    @GetMapping("profile-info")
    @PreAuthorize("hasAuthority('SHIP_OWNER')")
    public ResponseEntity<ShipOwnerProfileInfoDTO> getShipOwnerProfileInfo(@RequestParam String email){
        try{
            ShipOwnerProfileInfoDTO shipOwner =  userService.getShipOwnerDataByEmail(email);
            return ResponseEntity.ok(shipOwner);
        }catch  (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("change-data")
    @PreAuthorize("hasAuthority('SHIP_OWNER')")
    public ResponseEntity<String> changeShipOwnerData(@RequestBody ShipOwnerNewDataDTO newData){
        try{
            userService.changeShipOwnerData(newData);
            return ResponseEntity.ok("Successfully changed your data");
        } catch (OnlyLettersAndSpacesException | InvalidPhoneNumberException | InvalidAddressException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("send-delete-request")
    @PreAuthorize("hasAuthority('SHIP_OWNER')")
    public ResponseEntity<String> sendDeleteRequestShipOwner(@RequestParam String email, @RequestBody HashMap<String, String> data) {
        try{
            if(userService.sendDeleteRequestShipOwner(email, data.get("reason")))
                return ResponseEntity.ok("Successfully created request to delete the order.");
            else
                return ResponseEntity.status(400).body("Unable to send request to delete the order, user's offers have reservations.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Unable to send request to delete the order.");
        }
    }

    @DeleteMapping("delete-ship-owner/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteShipOwner(@PathVariable("userId") int userId){
        try{
            userService.deleteShipOwner(userId);
            return ResponseEntity.ok("Successfully deleted account");
        } catch (AccountDeletionException | OfferNotFoundException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(400).body("Something went wrong please try again");
        }
    }
}
