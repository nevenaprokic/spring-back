package com.booking.ISAbackend.service.impl;

import com.booking.ISAbackend.dto.AdditionalServiceDTO;
import com.booking.ISAbackend.dto.AddressDTO;
import com.booking.ISAbackend.exceptions.OfferNotFoundException;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.repository.OfferRepository;
import com.booking.ISAbackend.repository.QuickReservationRepository;
import com.booking.ISAbackend.repository.ReservationRepository;
import com.booking.ISAbackend.service.AdditionalServiceService;
import com.booking.ISAbackend.service.ClientService;
import com.booking.ISAbackend.service.OfferService;
import com.booking.ISAbackend.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {
    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private QuickReservationRepository quickReservationRepository;
    @Autowired
    private PhotoService photoService;
    @Autowired
    private AdditionalServiceService additionalServiceService;
    @Autowired
    private ClientService clientService;

    @Override
    @Transactional
    public AddressDTO findAddressByOfferId(Integer id){
        Offer offer = offerRepository.findOfferById(id);
        Address address = offer.getAddress();
        AddressDTO addressDTO = new AddressDTO(address);
        return addressDTO;
    }

    @Override
    @Transactional
    public List<AdditionalServiceDTO> findAdditionalServiceByOffer(Integer id) {
        Offer offer = offerRepository.findOfferById(id);
        List<AdditionalService> additionalServices = offer.getAdditionalServices();
        List<AdditionalServiceDTO> additionalServiceDTO = new ArrayList<>();
        for(AdditionalService ad: additionalServices){
            AdditionalServiceDTO a = new AdditionalServiceDTO(ad);
            additionalServiceDTO.add(a);
        }
        return  additionalServiceDTO;
    }

    @Override
    @Transactional
    public void delete(Integer offerId) throws OfferNotFoundException {
        Offer offer = offerRepository.findOfferById(offerId);
        if (offer == null)
            throw new OfferNotFoundException("Offer not found");
        if(offer.getSubscribedClients().size()!= 0)
            clientService.removeSubscribedClients(offer.getSubscribedClients());
        if(offer.getPhotos().size() != 0)
            photoService.removeOldPhotos(offer.getPhotos());
        if(offer.getAdditionalServices().size() != 0)
            additionalServiceService.removeAdditionalServices(offer.getAdditionalServices());
        if(offer.getQuickReservations().size() != 0)
            quickReservationRepository.deleteByOfferId(offerId);
        if(offer.getReservations().size() != 0)
            reservationRepository.deleteByOfferId(offerId);
        offerRepository.updateDeleteByOfferId(offerId);
    }
    @Override
    public Boolean checkOperationAllowed(Integer offerId) {
        List<Reservation> reservations = reservationRepository.findAllByOfferId(offerId);
        LocalDate today = LocalDate.now();
        for(Reservation r:reservations){
            if((today.compareTo(r.getEndDate())<0)){
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean checkUnavailableDate(Integer offerId, String startDate, Integer dateNumber) {
        Offer offer = offerRepository.findOfferById(offerId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateAction = LocalDate.parse(startDate, formatter);
        LocalDate endDateAction = startDateAction.plusDays(dateNumber);
        for(UnavailableOfferDates u: offer.getUnavailableDate()){
            if((u.getStartDate().compareTo(startDateAction) <= 0) && (startDateAction.compareTo(u.getEndDate()) <= 0))
                return false;
            if((u.getStartDate().compareTo(endDateAction) <= 0) && (endDateAction.compareTo(u.getEndDate()) <= 0))
                return false;
        }
        return true;
    }

}
