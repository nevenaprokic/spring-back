package com.booking.ISAbackend.service.impl;

import com.booking.ISAbackend.dto.*;
import com.booking.ISAbackend.email.EmailSender;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.repository.*;
import com.booking.ISAbackend.service.AdditionalServiceService;
import com.booking.ISAbackend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private AdditionalServiceRepository additionalServiceRepository;
    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AdditionalServiceService additionalServiceService;
    @Autowired
    private QuickReservationRepository quickReservationRepository;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private ClientCategoryRepository clientCategoryRepository;
    @Autowired
    private CottageRepository cottageRepository;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private AdventureReporitory adventureReporitory;

    @Override
    @Transactional
    public void makeReservation(ReservationParamsDTO params) throws OptimisticLockException, OfferNotAvailableException, PreviouslyCanceledReservationException, ClientNotAvailableException, NotAllowedToMakeReservationException, InterruptedException {
        Optional<Integer> isCanceled = reservationRepository.checkIfCanceled(params.getEmail(), params.getDate(), params.getOfferId());
        Integer penalties = clientRepository.getPenalties(params.getEmail());
        if(penalties >= 3)
            throw new NotAllowedToMakeReservationException("Not allowed to make a reservation.");
        if(isCanceled.isPresent())
            throw new PreviouslyCanceledReservationException("Reservation has already been reserved and canceled.");
        if(!isAvailableClient(params.getEmail(), params.getDate().toString(), params.getEndingDate().toString()))
            throw new ClientNotAvailableException("Client is not available in this time period.");

        List<Optional<AdditionalService>> services = new ArrayList<>();
        for(AdditionalService s : params.getServices()){
            services.add(additionalServiceRepository.findById(s.getId()));
        }
        Client user = clientRepository.findByEmail(params.getEmail());
        Offer offer = offerRepository.findOfferById(params.getOfferId());
        List<Offer> nonAvailable = offerRepository.nonAvailableOffers(params.getDate(), params.getEndingDate());

        for(Offer o: nonAvailable){
            if(Objects.equals(o.getId(), offer.getId()))
                throw new OfferNotAvailableException("Offer is not available in that period.");
        }

        for(UnavailableOfferDates ofd : offer.getUnavailableDate()){
            // (StartA <= EndB) and (EndA >= StartB)
            if(ofd.getEndDate().isAfter(params.getDate()) && ofd.getStartDate().isBefore(params.getEndingDate()))
                throw new OfferNotAvailableException("Offer is not available in that period.");
        }

        if(params.getActionId() != null)
            quickReservationRepository.deleteById(params.getActionId());

        List<AdditionalService> ys = new ArrayList<>();
        for (Optional<AdditionalService> x : services) {
            x.ifPresent(ys::add);
        }

        offer.setNumberOfReservations(offer.getNumberOfReservations() + 1);
        Reservation r = new Reservation(params.getDate(), params.getEndingDate(), ys, params.getTotal(), params.getGuests(), offer, user, false);
        Thread.sleep(r.getClient().getPenal() * 3000L);
        reservationRepository.save(r);
    }

    @Override
    @Transactional
    public List<ReservationDTO> getPastCottageReservationsByClient(String email) throws IOException {
        List<Reservation> reservations = reservationRepository.getPastCottageReservationsByClient(email, LocalDate.now());
        return getReservationDTOS(reservations);
    }

    @Override
    @Transactional
    public List<ReservationDTO> getPastShipReservationsByClient(String email) throws IOException {
        List<Reservation> reservations = reservationRepository.getPastShipReservationsByClient(email, LocalDate.now());
        return getReservationDTOS(reservations);
    }

    @Override
    @Transactional
    public List<ReservationDTO> getPastAdventureReservationsByClient(String email) throws IOException {
        List<Reservation> reservations = reservationRepository.getPastAdventureReservationsByClient(email, LocalDate.now());
        return getReservationDTOS(reservations);
    }

    @Override
    @Transactional
    public Boolean isAvailableOffer(Integer offerId, String startDate, Integer dayNum) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateReservation = LocalDate.parse(startDate, formatter);
        LocalDate endDateReservation = startDateReservation.plusDays(dayNum);
        List<Reservation> reservations = reservationRepository.findAllByOfferId(offerId);
        for(Reservation q: reservations){
            if((q.getStartDate().compareTo(startDateReservation) <= 0) && (startDateReservation.compareTo(q.getEndDate()) <= 0))
                return false;
            if((q.getStartDate().compareTo(endDateReservation) <= 0) && (endDateReservation.compareTo(q.getEndDate()) <= 0))
                return false;
        }
        return true;
    }
    @Override
    public Boolean isAvailableClient(String emailClient, String startReservation, String endReservation){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateReservation = LocalDate.parse(startReservation, formatter);
        LocalDate endDateReservation = LocalDate.parse(endReservation, formatter);
        List<Reservation> reservations = reservationRepository.findByClientEmail(emailClient);
        for(Reservation q: reservations){
            if((q.getStartDate().compareTo(startDateReservation) <= 0) && (startDateReservation.compareTo(q.getEndDate()) <= 0))
                return false;
            if((q.getStartDate().compareTo(endDateReservation) <= 0) && (endDateReservation.compareTo(q.getEndDate()) <= 0))
                return false;
        }
        return true;
    }
    @Override
    @Transactional
    public Integer makeReservationOwner(NewReservationDTO dto) throws InterruptedException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateReservation = LocalDate.parse(dto.getStartDateReservation(), formatter);
        LocalDate endDateReservation = startDateReservation.plusDays(dto.getDaysReservation());
        Offer offer = offerRepository.findOfferById(dto.getOfferId());
        Client client = clientRepository.findByEmail(dto.getClientUserName());

//        List<AdditionalService> newAdditionalService = new ArrayList<>();
//        for(AdditionalService a: dto.getServices()){
//            AdditionalService additionalService = additionalServiceRepository.save(new AdditionalService(a.getName(),a.getPrice()));
//            newAdditionalService.add(additionalService);
//        }

        List<Optional<AdditionalService>> services = new ArrayList<>();
        for(AdditionalService s : dto.getServices()){
            services.add(additionalServiceRepository.findById(s.getId()));
        }
        List<AdditionalService> newAdditionalService = new ArrayList<>();
        for (Optional<AdditionalService> x : services) {
            x.ifPresent(newAdditionalService::add);
        }
        offer.setNumberOfReservations(offer.getNumberOfReservations()+1);
        Reservation reservation = new Reservation(startDateReservation, endDateReservation,newAdditionalService, dto.getPrice()*dto.getDaysReservation(), dto.getPeopleNum(), offer, client, false);
        Reservation newReservation = reservationRepository.save(reservation);
        offer.getReservations().add(newReservation);
        Thread.sleep(client.getPenal()*2000);
        offerRepository.save(offer);
        sendEmail(client.getEmail(), reservation);
        return newReservation.getId();
    }

    @Transactional
    void sendEmail(String client, Reservation reservation){
        emailSender.notifyClientNewReservation("markoooperic123+++fdf@gmail.com", reservation);

    }

    @Override
    @Transactional
    public List<ClientDTO> getClientByCottageOwnerEmail(String email){
        LocalDate today = LocalDate.now();
        List<Reservation> currentReservation = reservationRepository.findCurrentByOwnerEmail(email, today);
        List<ClientDTO> clients = new ArrayList<>();
        for(Reservation r: currentReservation){
            ClientCategory category = clientCategoryRepository.findByMatchingInterval(r.getClient().getPoints()).get(0);
            ClientDTO dto = new ClientDTO(r.getClient(), r.getOffer().getId(), category.getName(), category.getDiscount());
            clients.add(dto);
        }
        return clients;
    }
    @Override
    @Transactional
    public List<ClientDTO> getClientByShipOwnerEmail(String email){
        LocalDate today = LocalDate.now();
        List<Reservation> currentReservation = reservationRepository.findCurrentByShipOwnerEmail(email, today);
        List<ClientDTO> clients = new ArrayList<>();
        for(Reservation r: currentReservation){
            ClientCategory category = clientCategoryRepository.findByMatchingInterval(r.getClient().getPoints()).get(0);
            ClientDTO dto = new ClientDTO(r.getClient(), r.getOffer().getId(), category.getName(), category.getDiscount());
            clients.add(dto);
        }
        return clients;
    }
    @Override
    @Transactional
    public List<ClientDTO> getClientByInstructorEmail(String email){
        LocalDate today = LocalDate.now();
        List<Reservation> currentReservation = reservationRepository.findCurrentByInstructorEmail(email, today);
        List<ClientDTO> clients = new ArrayList<>();
        for(Reservation r: currentReservation){
            ClientCategory category = clientCategoryRepository.findByMatchingInterval(r.getClient().getPoints()).get(0);
            ClientDTO dto = new ClientDTO(r.getClient(), r.getOffer().getId(), category.getName(), category.getDiscount());
            clients.add(dto);
        }
        return clients;
    }


    @Override
    @Transactional
    public List<ReservationDTO> getAllReservation(String ownerId, String role) throws IOException {
        List<Reservation> reservations = new ArrayList<>();
        if(role.equals(UserType.SHIP_OWNER.toString()))
            reservations = reservationRepository.findPastReservationByShipOwnerEmail(ownerId, LocalDate.now());
        else if(role.equals(UserType.COTTAGE_OWNER.toString()))
            reservations = reservationRepository.findPastReservationByCottageOwnerEmail(ownerId, LocalDate.now());
        else if(role.equals(UserType.INSTRUCTOR.toString()))
            reservations = reservationRepository.findPastReservationByInstructorEmail(ownerId, LocalDate.now());
        return getReservationDTOS(reservations);
    }

    private List<ReservationDTO> getReservationDTOS(List<Reservation> reservations) throws IOException {
        List<ReservationDTO> reservationDTOS = new ArrayList<>();
        for(Reservation r: reservations){
            ReservationDTO dto = new ReservationDTO(r);
//            dto.setAdditionalServices(additionalServiceService.getAdditionalServices(r.getOffer()));
            dto.setAdditionalServices(makeAdditionalServicesDTO(r.getAdditionalServices()));
            reservationDTOS.add(dto);
        }
        return reservationDTOS;
    }

    private List<AdditionalServiceDTO> makeAdditionalServicesDTO(List<AdditionalService> services){
        List<AdditionalServiceDTO> retList = new ArrayList<>();
        for(AdditionalService as : services){
            retList.add(new AdditionalServiceDTO(as));
        }
        return retList;
    }

    @Override
    @Transactional
    public List<ReservationDTO> getUpcomingCottageReservationsByClient(String email) throws IOException {
        List<Reservation> reservations = reservationRepository.getUpcomingCottageReservationsByClient(email, LocalDate.now());
        return getReservationDTOS(reservations);
    }

    @Override
    @Transactional
    public List<ReservationDTO> getUpcomingShipReservationsByClient(String email) throws IOException {
        List<Reservation> reservations = reservationRepository.getUpcomingShipReservationsByClient(email, LocalDate.now());
        return getReservationDTOS(reservations);
    }

    @Override
    @Transactional
    public List<ReservationDTO> getUpcomingAdventureReservationsByClient(String email) throws IOException {
        List<Reservation> reservations = reservationRepository.getUpcomingAdventureReservationsByClient(email, LocalDate.now());
        return getReservationDTOS(reservations);
    }

    @Override
    @Transactional
    public void cancelReservation(Integer id) throws CancellingReservationException {
        Optional<Reservation> r = reservationRepository.findById(id);
        LocalDate today = r.get().getStartDate();
        LocalDate boundary = today.minusDays(3);
        Optional<Integer> exists = Optional.ofNullable(reservationRepository.checkCancelCondition(id, boundary, LocalDate.now()));
        if(exists.isPresent())
            throw new CancellingReservationException("Cannot cancel reservation.");
        Offer o = r.get().getOffer();
        o.setNumberOfReservations(o.getNumberOfReservations() - 1);
        reservationRepository.deleteById(id);
    }

    @Override
    public List<AttendanceReportDTO> getAttendanceReportYearlyCottage(String email, String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(date, formatter);

        List<AttendanceReportDTO> dataForReport = new ArrayList<>();
        List<Reservation> allReservations = reservationRepository.findAllByCottageOwnerEmail(email);
        List<Cottage> cottages = cottageRepository.findCottageByCottageOwnerEmail(email);
        for(Cottage c:cottages){
            TreeMap<LocalDate, Integer> yearlyMap = getMapAttendance(365,  startDate, 31, allReservations, c);
            AttendanceReportDTO reportDTO = new AttendanceReportDTO(c.getName(),yearlyMap);
            dataForReport.add(reportDTO);
        }
        return  dataForReport;

    }
    @Override
    public List<AttendanceReportDTO> getAttendanceReportMonthlyCottage(String email, String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(date, formatter);

        List<AttendanceReportDTO> dataForReport = new ArrayList<>();
        List<Reservation> allReservations = reservationRepository.findAllByCottageOwnerEmail(email);
        List<Cottage> cottages = cottageRepository.findCottageByCottageOwnerEmail(email);
        for(Cottage c:cottages){
            TreeMap<LocalDate, Integer> monthlyMap = getMapAttendance(22,  startDate, 7, allReservations, c);
            AttendanceReportDTO reportDTO = new AttendanceReportDTO(c.getName(),monthlyMap);
            dataForReport.add(reportDTO);
        }
        return  dataForReport;
    }


    @Override
    public List<AttendanceReportDTO> getAttendanceReportWeeklyCottage(String email, String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(date, formatter);

        List<AttendanceReportDTO> dataForReport = new ArrayList<>();
        List<Reservation> allReservations = reservationRepository.findAllByCottageOwnerEmail(email);
        List<Cottage> cottages = cottageRepository.findCottageByCottageOwnerEmail(email);
        for(Cottage c:cottages){
            TreeMap<LocalDate,Integer> weeklyMap =  getMapAttendance(7,  startDate, 1, allReservations, c);
            AttendanceReportDTO reportDTO = new AttendanceReportDTO(c.getName(),weeklyMap);
            dataForReport.add(reportDTO);
        }
        return  dataForReport;
    }

    private TreeMap<LocalDate,Integer>mapInitialization(Integer day, LocalDate start, Integer num){
        TreeMap<LocalDate,Integer> map = new TreeMap<>();
        for(int i = 0; i<day;i+=num){
            map.put(start.plusDays(i),0);
        }
        return map;
    }
    private TreeMap<LocalDate, Integer> getMapAttendance(int day, LocalDate startDate, int num, List<Reservation> allReservations, Offer offer) {
        TreeMap<LocalDate,Integer> dateMap = mapInitialization(day, startDate, num);
        for(Reservation r: allReservations){
            if(r.getOffer().getId() == offer.getId()){
                for(int week = 0; week < day; week+= num){
                    for(int i = 0; i< num; i++){
                        if((startDate.plusDays(i+week).compareTo(r.getStartDate()) >= 0) && (startDate.plusDays(i+week).compareTo(r.getEndDate()) <= 0))
                            dateMap.put(startDate.plusDays(week),(dateMap.get(startDate.plusDays(week))+1));
                    }
                }
            }
        }
        return dateMap;
    }

    @Override
    public List<AttendanceReportDTO> getAttendanceReportYearlyShip(String email, String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(date, formatter);

        List<AttendanceReportDTO> dataForReport = new ArrayList<>();
        List<Reservation> allReservations = reservationRepository.findAllByShipOwnerEmail(email);
        List<Ship> ships = shipRepository.findShipByShipOwnerEmail(email);
        for(Ship s:ships){
            TreeMap<LocalDate, Integer> yearlyMap = getMapAttendance(365,  startDate, 31, allReservations, s);
            AttendanceReportDTO reportDTO = new AttendanceReportDTO(s.getName(),yearlyMap);
            dataForReport.add(reportDTO);
        }
        return  dataForReport;

    }
    @Override
    public List<AttendanceReportDTO> getAttendanceReportMonthlyShip(String email, String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(date, formatter);

        List<AttendanceReportDTO> dataForReport = new ArrayList<>();
        List<Reservation> allReservations = reservationRepository.findAllByShipOwnerEmail(email);
        List<Ship> ships = shipRepository.findShipByShipOwnerEmail(email);
        for(Ship s:ships){
            TreeMap<LocalDate, Integer> monthlyMap = getMapAttendance(22,  startDate, 7, allReservations, s);
            AttendanceReportDTO reportDTO = new AttendanceReportDTO(s.getName(),monthlyMap);
            dataForReport.add(reportDTO);
        }
        return  dataForReport;
    }


    @Override
    public List<AttendanceReportDTO> getAttendanceReportWeeklyShip(String email, String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(date, formatter);

        List<AttendanceReportDTO> dataForReport = new ArrayList<>();
        List<Reservation> allReservations = reservationRepository.findAllByShipOwnerEmail(email);
        List<Ship> ships = shipRepository.findShipByShipOwnerEmail(email);
        for(Ship s:ships){
            TreeMap<LocalDate,Integer> weeklyMap =  getMapAttendance(7,  startDate, 1, allReservations, s);
            AttendanceReportDTO reportDTO = new AttendanceReportDTO(s.getName(),weeklyMap);
            dataForReport.add(reportDTO);
        }
        return  dataForReport;
    }

    @Override
    public List<AttendanceReportDTO> getAttendanceReportYearlyAdventure(String email, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(date, formatter);

        List<AttendanceReportDTO> dataForReport = new ArrayList<>();
        List<Reservation> allReservations = reservationRepository.findAllByInstructorEmail(email);
        List<Adventure> adventures = adventureReporitory.findAdventureByInstructorEmail(email);
        for(Adventure a:adventures){
            TreeMap<LocalDate, Integer> yearlyMap = getMapAttendance(365,  startDate, 31, allReservations, a);
            AttendanceReportDTO reportDTO = new AttendanceReportDTO(a.getName(),yearlyMap);
            dataForReport.add(reportDTO);
        }
        return  dataForReport;
    }

    @Override
    public List<AttendanceReportDTO> getAttendanceReportMonthlyAdventure(String email, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(date, formatter);

        List<AttendanceReportDTO> dataForReport = new ArrayList<>();
        List<Reservation> allReservations = reservationRepository.findAllByInstructorEmail(email);
        List<Adventure> adventures = adventureReporitory.findAdventureByInstructorEmail(email);
        for(Adventure a:adventures){
            TreeMap<LocalDate, Integer> monthlyMap = getMapAttendance(22,  startDate, 7, allReservations, a);
            AttendanceReportDTO reportDTO = new AttendanceReportDTO(a.getName(),monthlyMap);
            dataForReport.add(reportDTO);
        }
        return  dataForReport;
    }

    @Override
    public List<AttendanceReportDTO> getAttendanceReportWeeklyAdventure(String email, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(date, formatter);

        List<AttendanceReportDTO> dataForReport = new ArrayList<>();
        List<Reservation> allReservations = reservationRepository.findAllByInstructorEmail(email);
        List<Adventure> adventures = adventureReporitory.findAdventureByInstructorEmail(email);
        for(Adventure a:adventures){
            TreeMap<LocalDate,Integer> weeklyMap =  getMapAttendance(7,  startDate, 1, allReservations, a);
            AttendanceReportDTO reportDTO = new AttendanceReportDTO(a.getName(),weeklyMap);
            dataForReport.add(reportDTO);
        }
        return  dataForReport;
    }

    @Override
    public void sendEmail(ReservationParamsDTO params) {
        emailSender.reservationConfirmation(params);
    }


//    private String localDateToString(LocalDate date){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
//        return formatter.format(date);
//    }
//
//    private String getOfferPhoto(Offer offer) throws IOException {
//        String photoName = "no-image.png";
//        if(!offer.getPhotos().isEmpty()) {
//            photoName = offer.getPhotos().get(0).getPath();
//        }
//        return convertPhoto(photoName);
//    }
//
//    private String convertPhoto(String photoName) throws IOException {
//        String pathFile = "./src/main/frontend/src/components/images/" + photoName;
//        byte[] bytes = Files.readAllBytes(Paths.get(pathFile));
//        String photoData = Base64.getEncoder().encodeToString(bytes);
//        return photoData;
//    }
}
