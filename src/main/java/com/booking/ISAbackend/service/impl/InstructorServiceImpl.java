package com.booking.ISAbackend.service.impl;

import com.booking.ISAbackend.dto.AdventureDTO;
import com.booking.ISAbackend.dto.InstructorProfileData;
import com.booking.ISAbackend.dto.OfferSearchParamsDTO;
import com.booking.ISAbackend.exceptions.InvalidPhoneNumberException;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.repository.DeleteRequestRepository;
import com.booking.ISAbackend.repository.InstructorRepository;
import com.booking.ISAbackend.repository.ReservationRepository;
import com.booking.ISAbackend.repository.UserRepository;
import com.booking.ISAbackend.service.AdventureService;
import com.booking.ISAbackend.service.InstructorService;
import com.booking.ISAbackend.service.OwnerCategoryService;
import com.booking.ISAbackend.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InstructorServiceImpl implements InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private AdventureService adventureService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private DeleteRequestRepository deleteRequestRepository;

    @Autowired
    private OwnerCategoryService ownerCategoryService;

    @Override
    @Transactional
    public List<InstructorProfileData> searchInstructors(String firstName, String lastName, String address, String phoneNumber) throws InvalidPhoneNumberException, IOException {
        List<InstructorProfileData> retList = new ArrayList<>();
        if(phoneNumber.equals("") || Validator.phoneNumberValidation(phoneNumber)){
            List<Instructor> instructors = instructorRepository.searchInstructors(firstName, lastName, address, phoneNumber);
            makeInstructorDTOs(retList, instructors);
            return retList;
        }
        return retList;
    }

    @Override
    @Transactional
    @Cacheable("instructors")
    public List<InstructorProfileData> findAll() throws IOException {
        List<InstructorProfileData> retList = new ArrayList<>();
        List<Instructor> instructors = instructorRepository.findAllActiveInstructors();
        makeInstructorDTOs(retList, instructors);
        return retList;
    }

    @Override
    public boolean sendDeleteRequest(String email, String reason) {
        MyUser user = userRepository.findByEmail(email);
        List<Reservation> listOfReservation = reservationRepository.findFutureByInstructorEmail(email, LocalDate.now());
        LocalDate today = LocalDate.now();
        for(Reservation r:listOfReservation){
            if((today.compareTo(r.getEndDate())<0)){
                return false;
            }
        }
        DeleteRequest deleteRequest = new DeleteRequest(reason, user);
        deleteRequestRepository.save(deleteRequest);
        return true;
    }

    @Override
    public List<InstructorProfileData> searchInstructorsClient(OfferSearchParamsDTO params) throws IOException {
        List<Instructor> instructors = instructorRepository.searchInstructorsClient(params.getFirstName(), params.getLastName(), params.getAddress());
        List<Adventure> nonAvailable = adventureService.nonAvailableAdventures(params.getDate());
        List<InstructorProfileData> availableInstructors = new ArrayList<>();

        for(Instructor i : instructors){
            List<Adventure> adv = adventureService.getInstructorsAdventuresById(i.getId());
            OwnerCategory category = ownerCategoryService.findByReservationpoints(i.getPoints()).get(0);
            List<Adventure> availableAdventures = adv.stream()
                    .filter(element -> nonAvailable.contains(element))
                    .collect(Collectors.toList());
            if(availableAdventures.size() == 0){
                List<AdventureDTO> adventures = adventureService.getInstructorAdventures(i.getEmail());
                InstructorProfileData dto = new InstructorProfileData(i, category);
                dto.setMark(calculateInstructorsRating(adventures));
                dto.setAdventures(adventures);
                availableInstructors.add(dto);
            }
        }
        return availableInstructors;
    }

    private void makeInstructorDTOs(List<InstructorProfileData> retList, List<Instructor> instructors) throws IOException {
        for (Instructor i : instructors) {
            List<AdventureDTO> adventures = adventureService.getInstructorAdventures(i.getEmail());
            OwnerCategory category = ownerCategoryService.findByReservationpoints(i.getPoints()).get(0);
            InstructorProfileData dto = new InstructorProfileData(i, category);
            dto.setAdventures(adventures);
            dto.setMark(calculateInstructorsRating(adventures));
            retList.add(dto);
        }
    }

    private double calculateInstructorsRating(List<AdventureDTO> adventures){
        double total = 0;
        int counter = 0;
        for(AdventureDTO a : adventures){
            total += a.getMark();
            counter += 1;
        }
        if(counter == 0) return 0;
        return total / counter;
    }

}
