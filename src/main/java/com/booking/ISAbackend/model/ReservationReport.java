package com.booking.ISAbackend.model;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

@Entity
public class ReservationReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private Boolean penalOption;

	@Column(nullable = false)
	private Boolean automaticallyPenal;

	private String comment;
	@OneToOne
	private Reservation reservation;

	private Boolean reviewed;

	private LocalDate sentDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id")
	private Client client;

	@Version
	@Column(name = "optlock", columnDefinition = "integer DEFAULT 0", nullable = false)
	private Long version;

	public ReservationReport(){

	}


	public ReservationReport(Boolean penalOption, Boolean automaticallyPenal, String comment, Reservation reservation, Client client) {
		this.penalOption = penalOption;
		this.automaticallyPenal = automaticallyPenal;
		this.comment = comment;
		this.reservation = reservation;
		this.client = client;
	}

	public Integer getId() {
		return id;
	}

	public Boolean getPenalOption() {
		return penalOption;
	}

	public Boolean getAutomaticallyPenal() {
		return automaticallyPenal;
	}

	public String getComment() {
		return comment;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public Client getClient() {
		return client;
	}

	public Boolean getReviewed(){ return reviewed;}
	public void setReviewed(boolean reviewed) {this.reviewed = reviewed;}

	public LocalDate getSentDate() {return  sentDate;}

	public void setSentDate(LocalDate sentDate) { this.sentDate = sentDate;}

	public void setPenalOption(Boolean penalOption) {
		this.penalOption = penalOption;
	}

	public void setAutomaticallyPenal(Boolean automaticallyPenal) {
		this.automaticallyPenal = automaticallyPenal;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public void setReviewed(Boolean reviewed) {
		this.reviewed = reviewed;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
