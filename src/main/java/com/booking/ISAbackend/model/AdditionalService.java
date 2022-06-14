package com.booking.ISAbackend.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class AdditionalService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name" ,nullable = false)
    private String serviceName;
    @Column(name = "price", nullable = false)
    private Double servicePrice;
    @ManyToMany(fetch = FetchType.LAZY, cascade={CascadeType.DETACH,CascadeType.REFRESH})
    private List<Reservation> reservations;

    public AdditionalService() {}

    public AdditionalService(String serviceName, Double servicePrice) {
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return serviceName;
    }

    public Double getPrice() {
        return servicePrice;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setPrice(Double servicePrice) {
        this.servicePrice = servicePrice;
    }
}
