package com.booking.ISAbackend.dto;

public class UserProfileData {
    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;


    private String street;
    private String city;
    private String state;

    private boolean emailVerified;

    public UserProfileData(String email, String firstName, String lastName, String phoneNumber, String street, String city, String state) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.city = city;
        this.state = state;
    }
    public UserProfileData(){

    }

    public UserProfileData(String email, String firstName, String lastName, String phoneNumber, String street, String city, String state, boolean emailVerified) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.emailVerified = emailVerified;
    }

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

    public boolean isEmailVerified() {return emailVerified;}

}

