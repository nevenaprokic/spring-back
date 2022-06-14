package com.booking.ISAbackend.dto;

import com.booking.ISAbackend.model.Instructor;
import com.booking.ISAbackend.model.OwnerCategory;

import java.util.List;

public class InstructorProfileData {

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String street;

    private String city;

    private String state;

    private String userCategory;

    private String biography;
    private int points;
    private Double mark;

    private List<AdventureDTO> adventures;

    public InstructorProfileData(String email, String firstName, String lastName, String phoneNumber, String street, String city, String state, String userCategory, String biography, Integer points) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.userCategory = userCategory;
        this.biography = biography;
        this.points = points;
    }

    public InstructorProfileData(Instructor i, OwnerCategory category) {
        this.email = i.getEmail();
        this.firstName = i.getFirstName();
        this.lastName = i.getLastName();
        this.phoneNumber = i.getPhoneNumber();
        this.street = i.getAddress().getStreet();
        this.city = i.getAddress().getCity();
        this.state = i.getAddress().getState();
        this.userCategory = category.getName();
        this.biography = i.getBiography();
        this.points = i.getPoints();
    }

    public InstructorProfileData(Instructor i) {
        this.email = i.getEmail();
        this.firstName = i.getFirstName();
        this.lastName = i.getLastName();
        this.phoneNumber = i.getPhoneNumber();
        this.street = i.getAddress().getStreet();
        this.city = i.getAddress().getCity();
        this.state = i.getAddress().getState();
        this.biography = i.getBiography();
        this.points = i.getPoints();
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

    public String getUserCategory() {
        return userCategory;
    }

    public String getBiography() {
        return biography;
    }

    public List<AdventureDTO> getAdventures() {
        return adventures;
    }

    public void setAdventures(List<AdventureDTO> adventures) {
        this.adventures = adventures;
    }


    public int getPoints() { return points;}

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
    }
}
