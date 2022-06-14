package com.booking.ISAbackend.model;

import javax.persistence.*;

@Entity
public class OwnerCategory {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="name")
    String name;

    @Column(name="earnings")
    Double earningsPercent;

    @Column
    Integer reservationPoints; //zarada sistema od svake rezervacije ce biti 100- ovaj procenat

    @Column
    Integer lowLimitPoints;  //treba proveravati da se ne poklapa sa drugim kategorijama

    @Column
    Integer heighLimitPoints; //treba proveravati da se ne poklapa sa drugim kategorijama

    @Column
    String categoryColor;

    public OwnerCategory(Long id, String name, Double earningsPercent, Integer reservationPoints, Integer lowLimitPoints, Integer heighLimitPoints) {
        this.id = id;
        this.name = name;
        this.earningsPercent = earningsPercent;
        this.reservationPoints = reservationPoints;
        this.lowLimitPoints = lowLimitPoints;
        this.heighLimitPoints = heighLimitPoints;
    }

    public OwnerCategory(Long id, String name, Double earningsPercent, Integer reservationPoints, Integer lowLimitPoints, Integer heighLimitPoints, String categoryColor) {
        this.id = id;
        this.name = name;
        this.earningsPercent = earningsPercent;
        this.reservationPoints = reservationPoints;
        this.lowLimitPoints = lowLimitPoints;
        this.heighLimitPoints = heighLimitPoints;
        this.categoryColor = categoryColor;
    }

    public OwnerCategory() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getEarningsPercent() {
        return earningsPercent;
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

    public void setHeighLimitPoints(int points) {
        this.heighLimitPoints = points;
    }

    public void setLowLimitPoints(int points){
        this.lowLimitPoints = points;
    }

    //    REGULAR,
//    SILVER,
//    GOLD;

//    public static OwnerCategory fromInteger(int x) {
//        switch(x) {
//            case 1:
//                return REGULAR;
//            case 2:
//                return SILVER;
//            default:
//                return REGULAR;
//        }

    }

