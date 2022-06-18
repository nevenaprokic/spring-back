package com.booking.ISAbackend.service.impl;

import com.booking.ISAbackend.dto.AdditionalServiceDTO;
import com.booking.ISAbackend.dto.AddressDTO;
import com.booking.ISAbackend.dto.BusinessReportDTO;
import com.booking.ISAbackend.dto.OfferForReportDTO;
import com.booking.ISAbackend.exceptions.OfferNotFoundException;
import com.booking.ISAbackend.exceptions.UserNotFoundException;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.repository.OfferRepository;
import com.booking.ISAbackend.repository.OwnerCategoryRepository;
import com.booking.ISAbackend.repository.QuickReservationRepository;
import com.booking.ISAbackend.repository.ReservationRepository;
import com.booking.ISAbackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
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
    @Autowired
    private ReservationReportService reservationReportService;
    @Autowired
    private OwnerCategoryRepository ownerCategoryRepository;


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
    @Caching(evict = {
            @CacheEvict(value="cottages", allEntries=true),
            @CacheEvict(value="ships", allEntries=true),
            @CacheEvict(value="instructors", allEntries=true)})
    public void delete(Integer offerId) throws OfferNotFoundException, InterruptedException {
        Offer offer = offerRepository.findOfferById(offerId);
        offer.setNumberOfModify(offer.getNumberOfModify()+1);
        if (offer == null)
            throw new OfferNotFoundException("Offer not found");
        if(offer.getSubscribedClients().size()!= 0){
            clientService.removeSubscribedClients(offer.getSubscribedClients(), offerId, offer.getName());
            offer.getSubscribedClients().clear();
        }
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

    @Override
    @Transactional
    public BusinessReportDTO getAdminBusinessReportData(String start, String end) throws UserNotFoundException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);

        List<Reservation> adventuresPastReservations = reservationRepository.findAllPastAdventureReservations(startDate, endDate);
        List<Reservation> cottagesPastReservations = reservationRepository.findAllPastCottageReservations(startDate, endDate);
        List<Reservation> shipsPastReservations = reservationRepository.findAllPastShipReservations(startDate, endDate);

        double totalReportPrice = 0;
        BusinessReportDTO reportData = new BusinessReportDTO();
        reportData.setTotalIncome(0.0);
        List<OfferForReportDTO> adventuresForReport = getOfferDataForAdminReport(adventuresPastReservations, reportData);
        List<OfferForReportDTO> cottagesForReport = getOfferDataForAdminReport(cottagesPastReservations, reportData);
        List<OfferForReportDTO> shipsForReport = getOfferDataForAdminReport(shipsPastReservations, reportData);

        reportData.setAdventuresIncome(adventuresForReport);
        reportData.setCottagesIncome(cottagesForReport);
        reportData.setShipsIncome(shipsForReport);

        return reportData;
    }


    @Transactional
    public List<OfferForReportDTO> getOfferDataForAdminReport(List<Reservation> reservations, BusinessReportDTO reportData) throws UserNotFoundException{

        HashMap<String, OfferForReportDTO> data = new HashMap<String, OfferForReportDTO>();
        for(Reservation reservation : reservations){

            Offer offer = reservation.getOffer();
            Owner owner = reservationReportService.findReservationOwner(reservation);
            int points = owner.getPoints();
            OwnerCategory category = ownerCategoryRepository.findByMatchingInterval(points).get(0);
            double earningSystemPrice = reservation.getPrice() * ((100 - category.getEarningsPercent())/100);

            if(data.containsKey(offer.getName())){
                OfferForReportDTO offerForReport = data.get(offer.getName());
                offerForReport.setNumberOfReservation(offerForReport.getNumberOfReservation() + 1);
                offerForReport.setTotalPrice(offerForReport.getTotalPrice() + earningSystemPrice);
                offerForReport.setRealPrice(offerForReport.getRealPrice() + reservation.getPrice());

            }
            else{
                OfferForReportDTO offerForReport = new OfferForReportDTO(offer.getName(),1, earningSystemPrice);
                offerForReport.setRealPrice(reservation.getPrice());
                offerForReport.setEarningPercent(100-category.getEarningsPercent());
                data.put(offer.getName(), offerForReport);
            }

            reportData.setTotalIncome(reportData.getTotalIncome() + earningSystemPrice);

            //String offerName, Integer numberOgReservation, Double totalPrice, Double realPrice, Double earningPerce

        }
        return  new ArrayList<OfferForReportDTO>(data.values());
    }
}

