package com.booking.ISAbackend.controller;

import com.booking.ISAbackend.dto.OwnerRegistrationRequestDTO;
import com.booking.ISAbackend.repository.RegistrationRequestRepository;
import com.booking.ISAbackend.service.RegistrationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("registration-request")
public class RegistrationRequestController {

    @Autowired
    private RegistrationRequestService registrationRequests;

    @GetMapping("get-all")
    public ResponseEntity<List<OwnerRegistrationRequestDTO>> getRegistrationRequests(){
        try{
            List<OwnerRegistrationRequestDTO> requestDTOS = registrationRequests.getAll();
            return ResponseEntity.ok(requestDTOS);
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("accept")
    public ResponseEntity<List<OwnerRegistrationRequestDTO>> acceptRegistrationRequest(@RequestBody int requestId){
        try{
            registrationRequests.acceptRegistrationRequest(requestId);
            List<OwnerRegistrationRequestDTO> updateRequestsList = registrationRequests.getAll();
            return ResponseEntity.ok(updateRequestsList);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("discard")
    public ResponseEntity<List<OwnerRegistrationRequestDTO>> discardRegistrationRequest(@RequestParam int requestId, @RequestBody  String message){
        try{
            registrationRequests.discardRegistrationRequest(requestId, message);
            List<OwnerRegistrationRequestDTO> updateRequestsList = registrationRequests.getAll();
            return ResponseEntity.ok(updateRequestsList);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
