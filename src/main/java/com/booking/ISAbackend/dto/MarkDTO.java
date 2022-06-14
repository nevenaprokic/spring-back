package com.booking.ISAbackend.dto;

import com.booking.ISAbackend.model.Mark;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MarkDTO {
    private Integer id;
    private Integer mark;
    private String comment;
    private Boolean approved;
    private ReservationDTO reservationDTO;
    private String sendingTime;

    public MarkDTO(Integer mark, String comment, Boolean approved, ReservationDTO reservationDTO, String sendingTime) {
        this.sendingTime = sendingTime;
        this.mark = mark;
        this.comment = comment;
        this.approved = approved;
        this.reservationDTO = reservationDTO;
    }

    public MarkDTO(Mark mark, ReservationDTO reservationDTO) {
        this.mark = mark.getMark();
        this.comment = mark.getComment();
        this.approved = mark.getApproved();
        this.reservationDTO = reservationDTO;
        this.sendingTime = localDateToString(mark.getSendingTime());
        this.id = mark.getId();
    }

    public Integer getMark() {
        return mark;
    }

    public String getComment() {
        return comment;
    }

    public Boolean getApproved() {
        return approved;
    }

    public ReservationDTO getReservationDTO() {
        return reservationDTO;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public void setReservationDTO(ReservationDTO reservationDTO) {
        this.reservationDTO = reservationDTO;
    }

    public void setSendingTime(String sendingTime){this.sendingTime = sendingTime;}
    public String getSendingTime(){return  sendingTime;}

    private String localDateToString(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return formatter.format(date);
    }
    public int getId(){ return  id;}

}

