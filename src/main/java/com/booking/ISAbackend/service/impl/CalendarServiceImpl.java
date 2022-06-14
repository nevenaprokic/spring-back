package com.booking.ISAbackend.service.impl;

import com.booking.ISAbackend.dto.CalendarItem;
import com.booking.ISAbackend.dto.QuickReservationDTO;
import com.booking.ISAbackend.dto.ReservationDTO;
import com.booking.ISAbackend.dto.UnavailableDateDTO;
import com.booking.ISAbackend.exceptions.BusyDateIntervalException;
import com.booking.ISAbackend.exceptions.InvalidDateInterval;
import com.booking.ISAbackend.exceptions.PassedDateException;
import com.booking.ISAbackend.exceptions.UnavailableDatesAlreadyDefine;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.repository.*;
import com.booking.ISAbackend.service.AdditionalServiceService;
import com.booking.ISAbackend.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarServiceImpl implements CalendarService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UnavailabelOfferDatesRepository unavailabelOfferDatesRepository;

    @Autowired
    AdditionalServiceService additionalServiceService;

    @Autowired
    OfferRepository offerRepository;

    @Autowired
    QuickReservationRepository quickReservationRepository;

    @Override
    @Transactional
    public List<CalendarItem> getCalendarInfo(String ownerEmail, int offerId) {
        MyUser user = userRepository.findByEmail(ownerEmail);
        int id = user.getId();
        List<Reservation> reservations = reservationRepository.findAllByOfferId(offerId);
        List<UnavailableOfferDates> unavailableOfferDates = unavailabelOfferDatesRepository.findByOfferId(offerId);
        List<QuickReservation> quickreservations = quickReservationRepository.findQuickReservationsByOfferId(offerId);
        return generateCalendarItems(reservations, unavailableOfferDates, quickreservations);
    }

    @Override
    @Transactional
    public ReservationDTO getReservationDetails(int reservationId) throws IOException {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if(reservation.isPresent()){
            Reservation r = reservation.get();
            Offer offer = r.getOffer();
            Client client = r.getClient();
            String clientName = client.getFirstName() + " " + client.getLastName();
            String offerPhoto = getOfferPhoto(offer);

            ReservationDTO reservationDTO = new ReservationDTO(r.getId(),
                    localDateToString(r.getStartDate()),
                    localDateToString(r.getEndDate()),
                    additionalServiceService.getAdditionalServices(offer),
                    r.getPrice(),
                    r.getNumberOfPerson(),
                    offer.getId(),
                    offer.getName(),
                    client.getId(),
                    clientName,
                    offerPhoto
                    );
            reservationDTO.setClientEmail(client.getEmail());
            return  reservationDTO;
        }
        return null;

    }
    @Override
    public void addOffersUnavailableDates(UnavailableDateDTO unavailableDates) throws BusyDateIntervalException, InvalidDateInterval, UnavailableDatesAlreadyDefine, PassedDateException {
        Offer offer = offerRepository.getById(unavailableDates.getOfferId());
        LocalDate start = revertToDate(unavailableDates.getStartDate());
        LocalDate end = revertToDate(unavailableDates.getEndDate());
        if(start.isBefore(LocalDate.now())) throw new PassedDateException();
        if (!isAvailablePeriod(offer.getId(), start, end)) throw new BusyDateIntervalException();
        if(!checkDateInterval(start, end)) throw  new InvalidDateInterval();
        List<UnavailableOfferDates> exitingUnavailableDatesInInterval = unavailabelOfferDatesRepository.findDatesByOfferInInterval(start, end,unavailableDates.getOfferId());
        if(!exitingUnavailableDatesInInterval.isEmpty()) throw  new UnavailableDatesAlreadyDefine();
        UnavailableOfferDates dates = new UnavailableOfferDates(offer, start, end);
        unavailabelOfferDatesRepository.save(dates);
    }

    @Override
    @Transactional
    public QuickReservationDTO getActionDetails(int actionId) throws IOException {
        Optional<QuickReservation> action = quickReservationRepository.findById(actionId);
        if(action.isPresent()){
            QuickReservation quickReservation = action.get();
            Offer offer = quickReservation.getOffer();
            String offerPhoto = getOfferPhoto(offer);

            QuickReservationDTO actionDTO = new QuickReservationDTO(quickReservation);
            actionDTO.setOfferName(offer.getName());
            actionDTO.setOfferPhoto(offerPhoto);
            actionDTO.setEndDateActionStr(localDateToString(quickReservation.getEndDateAction()));
            actionDTO.setStartDateActionStr(localDateToString(quickReservation.getStartDateAction()));
            actionDTO.setEndDateStr(localDateToString(quickReservation.getEndDate()));
            actionDTO.setStartDateStr(localDateToString(quickReservation.getStartDate()));
            return  actionDTO;
        }
        return null;
    }

    @Transactional
    public List<CalendarItem> generateCalendarItems(List<Reservation> reservations, List<UnavailableOfferDates> unavailableOfferDates, List<QuickReservation> quickreservations){
        List<CalendarItem> calendarItems = new ArrayList<CalendarItem>();

        createCalendarItemsFromReservations(calendarItems, reservations);
        createCalendarItemsFromUnavailableDates(calendarItems, unavailableOfferDates);
        createCalendarItemsFromActions(calendarItems, quickreservations);

        return calendarItems;
    }

    private boolean isAvailablePeriod(int offerId, LocalDate startDate, LocalDate endDate){
        List<Reservation> reservations = reservationRepository.findAllByOfferId(offerId);
        for(Reservation reservation : reservations){
            if((reservation.getStartDate().compareTo(startDate) <= 0) && (startDate.compareTo(reservation.getEndDate()) <= 0))
                return false;
            if((reservation.getStartDate().compareTo(endDate) <= 0) && (endDate.compareTo(reservation.getEndDate()) <= 0))
                return false;
        }
        List<QuickReservation> quickReservations = quickReservationRepository.findQuickReservationsByOfferId(offerId);
        for(QuickReservation action : quickReservations){
            if((action.getStartDate().compareTo(startDate) <= 0) && (startDate.compareTo(action.getEndDate()) <= 0))
                return false;
            if((action.getStartDate().compareTo(endDate) <= 0) && (endDate.compareTo(action.getEndDate()) <= 0))
                return false;
        }
        return true;
    }

    private void createCalendarItemsFromReservations(List<CalendarItem> calendarItems, List<Reservation> reservations){
        for(Reservation reservation : reservations){
            Offer of = reservation.getOffer();
            String title = "Reservation: " + of.getName();
            CalendarItem item = new CalendarItem(reservation.getId(), true, reservation.getStartDate().toString(), reservation.getEndDate().toString(), title, false);
            calendarItems.add(item);
        }
    }

    private void createCalendarItemsFromUnavailableDates(List<CalendarItem> calendarItems, List<UnavailableOfferDates> unavailableOfferDates){
        for(UnavailableOfferDates date : unavailableOfferDates){
            String title = "Unavailable";
            CalendarItem item = new CalendarItem(date.getId(), false, date.getStartDate().toString(), date.getEndDate().toString(), title, false);
            calendarItems.add(item);
        }
    }

    private void createCalendarItemsFromActions(List<CalendarItem> calendarItems, List<QuickReservation> actions){
        for(QuickReservation reservation : actions){
            String title = "Quick Action";
            CalendarItem item = new CalendarItem(reservation.getId(),
                    false, reservation.getStartDateAction().toString(),
                    reservation.getEndDateAction().toString(),
                    title,
                    true);
            calendarItems.add(item);
        }
    }

    private String localDateToString(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return formatter.format(date);
    }

    private String getOfferPhoto(Offer offer) throws IOException {
        String photoName = "no-image.png";
        if(!offer.getPhotos().isEmpty()) {
            photoName = offer.getPhotos().get(0).getPath();
        }
        return convertPhoto(photoName);
    }

    private String convertPhoto(String photoName) throws IOException {
        String pathFile = "../frontend/src/components/images/" + photoName;
        byte[] bytes = Files.readAllBytes(Paths.get(pathFile));
        String photoData = Base64.getEncoder().encodeToString(bytes);
        return photoData;
    }

    private LocalDate revertToDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    private boolean checkDateInterval(LocalDate start, LocalDate end){
        if(start == end) return true;
        return end.isAfter(start);
    }

}
