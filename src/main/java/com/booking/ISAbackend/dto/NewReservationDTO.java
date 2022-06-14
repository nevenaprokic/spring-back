package com.booking.ISAbackend.dto;

import com.booking.ISAbackend.model.AdditionalService;

import java.util.List;

public class NewReservationDTO {
    private String clientUserName;
    private String clientName;
    private String clientLastName;
    private String offerName;
    private Integer offerId;
    private Integer daysReservation;
    private String startDateReservation;
    private Integer peopleNum;
    private Double price;
    private List<AdditionalService> services;

    public NewReservationDTO(){}

    public NewReservationDTO(String clientUserName, String clientName, String clientLastName, String offerName, Integer offerId, Integer daysReservation, String startDateReservation, Integer peopleNum, Double price, List<AdditionalService> services) {
        this.clientUserName = clientUserName;
        this.clientName = clientName;
        this.clientLastName = clientLastName;
        this.offerName = offerName;
        this.offerId = offerId;
        this.daysReservation = daysReservation;
        this.startDateReservation = startDateReservation;
        this.peopleNum = peopleNum;
        this.price = price;
        this.services = services;
    }

    public String getClientUserName() {
        return clientUserName;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public String getOfferName() {
        return offerName;
    }

    public Integer getOfferId() {
        return offerId;
    }

    public Integer getDaysReservation() {
        return daysReservation;
    }

    public String getStartDateReservation() {
        return startDateReservation;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public Double getPrice() {
        return price;
    }

    public List<AdditionalService> getServices() {
        return services;
    }
}
