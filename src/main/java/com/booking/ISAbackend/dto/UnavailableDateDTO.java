package com.booking.ISAbackend.dto;

public class UnavailableDateDTO {
    private int offerId;
    private String startDate;
    private String endDate;

    public UnavailableDateDTO(int offerId, String startDate, String endDate) {
        this.offerId = offerId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public UnavailableDateDTO(){

    }

    public int getOfferId() {
        return offerId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
