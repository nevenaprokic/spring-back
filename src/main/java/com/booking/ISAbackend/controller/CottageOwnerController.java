package com.booking.ISAbackend.controller;


import com.booking.ISAbackend.dto.CottageOwnerNewDataDTO;
import com.booking.ISAbackend.dto.CottageOwnerProfileInfoDTO;
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
@RequestMapping("cottage-owner")
public class CottageOwnerController {

    @Autowired
    private UserService userService;
    @Autowired
    private RegistrationRequestService registrationRequestService;

    @GetMapping("profile-info")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<CottageOwnerProfileInfoDTO> getCottageOwnerProfileInfo(@RequestParam String email){
        try{
            CottageOwnerProfileInfoDTO cottageOwner =  userService.getCottageOwnerDataByEmail(email);
            return ResponseEntity.ok(cottageOwner);
        }catch  (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("change-data")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<String> changeCottageOwnerData(@RequestBody CottageOwnerNewDataDTO newData){
        try{
            userService.changeCottageOwnerData(newData);
            return ResponseEntity.ok("Successfully changed your data");
        } catch (OnlyLettersAndSpacesException | InvalidPhoneNumberException | InvalidAddressException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("send-delete-request")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<String> sendDeleteRequestCottageOwner(@RequestParam String email, @RequestBody HashMap<String, String> data) {
        try{
            if(userService.sendDeleteRequestCottageOwner(email, data.get("reason")))
                return ResponseEntity.ok("Successfully created request to delete the order.");
            else
                return ResponseEntity.status(400).body("Unable to send request to delete the order, user's offers have reservations.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Unable to send request to delete the order.");
        }
    }

    @DeleteMapping("delete-cottage-owner/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteCottageOwner(@PathVariable("userId") int userId){
        try{
            userService.deleteCottageOwner(userId);
            return ResponseEntity.ok("Successfully deleted account");
        } catch (AccountDeletionException | OfferNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(400).body("Something went wrong please try again");
        }
    }

}
