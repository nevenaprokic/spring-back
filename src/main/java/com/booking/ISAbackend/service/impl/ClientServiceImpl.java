package com.booking.ISAbackend.service.impl;

import com.booking.ISAbackend.confirmationToken.ConfirmationTokenService;
import com.booking.ISAbackend.dto.*;
import com.booking.ISAbackend.email.EmailSender;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.repository.*;
import com.booking.ISAbackend.service.ClientCategoryService;
import com.booking.ISAbackend.service.ClientService;
import com.booking.ISAbackend.service.ReservationReportService;
import com.booking.ISAbackend.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private DeleteRequestRepository deleteRequestRepository;

    @Autowired
    private AddressRepository adressRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ClientCategoryService clientCategoryService;

    @Autowired
    private MarkRepository markRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private ClientCategoryRepository categoryRepository;

    @Autowired
    private ReservationReportService reservationReportService;

    @Override
    @Transactional
    public String save(ClientRequest cr) throws InterruptedException {
        Client c = new Client();
        c.setEmail(cr.getEmail());
        c.setPassword(passwordEncoder.encode(cr.getPassword()));
        c.setFirstName(cr.getFirstName());
        c.setLastName(cr.getLastName());
        Address a = new Address(cr.getStreet(), cr.getCity(), cr.getState());
        adressRepository.save(a);
        c.setAddress(a);
        c.setPhoneNumber(cr.getPhoneNumber());
        c.setDeleted(false);
        c.setPoints(0);
        c.setEmailVerified(false);
        c.setPenal(0);
        c.setRole(roleRepository.findByName("CLIENT").get(0));
        clientRepository.save(c);

        String token = UUID.randomUUID().toString();
        confirmationTokenService.createVerificationToken(c, token);
        emailSender.sendConfirmationAsync(cr.getEmail(), token);

        return token;
    }

    @Override
    @Transactional
    public ClientDTO findByEmail(String email) {
        Client client = clientRepository.findByEmail(email);
        if(client == null) return null;
        ClientCategory category = clientCategoryService.findCategoryByReservationPoints(client.getPoints()).get(0);
        ClientDTO dto = new ClientDTO(
                client.getEmail(),
                client.getFirstName(),
                client.getLastName(),
                client.getPhoneNumber(),
                client.getAddress().getStreet(),
                client.getAddress().getCity(),
                client.getAddress().getState(),
                category.getName(),
                client.getPenal(),
                client.getPoints()
        );
        return dto;
    }

    @Override
    @Transactional
    public void updateInfo(String email, ClientDTO dto) throws OnlyLettersAndSpacesException, InvalidPhoneNumberException, InvalidAddressException {
        Client c = clientRepository.findByEmail(email);
        Address address = c.getAddress();
        if(!dto.getFirstName().equals("")){
            if(Validator.onlyLetterAndSpacesValidation(dto.getFirstName())) {
                c.setFirstName(dto.getFirstName());
            }
        }
        if(!dto.getLastName().equals("")){
            if(Validator.onlyLetterAndSpacesValidation(dto.getLastName())){
                c.setLastName(dto.getLastName());
            }
        }
        if(!dto.getPhoneNumber().equals("")){
            if(Validator.phoneNumberValidation(dto.getPhoneNumber())){
                c.setPhoneNumber(dto.getPhoneNumber());
            }
        }
        if(!dto.getStreet().equals("")){
            if(Validator.isValidAdress(dto.getStreet(), address.getCity(), address.getState()))
            {
                address.setStreet(dto.getStreet());
            }
        }
        if(!dto.getCity().equals("")){
            if(Validator.isValidAdress(address.getStreet(), dto.getCity(), address.getState()))
            {
                address.setCity(dto.getCity());
            }
        }
        if(!dto.getState().equals("")){
            if(Validator.isValidAdress(address.getStreet(), address.getCity(), dto.getState())) {
                address.setState(dto.getState());
            }
        }
        clientRepository.save(c);
    }

    @Override
    public void requestAccountDeletion(String email, String reason) throws AccountDeletionException {
        MyUser user = clientRepository.findByEmail(email);
        List<Reservation> reservations = reservationRepository.findClientsUpcomingReservations(user.getId());
//        Optional<DeleteRequest> request = Optional.ofNullable(deleteRequestRepository.alreadyExists(user.getId()));
        if(reservations.isEmpty()){
            deleteRequestRepository.save(new DeleteRequest(reason, user));
        }else{
            throw new AccountDeletionException("Account cannot be deleted.");
        }
    }

    @Override
    public boolean alreadyRequestedDeletion(String email) {
        MyUser user = clientRepository.findByEmail(email);
        Optional<DeleteRequest> request = Optional.ofNullable(deleteRequestRepository.alreadyExists(user.getId()));
        return request.isPresent();
    }

    @Override
    public void removeSubscribedClients(List<Client> services, int offerId, String offerName){
        for(Client c : services){
            c.getSubscribedOffers().removeIf(offer -> Objects.equals(offer.getId(), offerId));
            clientRepository.save(c);
            sendEmail(c.getEmail(), offerName);
        }
    }

    private void sendEmail(String clientEmail, String offerName){
        emailSender.notifyClientDeleteOffer(clientEmail, offerName);
    }
    @Override
    public Boolean canReserve(String email){
        Integer penalties = clientRepository.getPenalties(email);
        return penalties < 3;
    }

    @Override
    public void makeReview(Integer stars, Integer reservationId, String comment, String email) throws Exception {
        Optional<Reservation> r = reservationRepository.findById(reservationId);
        Client c = clientRepository.findByEmail(email);
        Optional<Mark> om = markRepository.alreadyReviewed(c.getId(), reservationId);
        if(om.isPresent()) throw new FeedbackAlreadyGivenException("You have already given the feedback");
        if(r.isPresent()){
            Mark m = new Mark(stars, comment, false, r.get(), c, LocalDate.now(), false);
            markRepository.save(m);
        }else{
            throw new Exception();
        }
    }

    @Override
    @Transactional
    public List<OfferDTO> getSubscriptions(String email) throws IOException {
        List<Offer> offers = clientRepository.getSubscriptions(email);
        List<OfferDTO> subscriptions = new ArrayList<>();
        for(Offer o : offers){
            OfferDTO dto = new OfferDTO(o);
            subscriptions.add(dto);
        }
        return subscriptions;
    }

    @Override
    @Transactional
    public void unsubscribe(String email, String offerId) {
        Integer id = Integer.parseInt(offerId);
        Client c = clientRepository.findByEmail(email);
        Offer o = offerRepository.findOfferById(id);
        c.getSubscribedOffers().removeIf(offer -> Objects.equals(offer.getId(), id));
        o.getSubscribedClients().removeIf(client -> Objects.equals(client.getId(), c.getId()));
        clientRepository.save(c);
        offerRepository.save(o);
    }

    @Override
    @Transactional
    public void subscribe(String email, String offerId) {
        Integer id = Integer.parseInt(offerId);
        Client c = clientRepository.findByEmail(email);
        Offer o = offerRepository.findOfferById(id);
        List<Offer> offers = c.getSubscribedOffers();
        offers.add(o);
        c.setSubscribedOffers(offers);

        List<Client> subs = o.getSubscribedClients();
        subs.add(c);
        o.setSubscribedClients(subs);
        clientRepository.save(c);
        offerRepository.save(o);
    }

    @Override
    @Transactional
    public Boolean isSubscribed(String email, String offerId) {
        Integer id = Integer.parseInt(offerId);
        Offer o = offerRepository.findOfferById(id);
        for(Client c : o.getSubscribedClients())
            if(c.getEmail().equals(email))
                return true;
        return false;
    }

    @Override
    @Transactional
    public void makeComplaint(Integer reservationId, String comment, String email) throws Exception {
        Optional<Reservation> r = reservationRepository.findById(reservationId);
        Client c = clientRepository.findByEmail(email);
        Optional<Complaint> m = Optional.ofNullable(complaintRepository.alreadyReviewed(c.getId(), reservationId));
        if(m.isPresent()) throw new FeedbackAlreadyGivenException("You have already given the feedback");
        if(r.isPresent()){
            Complaint a = new Complaint(comment, r.get(), c, false, LocalDate.now());
            complaintRepository.save(a);
        }else{
            throw new Exception();
        }
    }

    @Override
    @Transactional
    public List<ComplaintDTO> getAllNotDeletedComplaints() {
        List<ComplaintDTO> complaints =  new ArrayList<ComplaintDTO>();
        List<Complaint> notReviewedComplaints = complaintRepository.findAllNotDeleted();
        for(Complaint complaint : notReviewedComplaints){
            complaints.add(createComplaintDTO(complaint));
        }
        return complaints;
    }

    @Override
    @Transactional
    public void respondOnComplaint(String response, int complalintId) throws UserNotFoundException {
        Optional<Complaint> complaint = complaintRepository.findById(complalintId);
        if(complaint.isPresent()){
            Complaint complaintForResponse = complaint.get();
            Client client = complaintForResponse.getClient();
            Reservation reservation = complaintForResponse.getReservation();
            Owner owner = reservationReportService.findReservationOwner(reservation);
            sendComplaintResponseToUsers(owner, client, reservation, response);
            complaintForResponse.setDeleted(true);
            complaintRepository.save(complaintForResponse);

        }
    }

    @Transactional
    public void sendComplaintResponseToUsers(Owner owner, Client client, Reservation reservation, String text) {
        String clientMessage = "Response for complaint on reservation '" + reservation.getOffer().getName() + "' for period:  " +
                reservation.getStartDate().toString() + "-" + reservation.getEndDate() + ":  "  + text;

        String ownerMessage = "Response for complaint on reservation  " +  reservation.getOffer().getName() + "' for period:  " +
                reservation.getStartDate().toString() + " - " + reservation.getEndDate() + ": " + text;

        emailSender.sendResponseOnComplaint(owner.getEmail(), ownerMessage);
        emailSender.sendResponseOnComplaint(client.getEmail(), clientMessage);
    }

    @Scheduled(cron="0 0 0 1 1/1 *")
    @Transactional
    public void removePenalties(){

        clientRepository.removePenalties();
    }

    @Transactional
    public ComplaintDTO createComplaintDTO(Complaint complaint){
        Reservation reservation = complaint.getReservation();
        Client client = reservation.getClient();
        ClientCategory category = categoryRepository.findByMatchingInterval(client.getPoints()).get(0);
        //int id, String text, String offerName, String clientName, String clientCategory, int clientPenalty, String reservationStartDate, String reservationEndDate
        ComplaintDTO dto = new ComplaintDTO(complaint.getId(), complaint.getText(),
                reservation.getOffer().getName(),
                client.getFirstName() + " " + client.getLastName(),
                category.getName(),
                client.getPenal(),
                localDateToString(reservation.getStartDate()),
                localDateToString(reservation.getEndDate()),
                localDateToString(complaint.getRecivedTime())
        );
        return dto;
    }

    @Override
    @Transactional
    public List<UserDTO> getAllActiveClients(int page, int pageSize) {
        Page<Client> clients = clientRepository.findAllActiveUsers(PageRequest.of(page, pageSize));
        int clientNumbers = clientRepository.getNumberOfClients();
        List<UserDTO> userDTOS = new ArrayList<UserDTO>();
        for(Client client : clients.getContent()){
            UserDTO userDTO = createClientDTO(client);
            userDTOS.add(userDTO);
            userDTO.setUserNumber(clientNumbers);
        }
        return userDTOS;
    }

    @Transactional
    public UserDTO createClientDTO(Client client){
        Role role = client.getRole();
        int points = client.getPoints();
        ClientCategory clientCategory = categoryRepository.findByMatchingInterval(points).get(0);
        String category = clientCategory.getName();
        int penalty = client.getPenal();

        UserDTO userDTO = new UserDTO(client, client.getAddress(), role.getName(), category, penalty, points);
        return userDTO;
    }


    private String localDateToString(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return formatter.format(date);
    }

    @Override
    @Transactional
    public void deleteClient(int userId) throws AccountDeletionException {
        Optional<Client> user = clientRepository.findById(userId);
        if(user.isPresent()){
            Client client = user.get();
            List<Reservation> reservations = reservationRepository.findClientsUpcomingReservations(client.getId());
            if(reservations.isEmpty()){
                client.setDeleted(true);
                client.setEmailVerified(false);
                clientRepository.save(client);
                emailSender.notifyUserForDeleteAccount(client.getEmail(), "Your account is deleted by admin");
            }else{
                throw new AccountDeletionException("Account cannot be deleted because user has future reservations.");
            }
        }
    }


}
