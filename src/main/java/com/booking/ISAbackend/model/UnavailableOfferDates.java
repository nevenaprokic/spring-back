package com.booking.ISAbackend.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class UnavailableOfferDates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id")
    private Offer offer;

    private LocalDate startDate;
    private LocalDate endDate;

    public Integer getId() {
        return id;
    }

    public Offer getOffer() {
        return offer;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public UnavailableOfferDates(Offer offer, LocalDate startDate, LocalDate endDate) {
        this.offer = offer;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public UnavailableOfferDates(){

    }
}
