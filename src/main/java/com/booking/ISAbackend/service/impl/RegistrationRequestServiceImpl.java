package com.booking.ISAbackend.service.impl;

import com.booking.ISAbackend.dto.OwnerRegistrationRequestDTO;
import com.booking.ISAbackend.email.EmailSender;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.repository.*;
import com.booking.ISAbackend.service.RegistrationRequestService;
import com.booking.ISAbackend.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class RegistrationRequestServiceImpl implements RegistrationRequestService {

    @Autowired
    RegistrationRequestRepository registrationRequestRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    InstructorRepository instructorRepository;
    @Autowired
    CottageOwnerRepository cottageOwnerRepository;
    @Autowired
    ShipOwnerRepository shipOwnerRepository;
    @Autowired
    EmailSender emailSender;
    @Autowired
    OwnerCategoryRepository ownerCategoryRepository;


    @Override
    public boolean save(OwnerRegistrationRequestDTO request) throws InvalidAddressException, InvalidEmail, InvalidCredential, InvalidPhoneNumber, InvalidPasswordException {
        MyUser myUser =  userService.findByEmail(request.getEmail());
        if(validateRequest(request)) {
            if(myUser == null) {
                Address address = new Address(request.getStreet(), request.getCity(), request.getState());
                LocalDate sendingTime = LocalDate.now();
                RegistrationRequest registrationRequest = new RegistrationRequest(request.getExplanation(), request.getType(), request.getFirstName(), request.getLastName(), request.getPassword(), request.getPhoneNumber(), request.getEmail(), false, sendingTime,  address);
                addressRepository.save(address);
                registrationRequestRepository.save(registrationRequest);
                return true;
            }
        }
        return false;

    }

    @Override
    @Transactional
    public List<OwnerRegistrationRequestDTO> getAll() {
        List<RegistrationRequest> allRegistrationRequests = registrationRequestRepository.findAll();
        List<OwnerRegistrationRequestDTO> requestDTOS = new ArrayList<OwnerRegistrationRequestDTO>();
        for(RegistrationRequest request : allRegistrationRequests){
            if(!request.getDeleted()) {
                Address a = request.getAddress();
                OwnerRegistrationRequestDTO registrationRequest = new OwnerRegistrationRequestDTO(request.getDescription(),
                        request.getPersonType(),
                        request.getFirstName(),
                        request.getLastName(),
                        request.getPhoneNumber(),
                        request.getEmail(),
                        a.getStreet(),
                        a.getCity(),
                        a.getState(),
                        request.getId(),
                        formatDate(request.getSendingTime()));
                requestDTOS.add(registrationRequest);
            }
        }
//        Comparator<OwnerRegistrationRequestDTO> comparator = (OwnerRegistrationRequestDTO m1, OwnerRegistrationRequestDTO m2) -> revertToDate(m1.getSendingTime()).compareTo(revertToDate(m2.getSendingTime()));
//        Collections.sort(requestDTOS, comparator);
        return requestDTOS;
    }

    @Override
    @Transactional
    public void acceptRegistrationRequest(int id) throws InterruptedException {
        //obrisati zahtev logicki, sacuvati u bazi, aktivirati nalog i sacuvati u bazi, slanje mejla
        RegistrationRequest request = registrationRequestRepository.getById(id);

        if(request.getPersonType().equals(UserType.INSTRUCTOR.toString())){ //
            createInstructorAccount(request);
        }
        else if(request.getPersonType().equals(UserType.COTTAGE_OWNER.toString())){ //
            createCottageOwnerAccount(request);
        }
        else if (request.getPersonType().equals(UserType.SHIP_OWNER.toString())){ //
            createShipOwnerAccount(request);
        }

        request.setDeleted(true);
        registrationRequestRepository.save(request);


    }

    @Transactional
    public void createInstructorAccount(RegistrationRequest request) throws InterruptedException {
        Instructor instructor = new Instructor();
        instructor.setEmail(request.getEmail());
        instructor.setFirstName(request.getFirstName());
        instructor.setLastName(request.getLastName());
        instructor.setPhoneNumber(request.getPhoneNumber());
        instructor.setAddress(request.getAddress());
        instructor.setPassword(request.getPassword());
        instructor.setDeleted(false);
        instructor.setEmailVerified(true);
        instructor.setRole(roleRepository.findByName("INSTRUCTOR").get(0));
        instructor.setBiography("");
        instructor.setPoints(0);
        instructorRepository.save(instructor);
        emailSender.sendConfirmationRegistrationRequest(request.getEmail());
    }

    @Transactional
    public void createCottageOwnerAccount(RegistrationRequest request) throws InterruptedException {
        CottageOwner cottageOwner = new CottageOwner();
        cottageOwner.setEmail(request.getEmail());
        cottageOwner.setFirstName(request.getFirstName());
        cottageOwner.setLastName(request.getLastName());
        cottageOwner.setPhoneNumber(request.getPhoneNumber());
        cottageOwner.setAddress(request.getAddress());
        cottageOwner.setPassword(request.getPassword());
        cottageOwner.setDeleted(false);
        cottageOwner.setEmailVerified(true);
        cottageOwner.setRole(roleRepository.findByName("COTTAGE_OWNER").get(0));
        cottageOwner.setPoints(0);
        cottageOwnerRepository.save(cottageOwner);
        emailSender.sendConfirmationRegistrationRequest(request.getEmail());
    }

    @Transactional
    public void createShipOwnerAccount(RegistrationRequest request) throws InterruptedException {
        ShipOwner shipOwner = new ShipOwner();
        shipOwner.setEmail(request.getEmail());
        shipOwner.setFirstName(request.getFirstName());
        shipOwner.setLastName(request.getLastName());
        shipOwner.setPhoneNumber(request.getPhoneNumber());
        shipOwner.setAddress(request.getAddress());
        shipOwner.setPassword(request.getPassword());
        shipOwner.setDeleted(false);
        shipOwner.setEmailVerified(true);
        shipOwner.setRole(roleRepository.findByName("SHIP_OWNER").get(0));
        shipOwner.setPoints(0);
        shipOwnerRepository.save(shipOwner);
        emailSender.sendConfirmationRegistrationRequest(request.getEmail());
    }

    @Override
    @Transactional
    public void discardRegistrationRequest(int id, String message) throws InterruptedException {
        //obrisati adresu iz baze, obrisati zahtev logicki i poslati mejl sa porukom
        RegistrationRequest request = registrationRequestRepository.getById(id);
        String email = request.getEmail();
        request.setDeleted(true);
        registrationRequestRepository.save(request);
        emailSender.sendRejectionRegistrationRequest(email, message);
    }


    private boolean validateRequest(OwnerRegistrationRequestDTO request) throws InvalidAddressException, InvalidPasswordException, InvalidEmail, InvalidCredential, InvalidPhoneNumber {
        boolean validationResult = Validator.isValidAdress(request.getStreet(), request.getCity(), request.getState())
                && Validator.isMachPassword(request.getPassword(), request.getConfirmPassword())
                && Validator.isValidEmail(request.getEmail())
                && Validator.isValidCredentials(request.getFirstName())
                && Validator.isValidCredentials(request.getLastName())
                && Validator.isValidPhoneNumber(request.getPhoneNumber())
                && (!request.getExplanation().isEmpty());
        return validationResult;
    }

    private String formatDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return formatter.format(date);
    }

    private LocalDate revertToDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return LocalDate.parse(date, formatter);
    }

}
