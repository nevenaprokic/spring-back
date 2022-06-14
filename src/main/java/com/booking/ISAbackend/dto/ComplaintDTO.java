package com.booking.ISAbackend.dto;

public class ComplaintDTO {
    private int id;
    private String text;
    private String offerName;
    private String clientName;
    private String clientCategory;
    private int clientPenalty;
    private String reservationStartDate;
    private String reservationEndDate;
    private String recivedTime;

    public ComplaintDTO(int id, String text, String offerName, String clientName, String clientCategory, int clientPenalty,
                        String reservationStartDate, String reservationEndDate, String recivedTime) {
        this.id = id;
        this.text = text;
        this.offerName = offerName;
        this.clientName = clientName;
        this.clientCategory = clientCategory;
        this.clientPenalty = clientPenalty;
        this.reservationStartDate = reservationStartDate;
        this.reservationEndDate = reservationEndDate;
        this.recivedTime = recivedTime;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getOfferName() {
        return offerName;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientCategory() {
        return clientCategory;
    }

    public int getClientPenalty() {
        return clientPenalty;
    }

    public String getReservationStartDate() {
        return reservationStartDate;
    }

    public String getReservationEndDate() {
        return reservationEndDate;
    }

    public String getRecivedTime(){
        return recivedTime;
    }
}
