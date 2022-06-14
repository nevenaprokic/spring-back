package com.booking.ISAbackend.dto;

public class OfferForReportDTO {
    private String offerName;
    private Integer numberOfReservation;
    private Double totalPrice;

    public OfferForReportDTO(){}

    public OfferForReportDTO(String offerName, Integer numberOgReservation, Double totalPrice) {
        this.offerName = offerName;
        this.numberOfReservation = numberOgReservation;
        this.totalPrice = totalPrice;
    }

    public String getOfferName() {
        return offerName;
    }

    public Integer getNumberOfReservation() {
        return numberOfReservation;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public void setNumberOfReservation(Integer numberOfReservation) {
        this.numberOfReservation = numberOfReservation;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
