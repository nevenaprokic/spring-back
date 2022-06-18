package com.booking.ISAbackend.service.impl;

import com.booking.ISAbackend.dto.*;
import com.booking.ISAbackend.email.EmailSender;
import com.booking.ISAbackend.exceptions.UserNotFoundException;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.repository.*;
import com.booking.ISAbackend.service.ReservationReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationReportServiceImpl implements ReservationReportService {


    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationReportRepository reservationReportRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CottageRepository cottageRepository;
    @Autowired
    private AdventureReporitory adventureReporitory;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private ClientCategoryRepository clientCategoryRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private OwnerCategoryRepository ownerCategoryRepository;
    @Autowired
    private CottageOwnerRepository cottageOwnerRepository;
    @Autowired
    private ShipOwnerRepository shipOwnerRepository;
    @Autowired
    private EmailSender emailSender;



    @Override
    public List<Integer> getReservationReportCottageOwner(String ownerEmail) {
        LocalDate today = LocalDate.now();
        List<Integer> reservationWithReport = reservationRepository.findReservationWithReportByCottageOwnerEmail(ownerEmail, today);
        List<Reservation> reservations = reservationRepository.findPastReservationByCottageOwnerEmail(ownerEmail,today);
        List<Integer> reservationWithNoReport = new ArrayList<>();
        for(Reservation r: reservations) {
            if(!reservationWithReport.contains(r.getId())){
                reservationWithNoReport.add(r.getId());
            }
        }
        return reservationWithNoReport;
    }

    @Override
    public List<Integer> getReservationReportShipOwner(String ownerEmail) {
        LocalDate today = LocalDate.now();
        List<Integer> reservationWithReport = reservationRepository.findReservationWithReportByShipOwnerEmail(ownerEmail, today);
        List<Reservation> reservations = reservationRepository.findPastReservationByShipOwnerEmail(ownerEmail,today);
        List<Integer> reservationWithNoReport = new ArrayList<>();
        for(Reservation r: reservations) {
            if(!reservationWithReport.contains(r.getId())){
                reservationWithNoReport.add(r.getId());
            }
        }
        return reservationWithNoReport;
    }

    @Override
    public void addReservationReport(NewReservationReportDTO dto){
        Optional<Client> client = clientRepository.findById(dto.getClientId());
        Optional<Reservation> reservation = reservationRepository.findById(dto.getReservationId());
        Boolean penalOption = false;
        if(Impression.NEGATIVE == dto.getValueImpression())
            penalOption = true;
        Boolean automaticallyPenal = false;
        if(!dto.getValueShowUp())
            automaticallyPenal = true;
            if(client.isPresent() && reservation.isPresent()){
                addPenalToClient(client.get());
            }
        if(client.isPresent() && reservation.isPresent()){
            ReservationReport reservationReport = new ReservationReport(penalOption,automaticallyPenal, dto.getComment(),reservation.get(),client.get());
            reservationReport.setReviewed(false);
            reservationReport.setSentDate(LocalDate.now());
            reservationReportRepository.save(reservationReport);
        }

    }

    private void addPenalToClient(Client client) {
        int penals = client.getPenal();
        penals +=1 ;
        client.setPenal(penals);
        clientRepository.save(client);
    }

    @Override
    @Transactional
    public List<OfferForReportDTO> getReportIncomeStatementCottage(String start, String end, String email) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);

        CottageOwner cottageOwner = cottageOwnerRepository.findByEmail(email);
        List<Reservation> reservations = reservationRepository.findPastReservationByCottageOwnerEmail(email,LocalDate.now());

        List<OfferForReportDTO> offers = listInitialization(email);
        getReportList(reservations, startDate, endDate, offers, cottageOwner.getPoints());
        return offers;
    }

    @Override
    @Transactional
    public List<OfferForReportDTO> getReportIncomeStatementShip(String start, String end, String email) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);

        ShipOwner shipOwner = shipOwnerRepository.findByEmail(email);
        List<Reservation> reservations = reservationRepository.findPastReservationByShipOwnerEmail(email,LocalDate.now());

        List<OfferForReportDTO> offers = listInitializationShip(email);
        return  getReportList(reservations, startDate, endDate, offers, shipOwner.getPoints());

    }

    private List<OfferForReportDTO> getReportList(List<Reservation> reservations, LocalDate startDate, LocalDate endDate, List<OfferForReportDTO> offers, int points) {
        OwnerCategory category =  ownerCategoryRepository.findByMatchingInterval(points).get(0);
        Double totalPrice = 0.0;
        Integer numberOfReservation = 0;

        for(Reservation r: reservations){
            if((r.getStartDate().compareTo(startDate)>=0)&&(r.getStartDate().compareTo(endDate)<=0)){
                totalPrice += (r.getPrice() * category.getEarningsPercent()/100);
                numberOfReservation++;
                for(OfferForReportDTO of: offers){
                    if(of.getOfferName().equals(r.getOffer().getName())){
                        of.setNumberOfReservation((of.getNumberOfReservation()+1));
                        of.setTotalPrice((of.getTotalPrice()+(r.getPrice()* category.getEarningsPercent()/100)));
                        break;
                    }
                }
            }
        }
        offers.add(new OfferForReportDTO("total", numberOfReservation, totalPrice));
        return offers;
    }

    @Override
    @Transactional
    public List<OfferForReportDTO> getReportIncomeStatementAdventure(String start, String end, String email) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(start, formatter);
        LocalDate endDate = LocalDate.parse(end, formatter);

        List<Reservation> reservations = reservationRepository.findPastReservationByInstructorEmail(email,LocalDate.now());
        Instructor instructor = instructorRepository.findByEmail(email);
        List<OfferForReportDTO> offers = listInitializationAdventure(email);
        return  getReportList(reservations, startDate, endDate, offers, instructor.getPoints());
    }

    @Override
    public List<Integer> getNotReportedReservationsInstructor(String ownerEmail) {
        LocalDate today = LocalDate.now();
        List<Integer> reservationWithReport = reservationRepository.findReservationWithReportByInstructorEmeil(ownerEmail, today);
        List<Reservation> reservations = reservationRepository.findPastReservationByInstructorEmail(ownerEmail,today);
        List<Integer> reservationWithNoReport = new ArrayList<>();
        for(Reservation r: reservations) {
            if(!reservationWithReport.contains(r.getId())){
                reservationWithNoReport.add(r.getId());
            }
        }
        return reservationWithNoReport;
    }

    @Override
    @Transactional
    public List<ReservationReportAdminDTO> getAllNotReviewedWIthPenaltyOption() throws IOException {
        List<ReservationReport> reservationReports = reservationReportRepository.findAllNotReviewedForPenalty();
        List<ReservationReportAdminDTO> reportsForAdmin = new ArrayList<ReservationReportAdminDTO>();
        for(ReservationReport report: reservationReports){
            reportsForAdmin.add(createAdminReportDTO(report));
        }
        return reportsForAdmin;
    }

    @Override
    @Transactional
    public void addPenaltyToClient(Integer reportId) throws UserNotFoundException {
        Optional<ReservationReport> report = reservationReportRepository.findById(reportId);
        if(report.isPresent()){
            ReservationReport reservationReport = report.get();
            Owner owner = findReservationOwner(reservationReport.getReservation());
            Client client = reservationReport.getReservation().getClient();
            client.setPenal(client.getPenal()+ 1);
            clientRepository.save(client);
            reservationReport.setReviewed(true);
            reservationReportRepository.save(reservationReport);
            sendAddPenaltyEmailToUsers(owner, client, reservationReport.getReservation());
        }
    }

    @Override
    @Transactional
    public void rejectPenaltyOption(Integer reportId) throws UserNotFoundException {
        Optional<ReservationReport> report = reservationReportRepository.findById(reportId);
        if(report.isPresent()){
            ReservationReport reservationReport = report.get();
            Owner owner = findReservationOwner(reservationReport.getReservation());
            Client client = reservationReport.getReservation().getClient();
            reservationReport.setReviewed(true);
            reservationReportRepository.save(reservationReport);
            sendRejectPenaltyEmailToUsers(owner, client, reservationReport.getReservation());
        }
    }

    @Transactional
    public void sendAddPenaltyEmailToUsers(Owner owner, Client client, Reservation reservation) {
        String clientMessage = "You GOT 1 PENALTY for reservation '" + reservation.getOffer().getName() + "' for period:  " +
        reservation.getStartDate().toString() + "-" + reservation.getEndDate() + "  at the request of the owner.";

        String ownerMessage = "Client: " + client.getFirstName() + " " + client.getLastName() + " GET 1 PENALTY for reservation " +  reservation.getOffer().getName() + "' for period:  " +
                reservation.getStartDate().toString() + " - " + reservation.getEndDate() + "  on your request.";

        emailSender.notifyUserAboutReservationReport(owner.getEmail(), ownerMessage);
        emailSender.notifyUserAboutReservationReport(client.getEmail(), clientMessage);
    }

    @Transactional
    public void sendRejectPenaltyEmailToUsers(Owner owner, Client client, Reservation reservation) {
        String clientMessage = "You DIDN'T GET 1 PENALTY for reservation '" + reservation.getOffer().getName() + "' for period:  " +
                reservation.getStartDate().toString() + "-" + reservation.getEndDate() + "  at the request of the owner.";

        String ownerMessage = "Client: " + client.getFirstName() + " " + client.getLastName() + " DIDN'T GET 1 PENALTY for reservation " +  reservation.getOffer().getName() + "' for period:  " +
                reservation.getStartDate().toString() + " - " + reservation.getEndDate() + "  on your request.";

        emailSender.notifyUserAboutReservationReport(owner.getEmail(), ownerMessage);
        emailSender.notifyUserAboutReservationReport(client.getEmail(), clientMessage);
    }

    @Override
    public Owner findReservationOwner(Reservation reservation) throws UserNotFoundException {
        List<Integer> cottagesId = cottageRepository.getCottagesId();
        List<Integer> adventuresId = adventureReporitory.getAdveturesId();
        List<Integer> shipsId = shipRepository.getShipsId();
        Offer offer = reservation.getOffer();
        if(cottagesId.contains(offer.getId())){
            return cottageOwnerRepository.findCottageOwnerByCottageId(offer.getId());
        }
        else if(adventuresId.contains(offer.getId())){
            return instructorRepository.findInstructorByAdventure(offer.getId());
        }
        else if(shipsId.contains(offer.getId())){
            return shipOwnerRepository.findShipOwnerByShipId(offer.getId());
        }
        else{
            throw new UserNotFoundException("Offer's owner not foud.");
        }
    }

    private ArrayList<OfferForReportDTO> listInitializationAdventure(String email){
        ArrayList<OfferForReportDTO> offers = new ArrayList<OfferForReportDTO>();
        List<Adventure> adventures = adventureReporitory.findAdventureByInstructorEmail(email);
        for(Adventure a: adventures){
            offers.add(new OfferForReportDTO(a.getName(),0,0.0));
        }
        return offers;
    }

    private ArrayList<OfferForReportDTO> listInitializationShip(String email){
        ArrayList<OfferForReportDTO> offers = new ArrayList<OfferForReportDTO>();
        List<Ship> ships = shipRepository.findShipByShipOwnerEmail(email);
        for(Ship s: ships){
            offers.add(new OfferForReportDTO(s.getName(),0,0.0));
        }
        return offers;
    }

    private ArrayList<OfferForReportDTO> listInitialization(String email){
        ArrayList<OfferForReportDTO> offers = new ArrayList<OfferForReportDTO>();
        List<Cottage> cottages = cottageRepository.findCottageByCottageOwnerEmail(email);
        for(Cottage c: cottages){
            offers.add(new OfferForReportDTO(c.getName(),0,0.0));
        }
        return offers;
    }

    @Transactional
    public ReservationReportAdminDTO createAdminReportDTO(ReservationReport report) throws IOException {
        Reservation reservation = report.getReservation();
        ReservationDTO reservationDTO = new ReservationDTO(reservation);
        Client client = reservation.getClient();
        ClientCategory category = clientCategoryRepository.findByMatchingInterval(client.getPoints()).get(0);
        ClientDTO clientDTO = new ClientDTO(reservation.getClient(), category.getName());
        ReservationReportAdminDTO reportDTO = new ReservationReportAdminDTO(report, reservationDTO, clientDTO);
        return reportDTO;
    }

    private String convertDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return formatter.format(date);
    }
}
