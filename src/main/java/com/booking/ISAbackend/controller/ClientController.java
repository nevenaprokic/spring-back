package com.booking.ISAbackend.controller;

import com.booking.ISAbackend.dto.ClientDTO;
import com.booking.ISAbackend.dto.ClientRequest;
import com.booking.ISAbackend.dto.OfferDTO;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.Offer;
import com.booking.ISAbackend.service.ClientService;
import com.booking.ISAbackend.model.Client;
import com.booking.ISAbackend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("client")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private ReservationService reservationService;

    @PostMapping("registration")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<String> addClient(@RequestBody ClientRequest request, UriComponentsBuilder ucBuilder) throws InterruptedException {

        ClientDTO existUser = this.clientService.findByEmail(request.getEmail());

        if (existUser != null) {
            return ResponseEntity.status(400).body("Email is already registered.");
        }

        String token = this.clientService.save(request);

        return ResponseEntity.ok("Registration was successful.");
    }

    @GetMapping("profile-info/{email}")
    @PreAuthorize("hasAnyAuthority('CLIENT','COTTAGE_OWNER','INSTRUCTOR','SHIP_OWNER')")
    public ResponseEntity<ClientDTO> getClientProfileInfo(@PathVariable("email") String email){
        ClientDTO dto = clientService.findByEmail(email);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("update-profile-info")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<String> updateInfo(@RequestParam String email, @RequestBody ClientDTO dto) {
        try{
            clientService.updateInfo(email, dto);
            return ResponseEntity.ok("Successfully updated personal info");
        } catch (OnlyLettersAndSpacesException | InvalidPhoneNumberException | InvalidAddressException e) {

            return ResponseEntity.status(400).body("Data is invalid.");
        }
    }

    @PostMapping("delete-account")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<String> deleteAccount(@RequestBody HashMap<String, String> data){
        try{
            clientService.requestAccountDeletion(data.get("email"), data.get("reason"));
            return ResponseEntity.ok("Successfully created request for deleting account.");
        } catch (AccountDeletionException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("deletion-requested")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Boolean> alreadyRequestedDeletion(@RequestParam String email){
        return ResponseEntity.ok(clientService.alreadyRequestedDeletion(email));
    }

    @GetMapping("get-by-reservation-cottage")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<List<ClientDTO>> getClientByCottageOwnerEmail(@RequestParam String ownerEmail){
        try{
            List<ClientDTO> clients = reservationService.getClientByCottageOwnerEmail(ownerEmail);
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("get-by-reservation-ship")
    @PreAuthorize("hasAuthority('SHIP_OWNER')")
    public ResponseEntity<List<ClientDTO>> getClientByShipOwnerEmail(@RequestParam String ownerEmail){
        try{
            List<ClientDTO> clients = reservationService.getClientByShipOwnerEmail(ownerEmail);
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("get-by-reservation-adventure")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<List<ClientDTO>> getClientByInstructorEmail(@RequestParam String ownerEmail){
        try{
            List<ClientDTO> clients = reservationService.getClientByInstructorEmail(ownerEmail);
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("available-client")
    @PreAuthorize("hasAnyAuthority('CLIENT','COTTAGE_OWNER','INSTRUCTOR','SHIP_OWNER')")
    public ResponseEntity<Boolean> isAvailableClient(@RequestParam String emailClient, @RequestParam String startDateReservation, @RequestParam String endDateReservation){
        try{
            Boolean check = reservationService.isAvailableClient(emailClient, startDateReservation, endDateReservation);
            return ResponseEntity.ok(check);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("is-allowed-to-reserve")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Boolean> canReserve(@RequestParam String email) {
        try{
            Boolean check = clientService.canReserve(email);
            return ResponseEntity.ok(check);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PutMapping("make-review")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<String> makeReview(@RequestBody HashMap<String, String> data) {
        try{
            Integer reservationId = Integer.parseInt(data.get("reservationId"));
            Integer stars = Integer.parseInt(data.get("stars"));
            clientService.makeReview(stars, reservationId, data.get("comment"), data.get("email"));
            return new ResponseEntity<String>("Review has been successfully added.", HttpStatus.CREATED);
        }catch (FeedbackAlreadyGivenException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Something went wrong.");
        }
    }

    @GetMapping("get-subscriptions/{email}")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<OfferDTO>> getSubscriptions(@PathVariable String email){
        try{
            List<OfferDTO> subscriptions = clientService.getSubscriptions(email);
            return ResponseEntity.ok(subscriptions);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PostMapping("unsubscribe")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<String> unsubscribe(@RequestBody HashMap<String, String> data){
        try{
            clientService.unsubscribe(data.get("email"), data.get("offerId"));
            return ResponseEntity.ok("Successfully unsubscribed");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Something went wrong");
        }
    }

    @PostMapping("subscribe")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<String> subscribe(@RequestBody HashMap<String, String> data){
        try{
            clientService.subscribe(data.get("email"), data.get("offerId"));
            return ResponseEntity.ok("Successfully subscribed");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Something went wrong");
        }
    }

    @GetMapping("is-subscribed")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Boolean> isSubscribed(@RequestParam String email, @RequestParam String offerId){
        try{
            if (clientService.isSubscribed(email, offerId))
                return ResponseEntity.ok(true);
            return ResponseEntity.ok(false);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PutMapping("make-complaint")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<String> makeComplaint(@RequestBody HashMap<String, String> data) {
        try{
            Integer reservationId = Integer.parseInt(data.get("reservationId"));
            clientService.makeComplaint(reservationId, data.get("comment"), data.get("email"));
            //return ResponseEntity.ok("Complaint has been successfully added.");
            return new ResponseEntity<String>("Complaint has been successfully added.", HttpStatus.CREATED);
        }catch (FeedbackAlreadyGivenException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Something went wrong.");
        }
    }
}
