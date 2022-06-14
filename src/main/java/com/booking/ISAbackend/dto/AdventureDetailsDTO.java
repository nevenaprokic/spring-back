package com.booking.ISAbackend.dto;

import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.util.List;

public class AdventureDetailsDTO {

    private int id;
    private String ownerEmail;
    private String offerName;
    private String description;
    private String price;
    private List<byte[]> photos;
    private String peopleNum;
    private String rulesOfConduct;
    private List<AdditionalServiceDTO> additionalServices;
    private String cancelationConditions;
    private String street;
    private String city;
    private String state;
    private String additionalEquipment;
    private Double mark;

    public int getId() {
        return id;
    }

    public String getOfferName() {
        return offerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public List<byte[]> getPhotos() {
        return photos;
    }

    public String getPeopleNum() {
        return peopleNum;
    }

    public String getRulesOfConduct() {
        return rulesOfConduct;
    }

    public List<AdditionalServiceDTO> getAdditionalServices() {
        return additionalServices;
    }

    public String getCancelationConditions() {
        return cancelationConditions;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getAdditionalEquipment() {
        return additionalEquipment;
    }

    public AdventureDetailsDTO(int id, String ownerEmail, String offerName, String description, String price, List<byte[]> pictures, String peopleNum, String rulesOfConduct, List<AdditionalServiceDTO> additionalServices, String cancelationConditions, String street, String city, String state, String additionalEquipment) {
        this.id = id;
        this.ownerEmail = ownerEmail;
        this.offerName = offerName;
        this.description = description;
        this.price = price;
        this.photos = pictures;
        this.peopleNum = peopleNum;
        this.rulesOfConduct = rulesOfConduct;
        this.additionalServices = additionalServices;
        this.cancelationConditions = cancelationConditions;
        this.street = street;
        this.city = city;
        this.state = state;
        this.additionalEquipment = additionalEquipment;
    }

    public AdventureDetailsDTO(int id, String ownerEmail, String offerName, String description, String price, List<byte[]> pictures, String peopleNum, String rulesOfConduct, String cancelationConditions, String street, String city, String state, String additionalEquipment) {
        this.id = id;
        this.ownerEmail = ownerEmail;
        this.offerName = offerName;
        this.description = description;
        this.price = price;
        this.photos = pictures;
        this.peopleNum = peopleNum;
        this.rulesOfConduct = rulesOfConduct;
        this.cancelationConditions = cancelationConditions;
        this.street = street;
        this.city = city;
        this.state = state;
        this.additionalEquipment = additionalEquipment;
    }


    public AdventureDetailsDTO(){

    }

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
    }
}
