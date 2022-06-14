package com.booking.ISAbackend.dto;

import com.booking.ISAbackend.model.Client;
import com.booking.ISAbackend.model.Reservation;
import com.booking.ISAbackend.model.ReservationReport;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReservationReportAdminDTO {
    private String comment;
    private int reportId;
    private String reportSentDate;
    private ReservationDTO reservationDTO;
    private ClientDTO clientDTO;

    public ReservationReportAdminDTO(ReservationReport report, ReservationDTO reservationDTO, ClientDTO clientDTO) {
        reportId = report.getId();
        comment = report.getComment();
        reportSentDate = localDateToString(report.getSentDate());
        this.reservationDTO = reservationDTO;
        this.clientDTO = clientDTO;

    }
    public String getComment() {
        return comment;
    }

    public int getReportId() {
        return reportId;
    }

    public String getReportSentDate() {
        return reportSentDate;
    }

    public ReservationDTO getReservationDTO() {return reservationDTO;}

    public void setReservationDTO(ReservationDTO dto) {reservationDTO = dto;}

    public ClientDTO getClientDTO() {return clientDTO;}


    private String localDateToString(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return formatter.format(date);
    }

}
