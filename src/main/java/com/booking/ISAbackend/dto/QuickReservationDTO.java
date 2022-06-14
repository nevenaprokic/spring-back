package com.booking.ISAbackend.dto;

import com.booking.ISAbackend.model.AdditionalService;
import com.booking.ISAbackend.model.QuickReservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class QuickReservationDTO {
    private Integer id;
    private LocalDate startDate;
    private LocalDate endDateAction;
    private LocalDate startDateAction;
    private LocalDate endDate;
    private List<String> additionalServices;
    private Double price;
    private Integer numberOfPerson;
    private String OfferName;
    private String offerPhoto;
    private String startDateStr;
    private String endDateActionStr;
    private String startDateActionStr;
    private String endDateStr;
    public QuickReservationDTO(){}

    public QuickReservationDTO(LocalDate startDate, LocalDate endDateAction, LocalDate startDateAction, LocalDate endDate, List<String> additionalServices, Double price, Integer numberOfPerson) {
        this.startDate = startDate;
        this.endDateAction = endDateAction;
        this.startDateAction = startDateAction;
        this.endDate = endDate;
        this.additionalServices = additionalServices;
        this.price = price;
        this.numberOfPerson = numberOfPerson;
    }
    public QuickReservationDTO(QuickReservation res) {
        this.id = res.getId();
        this.startDate = res.getStartDate();
        this.endDateAction = res.getEndDateAction();
        this.startDateAction = res.getStartDateAction();
        this.endDate = res.getEndDate();
        this.additionalServices = getAdditionalServices(res.getAdditionalServices());
        this.price = res.getPrice();
        this.numberOfPerson = res.getNumberOfPerson();

    }
    private List<String> getAdditionalServices(List<AdditionalService> services){
        List<String> additionalServices = new ArrayList<>();
        for(AdditionalService ad: services){
            additionalServices.add(ad.getName());
        }
        return additionalServices;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDateAction() {
        return endDateAction;
    }

    public LocalDate getStartDateAction() {
        return startDateAction;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public List<String> getAdditionalServices() {
        return additionalServices;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getNumberOfPerson() {
        return numberOfPerson;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDateAction(LocalDate endDateAction) {
        this.endDateAction = endDateAction;
    }

    public void setStartDateAction(LocalDate startDateAction) {
        this.startDateAction = startDateAction;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setAdditionalServices(List<String> additionalServices) {
        this.additionalServices = additionalServices;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setNumberOfPerson(Integer numberOfPerson) {
        this.numberOfPerson = numberOfPerson;
    }

    public String getOfferName(){return this.OfferName;}

    public void setOfferName(String offerName) {this.OfferName = offerName;}
    public void setOfferPhoto(String offerPhoto) {this.offerPhoto = offerPhoto;}
    public String getOfferPhoto() {return this.offerPhoto;}

    public String getStartDateStr() {
        return startDateStr;
    }

    public String getEndDateActionStr() {
        return endDateActionStr;
    }

    public String getStartDateActionStr() {
        return startDateActionStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public void setEndDateActionStr(String endDateActionStr) {
        this.endDateActionStr = endDateActionStr;
    }

    public void setStartDateActionStr(String startDateActionStr) {
        this.startDateActionStr = startDateActionStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }
}
