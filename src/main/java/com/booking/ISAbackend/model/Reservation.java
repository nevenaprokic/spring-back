package com.booking.ISAbackend.model;

import com.booking.ISAbackend.model.Client;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.*;

@Entity
@SQLDelete(sql = "UPDATE reservation SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private LocalDate startDate;

	@Column(nullable = false)
	private LocalDate endDate;

	@ManyToMany(fetch = FetchType.LAZY, cascade={CascadeType.DETACH,CascadeType.REFRESH})
	private List<AdditionalService> additionalServices;

	@Column(nullable = false)
	private Double price;

	@Column(nullable = false)
	private Integer numberOfPerson;

	@Column(nullable = false)
	private Boolean deleted;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name = "offer_id")
	private Offer offer;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "my_user_id")
	private Client client;
	@OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
	private List<Mark> marks;
	@OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
	private List<Complaint> complaints;
	@OneToOne
	private ReservationReport report;

	public Reservation(){}

	public Reservation(LocalDate startDate, LocalDate endDate, List<AdditionalService> additionalServices, Double price, Integer numberOfPerson, Boolean deleted, Offer offer, Client client, List<Mark> marks, List<Complaint> complaints, ReservationReport report) {
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.additionalServices = additionalServices;
		this.price = price;
		this.numberOfPerson = numberOfPerson;
		this.deleted = deleted;
		this.offer = offer;
		this.client = client;
		this.marks = marks;
		this.complaints = complaints;
		this.report = report;
	}

	public Reservation(LocalDate startDate, LocalDate endDate, List<AdditionalService> additionalServices, Double price, Integer numberOfPerson, Offer offer, Client client, Boolean deleted) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.additionalServices = additionalServices;
		this.price = price;
		this.numberOfPerson = numberOfPerson;
		this.offer = offer;
		this.client = client;
		this.deleted = deleted;

	}
	public Reservation(LocalDate startDate, LocalDate endDate, Double price, Integer numberOfPerson, Offer offer, Client client, Boolean deleted) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
		this.numberOfPerson = numberOfPerson;
		this.offer = offer;
		this.client = client;
		this.deleted = deleted;

	}

	public Integer getId() {
		return id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public List<AdditionalService> getAdditionalServices() {
		return additionalServices;
	}

	public Double getPrice() {
		return price;
	}

	public Integer getNumberOfPerson() {
		return numberOfPerson;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public Offer getOffer() {
		return offer;
	}

	public Client getClient() {
		return client;
	}

	public List<Mark> getMarks() {
		return marks;
	}

	public List<Complaint> getComplaints() {
		return complaints;
	}

	public ReservationReport getReport() {
		return report;
	}

	public void setAdditionalServices(List<AdditionalService> additionalServices) {
		this.additionalServices = additionalServices;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setNumberOfPerson(Integer numberOfPerson) {
		this.numberOfPerson = numberOfPerson;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}
}
