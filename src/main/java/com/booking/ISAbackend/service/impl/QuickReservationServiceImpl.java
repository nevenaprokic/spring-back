package com.booking.ISAbackend.service.impl;

import com.booking.ISAbackend.dto.NewQuickReservationDTO;
import com.booking.ISAbackend.dto.QuickReservationDTO;
import com.booking.ISAbackend.email.EmailSender;
import com.booking.ISAbackend.exceptions.InvalidPriceException;
import com.booking.ISAbackend.exceptions.RequiredFiledException;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.repository.OfferRepository;
import com.booking.ISAbackend.repository.QuickReservationRepository;
import com.booking.ISAbackend.service.AdditionalServiceService;
import com.booking.ISAbackend.service.QuickReservationService;
import com.booking.ISAbackend.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class QuickReservationServiceImpl implements QuickReservationService {

    @Autowired
    private QuickReservationRepository quickReservationRepository;
    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private AdditionalServiceService additionalServiceService;
    @Autowired
    private EmailSender emailSender;

    @Override
    @Transactional
    public List<QuickReservationDTO> findQuickReservationByOfferId(Integer id){
        List<QuickReservation> listOfAllQuickReservation = quickReservationRepository.findQuickReservationsByOfferId(id);

        List<QuickReservation> listOfCurrentQuickReservation = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for(QuickReservation qr:listOfAllQuickReservation){
            if((today.compareTo(qr.getStartDateAction())>0)&&(today.compareTo(qr.getEndDateAction())<0)){
                listOfCurrentQuickReservation.add(qr);
            }
        }
        List<QuickReservationDTO> dto = new ArrayList<>();
        for(QuickReservation res: listOfCurrentQuickReservation){
            QuickReservationDTO quickActionDTO = new QuickReservationDTO(res);
            dto.add(quickActionDTO);
        }
        return dto;

    }

    @Override
    public Boolean checkQuickReservationByOfferId(Integer offerId, String startDate, Integer dateNumber) {
        List<QuickReservation> quickReservations = quickReservationRepository.findQuickReservationsByOfferId(offerId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateAction = LocalDate.parse(startDate, formatter);
        LocalDate endDateAction = startDateAction.plusDays(dateNumber);
        for(QuickReservation q: quickReservations){
            if((q.getStartDateAction().compareTo(startDateAction) <= 0) && (startDateAction.compareTo(q.getEndDateAction()) <= 0))
                return false;
            if((q.getStartDateAction().compareTo(endDateAction) <= 0) && (endDateAction.compareTo(q.getEndDateAction()) <= 0))
                return false;
        }
        return true;
    }

    @Override
    @Transactional
    public Integer addNewQuickReservation(NewQuickReservationDTO dto) throws InterruptedException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateAction = LocalDate.parse(dto.getStartDateAction(), formatter);
        LocalDate endDateAction = startDateAction.plusDays(dto.getDaysAction());
        LocalDate startDateReservation = LocalDate.parse(dto.getStartDateReservation(), formatter);
        LocalDate endDateReservation = startDateReservation.plusDays(dto.getDaysReservation());
        Offer offer = offerRepository.findOfferById(dto.getOfferId());

        offer.setNumberOfQuickReservation(offer.getNumberOfQuickReservation()+1);

        QuickReservation quickReservation = new QuickReservation(startDateReservation, endDateAction, startDateAction, endDateReservation, dto.getPrice(), dto.getPeopleNum(), false, offer);
        QuickReservation newQuickReservation = quickReservationRepository.save(quickReservation);
        offer.getQuickReservations().add(newQuickReservation);

        offerRepository.save(offer);
        sendEmail(offer, dto.getStartDateAction());
        return newQuickReservation.getId();
    }
    @Transactional
    void sendEmail(Offer offer, String date){
        for(MyUser u : offer.getSubscribedClients())
            emailSender.notifySubscribersNewQuickReservation(u.getEmail(), offer.getName(), date);
    }

    @Override
    public void addAdditionalServices(List<HashMap<String, String>> additionalServiceDTOs, Integer quickId) throws InvalidPriceException, RequiredFiledException {
        Optional<QuickReservation> quickReservation = quickReservationRepository.findById(quickId);
        if(quickReservation.isPresent() && Validator.isValidAdditionalServices(additionalServiceDTOs)){
            QuickReservation q = quickReservation.get();
            List<AdditionalService> additionalServices = additionalServiceService.convertServicesFromDTO(additionalServiceDTOs);
            q.setAdditionalServices(additionalServices);
            quickReservationRepository.save(q);
        }

    }
}
