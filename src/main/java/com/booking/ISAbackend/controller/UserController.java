package com.booking.ISAbackend.controller;

import com.booking.ISAbackend.dto.*;
import com.booking.ISAbackend.exceptions.InvalidAddressException;
import com.booking.ISAbackend.exceptions.InvalidPasswordException;
import com.booking.ISAbackend.exceptions.InvalidPhoneNumberException;
import com.booking.ISAbackend.exceptions.OnlyLettersAndSpacesException;
import com.booking.ISAbackend.model.Address;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.service.RegistrationRequestService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.booking.ISAbackend.service.UserService;

import java.util.HashMap;

@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private RegistrationRequestService registrationRequestService;

	@PostMapping("registration-owner")
	public ResponseEntity<String> sendOwnerRegistration(@RequestBody OwnerRegistrationRequestDTO request){
		try {
			boolean userIsExists = registrationRequestService.save(request);
			if(userIsExists)
				return ResponseEntity.ok("You have successfully submitted your registration request!");
			else
				return new ResponseEntity<>("User  with this email alredy exists!", HttpStatus.BAD_REQUEST);
		}catch (Exception e){
			return new ResponseEntity<>("You haven't successfully submitted your registration request!", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("update-password/{email}")
	@PreAuthorize("hasAnyAuthority('COTTAGE_OWNER','INSTRUCTOR','SHIP_OWNER', 'ADMIN', 'CLIENT')")
	public ResponseEntity<String> updatePassword(@PathVariable String email, @RequestBody HashMap<String, String> data) {
		try {
			userService.isOldPasswordCorrect(email, data);
			return ResponseEntity.ok("Successfully changed password.");
		} catch (InvalidPasswordException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

}
