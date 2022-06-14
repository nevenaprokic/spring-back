package com.booking.ISAbackend.dto;

public class CottageOwnerNewDataDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String street;
    private String state;
    private String city;

    public CottageOwnerNewDataDTO(String email, String firstName, String lastName, String phoneNumber, String street, String state, String city) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.state = state;
        this.city = city;
    }
    public CottageOwnerNewDataDTO(){}

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

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }
}
