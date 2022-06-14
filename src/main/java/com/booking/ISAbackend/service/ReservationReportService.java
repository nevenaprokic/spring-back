package com.booking.ISAbackend.service;

import com.booking.ISAbackend.dto.NewReservationReportDTO;
import com.booking.ISAbackend.dto.OfferForReportDTO;
import com.booking.ISAbackend.dto.ReservationReportAdminDTO;
import com.booking.ISAbackend.exceptions.UserNotFoundException;
import com.booking.ISAbackend.model.Owner;
import com.booking.ISAbackend.model.Reservation;
import com.booking.ISAbackend.model.ReservationReport;

import java.io.IOException;
import java.util.List;

public interface ReservationReportService {
    List<Integer> getReservationReportCottageOwner(String ownerEmail);
    List<Integer> getReservationReportShipOwner(String ownerEmail);
    void addReservationReport(NewReservationReportDTO dto);
    List<OfferForReportDTO> getReportIncomeStatementCottage(String start, String end, String email);
    List<OfferForReportDTO> getReportIncomeStatementShip(String start, String end, String email);
    List<OfferForReportDTO> getReportIncomeStatementAdventure(String start, String end, String email);
    List<Integer> getNotReportedReservationsInstructor(String ownerEmail);
    List<ReservationReportAdminDTO> getAllNotReviewedWIthPenaltyOption() throws IOException;
    void addPenaltyToClient(Integer reportId) throws UserNotFoundException;

    void rejectPenaltyOption(Integer reportId) throws UserNotFoundException;
    Owner findReservationOwner(Reservation reservation) throws UserNotFoundException;
}
