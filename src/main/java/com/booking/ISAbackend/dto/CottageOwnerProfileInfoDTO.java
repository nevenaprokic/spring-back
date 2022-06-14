package com.booking.ISAbackend.dto;

import com.booking.ISAbackend.model.CottageOwner;
import com.booking.ISAbackend.model.Owner;
import com.booking.ISAbackend.model.OwnerCategory;

public class CottageOwnerProfileInfoDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String street;
    private String city;
    private String state;
    private String userCategory;
    private int points;

    public CottageOwnerProfileInfoDTO(String email, String firstName, String lastName, String phoneNumber, String street, String city, String state, String userCategory) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.userCategory = userCategory;
    }
    public CottageOwnerProfileInfoDTO(CottageOwner cottageOwner, OwnerCategory category) {
        this.email = cottageOwner.getEmail();
        this.firstName = cottageOwner.getFirstName();
        this.lastName = cottageOwner.getLastName();
        this.phoneNumber = cottageOwner.getPhoneNumber();
        this.street = cottageOwner.getAddress().getStreet();
        this.city = cottageOwner.getAddress().getCity();
        this.state = cottageOwner.getAddress().getState();
        this.userCategory = category.getName();
        this.points = cottageOwner.getPoints();
    }
    public CottageOwnerProfileInfoDTO(){}

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
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

    public String getUserCategory() {
        return userCategory;
    }
    public int getPoints() { return points;}


}
