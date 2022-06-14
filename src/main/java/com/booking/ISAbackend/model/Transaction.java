package com.booking.ISAbackend.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double price;
    private LocalDate date;

    @OneToOne
    private Reservation reservation;
}
