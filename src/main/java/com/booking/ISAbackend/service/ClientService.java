package com.booking.ISAbackend.service;

import com.booking.ISAbackend.dto.*;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.Client;
import com.booking.ISAbackend.model.Complaint;

import java.io.IOException;
import java.util.List;

public interface ClientService {

    String save(ClientRequest c) throws InterruptedException;
    ClientDTO findByEmail(String email);
    void updateInfo(String email, ClientDTO dto) throws OnlyLettersAndSpacesException, InvalidPhoneNumberException, InvalidAddressException;
    void requestAccountDeletion(String email, String reason) throws AccountDeletionException;
    boolean alreadyRequestedDeletion(String email);
    void removeSubscribedClients(List<Client> services,int offerId, String offerName);
    Boolean canReserve(String email);

    void makeReview(Integer stars, Integer offerId, String comment, String email) throws Exception;

    List<OfferDTO> getSubscriptions(String email) throws IOException;

    void unsubscribe(String email, String offerId);

    void subscribe(String email, String offerId);

    Boolean isSubscribed(String email, String offerId);

    void makeComplaint(Integer reservationId, String comment, String email) throws Exception;

    List<ComplaintDTO> getAllNotDeletedComplaints();

    void respondOnComplaint(String response, int complalintId) throws UserNotFoundException;

    List<UserDTO> getAllActiveClients(int startId, int endId);

    void deleteClient(int userId) throws AccountDeletionException;
}
