package com.booking.ISAbackend.dto;

import com.booking.ISAbackend.model.AdditionalService;
import com.booking.ISAbackend.model.QuickReservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NewQuickReservationDTO {
    private String name;
    private Integer offerId;
    private Integer daysAction;
    private Integer daysReservation;
    private String startDateAction;
    private String startDateReservation;
    private Double price;
    private Integer peopleNum;

    public NewQuickReservationDTO(){}


    public NewQuickReservationDTO(String name, Integer offerId, Integer daysAction, Integer daysReservation, String startDateAction, String startDateReservation, Double price, Integer peopleNum) {
        this.name = name;
        this.offerId = offerId;
        this.daysAction = daysAction;
        this.daysReservation = daysReservation;
        this.startDateAction = startDateAction;
        this.startDateReservation = startDateReservation;
        this.price = price;
        this.peopleNum = peopleNum;
    }

    public String getName() {
        return name;
    }

    public Integer getOfferId() {
        return offerId;
    }

    public Integer getDaysAction() {
        return daysAction;
    }

    public Integer getDaysReservation() {
        return daysReservation;
    }

    public String getStartDateAction() {
        return startDateAction;
    }

    public String getStartDateReservation() {
        return startDateReservation;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }
}
