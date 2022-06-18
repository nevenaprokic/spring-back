package com.booking.ISAbackend.controller;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.booking.ISAbackend.model.MyUser;

import com.booking.ISAbackend.dto.JwtAuthenticationRequest;
import com.booking.ISAbackend.dto.UserTokenState;
import com.booking.ISAbackend.security.TokenUtils;
import com.booking.ISAbackend.service.UserService;

@RestController
@CrossOrigin(origins = "https://isa-react-front.herokuapp.com/", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

	protected final Log LOGGER = LogFactory.getLog(getClass());

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<UserTokenState> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) throws Exception{

		// Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
		// AuthenticationException
		try{
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getEmail(), authenticationRequest.getPassword()));

			// Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni com.booking.ISAbackend.security
			// kontekst
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// Kreiraj token za tog korisnika
			MyUser user = (MyUser) authentication.getPrincipal();

			String jwt = tokenUtils.generateToken(user);
			int expiresIn = tokenUtils.getExpiredIn();

			// Vrati token kao odgovor na uspesnu autentifikaciju
			return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
		}catch(Exception e){
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
}
