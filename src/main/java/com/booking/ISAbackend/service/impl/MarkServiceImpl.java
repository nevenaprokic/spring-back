package com.booking.ISAbackend.service.impl;

import com.booking.ISAbackend.dto.MarkDTO;
import com.booking.ISAbackend.dto.ReservationDTO;
import com.booking.ISAbackend.email.EmailService;
import com.booking.ISAbackend.exceptions.UserNotFoundException;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.repository.*;
import com.booking.ISAbackend.service.MarkService;
import com.booking.ISAbackend.service.ReservationReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MarkServiceImpl implements MarkService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MarkRepository markRepository;
    @Autowired
    private CottageRepository cottageRepository;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private AdventureReporitory adventureReporitory;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ReservationReportService reservationReportService;

    @Override
    public Double getMark(Integer idOffer){
        List<Mark> marks = markRepository.findAllMarkByOfferId(idOffer);
        Double mark = 0.0;
        for(Mark m: marks) {
            mark += m.getMark();
        }
        if(marks.size() > 0)
            mark /= marks.size();
        return mark;
    }

    @Override
    public Double getMarkByCottageOwnerEmail(String email){
        List<Cottage> cottages = cottageRepository.findCottageByCottageOwnerEmail(email);
        Double mark = 0.0;
        for(Cottage c: cottages){
            mark += getMark(c.getId());
        }
        if(cottages.size() > 0 && mark > 0.0)
            mark /= cottages.size();
        return mark;
    }

    @Override
    public Double getMarkByShipOwnerEmail(String email) {
        List<Ship> ships = shipRepository.findShipByShipOwnerEmail(email);
        Double mark = 0.0;
        for(Ship s: ships){
            mark += getMark(s.getId());
        }
        if(ships.size() > 0 && mark > 0.0)
            mark /= ships.size();
        return mark;
    }

    @Override
    public Double getMarkByInstructorEmail(String email) {
        List<Adventure> adventures = adventureReporitory.findAdventureByInstructorEmail(email);
        Double mark = 0.0;
        for(Adventure a: adventures){
            mark += getMark(a.getId());
        }
        if(adventures.size() > 0 && mark > 0.0)
            mark /= adventures.size();
        return mark;
    }

    @Override
    @Transactional
    public List<MarkDTO> getAllUncheckesMarks() throws IOException {
        List<Mark> notApprovedMarks = markRepository.findAllNotApproved(Sort.by(Sort.Direction.DESC, "sendingTime"));
        List<MarkDTO> marksData = new ArrayList<MarkDTO>();
        for(Mark mark : notApprovedMarks){
            Optional<Reservation> reservation = reservationRepository.findById(mark.getId());
            if(reservation.isPresent()){
                Reservation r = reservation.get();
                ReservationDTO reservationDTO = new ReservationDTO(r);
                MarkDTO data = new MarkDTO(mark, reservationDTO);
                marksData.add(data);
            }

        }
        return marksData;
    }

    @Override
    @Transactional
    public void acceptMark(int markId) throws UserNotFoundException {
        Optional<Mark> mark = markRepository.findById(markId);
        if(mark.isPresent()){
            Mark m = mark.get();
            m.setApproved(true);
            m.setReviewed(true);
            Reservation reservation = m.getReservation();
            Owner owner = reservationReportService.findReservationOwner(reservation);
            String message = "You have received new review for reservation " + reservation.getOffer().getName() +
                    " from " + reservation.getStartDate().toString() +
                    " to " + reservation.getEndDate().toString() +
                    "\r\n\" + Received review: " + m.getComment();
            sendEmailNotification(owner, message);
            markRepository.save(m);
        }
    }

    @Override
    @Transactional
    public void discardMark(int markId) {
        Optional<Mark> mark = markRepository.findById(markId);
        if(mark.isPresent()){
            Mark m = mark.get();
            m.setReviewed(true);
            markRepository.save(m);
            Reservation reservation = m.getReservation();
            Client client = reservation.getClient();
            String message = "Your review for reservation " + reservation.getOffer().getName() +
                    " from " + reservation.getStartDate().toString() +
                    " to " + reservation.getEndDate().toString() +
                    " has been rejected.";
            sendEmailNotification(client, message);
        }
    }

    @Override
    @Transactional
    public List<MarkDTO> getAllMarksForOffer(int offerId) throws IOException {
        List<Mark> offerMarks = markRepository.findAllMarkByOfferId(offerId);
        List<MarkDTO> marks = new ArrayList<MarkDTO>();
        for( Mark mark : offerMarks){
             Reservation r = mark.getReservation();
             MarkDTO dto = new MarkDTO(mark, new ReservationDTO(r));
             marks.add(dto);
        }
        return marks;
    }

    @Transactional
    public void sendEmailNotification(MyUser user, String message) {

        emailService.notifyUserAboutMark(user.getEmail(), message);
    }
}
