package com.booking.ISAbackend.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.persistence.*;

@Entity
@SQLDelete(sql = "UPDATE quick_reservation SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class QuickReservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = false)
	private LocalDate startDate;
	@Column(nullable = false)
	private LocalDate endDateAction;
	@Column(nullable = false)
	private LocalDate startDateAction;
	@Column(nullable = false)
	private LocalDate endDate;
	@OneToMany(fetch = FetchType.LAZY)
	private List<AdditionalService> additionalServices;
	@Column(nullable = false)
	private Double price;
	@Column(nullable = false)
	private Integer numberOfPerson;

	private boolean deleted = Boolean.FALSE;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "offer_id")
	private Offer offer;

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDateAction() {
		return endDateAction;
	}

	public LocalDate getStartDateAction() {
		return startDateAction;
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

	public Offer getOffer() {
		return offer;
	}

	public Integer getId() {
		return id;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public QuickReservation( LocalDate startDate, LocalDate endDateAction, LocalDate startDateAction, LocalDate endDate,  Double price, Integer numberOfPerson, boolean deleted, Offer offer) {
		this.startDate = startDate;
		this.endDateAction = endDateAction;
		this.startDateAction = startDateAction;
		this.endDate = endDate;
		this.price = price;
		this.numberOfPerson = numberOfPerson;
		this.deleted = deleted;
		this.offer = offer;
	}

	public void setAdditionalServices(List<AdditionalService> additionalServices) {
		this.additionalServices = additionalServices;
	}

	public QuickReservation(){}
}
