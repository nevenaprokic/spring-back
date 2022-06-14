package com.booking.ISAbackend.dto;

import com.booking.ISAbackend.model.AdditionalService;

import java.time.LocalDate;
import java.util.List;

public class ReservationParamsDTO {

    private LocalDate date;
    private LocalDate endingDate;
    private String email;
    private Integer guests;
    private List<AdditionalService> services;
    private Double total;
    private Integer offerId;
    private Integer actionId;

    public ReservationParamsDTO() {}

    public ReservationParamsDTO(LocalDate date, LocalDate endingDate, String email, Integer guests, List<AdditionalService> services, Double total) {
        this.date = date;
        this.endingDate = endingDate;
        this.email = email;
        this.guests = guests;
        this.services = services;
        this.total = total;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDate getEndingDate() {
        return endingDate;
    }

    public String getEmail() {
        return email;
    }

    public Integer getGuests() {
        return guests;
    }

    public List<AdditionalService> getServices() {
        return services;
    }

    public Double getTotal() {
        return total;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setEndingDate(LocalDate endingDate) {
        this.endingDate = endingDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGuests(Integer guests) {
        this.guests = guests;
    }

    public void setServices(List<AdditionalService> services) {
        this.services = services;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getOfferId() {
        return offerId;
    }

    public void setOfferId(Integer offerId) {
        this.offerId = offerId;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }
}
