package com.booking.ISAbackend.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class NewAdventureDTO {
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
        private String additionalEquipment;

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

        public String getAdditionalEquipment() {
            return additionalEquipment;
        }

        public NewAdventureDTO(String ownerEmail, String offerName, String description, String price, List<MultipartFile> pictures, String peopleNum, String rulesOfConduct, String cancelationConditions, String street, String city, String state, String additionalEquipment) {
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

    public NewAdventureDTO(String offerName, String description, String price, List<MultipartFile> pictures, String peopleNum, String rulesOfConduct, String cancelationConditions, String street, String city, String state, String additionalEquipment) {
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

        public NewAdventureDTO() {

        }
    }

