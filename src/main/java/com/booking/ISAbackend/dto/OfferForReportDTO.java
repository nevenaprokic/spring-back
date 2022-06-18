package com.booking.ISAbackend.dto;

public class OfferForReportDTO {
    private String offerName;
    private Integer numberOfReservation;
    private Double totalPrice;
    private Double realPrice;
    private double earningPercent;

    public OfferForReportDTO(){}

    public OfferForReportDTO(String offerName, Integer numberOgReservation, Double totalPrice) {
        this.offerName = offerName;
        this.numberOfReservation = numberOgReservation;
        this.totalPrice = totalPrice;
    }

    public OfferForReportDTO(String offerName, Integer numberOgReservation, Double totalPrice, Double realPrice, Double earningPercent) {
        this.offerName = offerName;
        this.numberOfReservation = numberOgReservation;
        this.totalPrice = totalPrice;
        this.realPrice = realPrice;
        this.earningPercent = earningPercent;
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

    public Double getRealPrice() {
        return realPrice;
    }

    public double getEarningPercent() {
        return earningPercent;
    }

    public void setRealPrice(Double realPrice) {
        this.realPrice = realPrice;
    }

    public void setEarningPercent(double earningPercent) {
        this.earningPercent = earningPercent;
    }
}
