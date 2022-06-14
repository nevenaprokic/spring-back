package com.booking.ISAbackend.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Complaint {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "my_user_id")
	private Client client;

	@Column(nullable = false)
	private String text;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;

	private boolean deleted;

	private LocalDate recivedTime;

	@Version
	@Column(name = "optlock", columnDefinition = "integer DEFAULT 0", nullable = false)
	private Long version;

	public Complaint() {}

	public Complaint(String text, Reservation reservation, Client client, boolean deleted, LocalDate recivedTime) {
		this.text = text;
		this.reservation = reservation;
		this.client = client;
		this.deleted = deleted;
		this.recivedTime = recivedTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public boolean isDeleted() {return deleted;}

	public void setDeleted(boolean deleted) { this.deleted = deleted; }

	public Client getClient() {return client;}

	public LocalDate getRecivedTime() {return recivedTime;}
}
