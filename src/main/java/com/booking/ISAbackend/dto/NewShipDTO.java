package com.booking.ISAbackend.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class NewShipDTO {
    private String ownerEmail;
    private String offerName;
    private String description;
    private String price;
    private List<MultipartFile> photos;
    private String peopleNum;
    private String rulesOfConduct;
    private String cancelationConditions;
    private String street;
    private String city;
    private String state;
    private String type;
    private String size;
    private String motorNumber;
    private String motorPower;
    private String maxSpeed;
    private String additionalEquipment;
    private String navigationEquipment;

    public NewShipDTO() {
    }

    public NewShipDTO(String ownerEmail, String offerName, String description, String price, List<MultipartFile> photos, String peopleNum, String rulesOfConduct, String cancelationConditions, String street, String city, String state, String type, String size, String motorNumber, String motorPower, String maxSpeed, String additionalEquipment, String navigationEquipment) {
        this.ownerEmail = ownerEmail;
        this.offerName = offerName;
        this.description = description;
        this.price = price;
        this.photos = photos;
        this.peopleNum = peopleNum;
        this.rulesOfConduct = rulesOfConduct;
        this.cancelationConditions = cancelationConditions;
        this.street = street;
        this.city = city;
        this.state = state;
        this.type = type;
        this.size = size;
        this.motorNumber = motorNumber;
        this.motorPower = motorPower;
        this.maxSpeed = maxSpeed;
        this.additionalEquipment = additionalEquipment;
        this.navigationEquipment = navigationEquipment;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public String getOfferName() {
        return offerName;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public List<MultipartFile> getPhotos() {
        return photos;
    }

    public String getPeopleNum() {
        return peopleNum;
    }

    public String getRulesOfConduct() {
        return rulesOfConduct;
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

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public String getMotorNumber() {
        return motorNumber;
    }

    public String getMotorPower() {
        return motorPower;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public String getAdditionalEquipment() {
        return additionalEquipment;
    }

    public String getNavigationEquipment() {
        return navigationEquipment;
    }
}
