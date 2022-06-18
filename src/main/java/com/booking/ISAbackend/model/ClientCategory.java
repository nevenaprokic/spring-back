package com.booking.ISAbackend.model;

import javax.persistence.*;

@Entity
public class ClientCategory {
//	CASUAL_CLIENT,
//    CLOSE_CLIENT,
//    BEST_CLIENT
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="name")
    String name;

    @Column(name="discount")
    Double discount; //popusta za svaku rezervaciju

    @Column
    Integer reservationPoints; //broj poena od svake rezervacije

    @Column
    Integer lowLimitPoints;

    @Column
    Integer heighLimitPoints;

    @Column
    String categoryColor;

    public ClientCategory(Long id, String name, Double discount, Integer reservationPoints, Integer lowLimitPoints, Integer heighLimitPoints) {
        this.id = id;
        this.name = name;
        this.discount = discount;
        this.reservationPoints = reservationPoints;
        this.lowLimitPoints = lowLimitPoints;
        this.heighLimitPoints = heighLimitPoints;
    }

    public ClientCategory(Long id, String name, Double discount, Integer reservationPoints, Integer lowLimitPoints, Integer heighLimitPoints, String categoryColor) {
        this.id = id;
        this.name = name;
        this.discount = discount;
        this.reservationPoints = reservationPoints;
        this.lowLimitPoints = lowLimitPoints;
        this.heighLimitPoints = heighLimitPoints;
        this.categoryColor = categoryColor;
    }

    public ClientCategory() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getDiscount() {
        return discount;
    }

    public Integer getReservationPoints() {
        return reservationPoints;
    }

    public Integer getLowLimitPoints() {
        return lowLimitPoints;
    }

    public Integer getHeighLimitPoints() {
        return heighLimitPoints;
    }

    public String getCategoryColor() { return categoryColor; }

    public void setName(String name) {
        this.name = name;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public void setReservationPoints(Integer reservationPoints) {
        this.reservationPoints = reservationPoints;
    }

    public void setLowLimitPoints(Integer lowLimitPoints) {
        this.lowLimitPoints = lowLimitPoints;
    }

    public void setHeighLimitPoints(Integer heighLimitPoints) {
        this.heighLimitPoints = heighLimitPoints;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }

    public void setId(long id) { this.id = id; }
}
