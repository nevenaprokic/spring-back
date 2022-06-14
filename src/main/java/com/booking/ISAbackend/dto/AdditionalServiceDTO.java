package com.booking.ISAbackend.dto;

import com.booking.ISAbackend.model.AdditionalService;

public class AdditionalServiceDTO {
    private Integer id;
    private String serviceName;
    private Double servicePrice;

    public AdditionalServiceDTO(Integer id, String name, Double price) {
        this.id = id;
        this.serviceName = name;
        this.servicePrice = price;
    }
    public AdditionalServiceDTO(AdditionalService a) {
        this.id = a.getId();
        this.serviceName = a.getName();
        this.servicePrice = a.getPrice();
    }

    public AdditionalServiceDTO(){}

    public AdditionalServiceDTO(String name, Double price) {
        this.serviceName = name;
        this.servicePrice = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Double getServicePrice() {
        return servicePrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServicePrice(Double servicePrice) {
        this.servicePrice = servicePrice;
    }
}
