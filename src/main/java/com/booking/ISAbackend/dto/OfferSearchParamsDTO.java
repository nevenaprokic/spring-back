package com.booking.ISAbackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class OfferSearchParamsDTO {

    private String firstName;
    private String lastName;
    private String name;
    private String description;
    private String address;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;

    public OfferSearchParamsDTO(){}

    public OfferSearchParamsDTO(String name, String description, String address, LocalDate dateFrom, LocalDate dateTo) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.date = dateFrom;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
