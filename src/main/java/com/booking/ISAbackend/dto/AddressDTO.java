package com.booking.ISAbackend.dto;

import com.booking.ISAbackend.model.Address;

public class AddressDTO {

    private String city;
    private String state;
    private String street;

    public AddressDTO(String street, String city, String state) {
        this.street = street;
        this.city = city;
        this.state = state;
    }
    public AddressDTO(Address ad) {
        this.street = ad.getStreet();
        this.city = ad.getCity();
        this.state = ad.getState();
    }


    public AddressDTO() {

    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getStreet() {
        return street;
    }
}
