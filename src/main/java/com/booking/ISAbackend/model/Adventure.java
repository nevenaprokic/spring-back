package com.booking.ISAbackend.model;

import javax.persistence.*;

import java.util.List;

@Entity
public class Adventure extends Offer{

	@Column(nullable = false)
	private String additionalEquipment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "my_user_id")
	private Instructor instructor;

	public Adventure() {

	}

	public String getAdditionalEquipment() {
		return additionalEquipment;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public void setAdditionalEquipment(String additionalEquipment) {
		this.additionalEquipment = additionalEquipment;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public Adventure(String name, String description, Double price, List<Photo> photos, Integer numberOfPerson, String rulesOfConduct, List<AdditionalService> additionalServices, String cancellationConditions, Boolean deleted, Address address, List<QuickReservation> quickReservations, List<Reservation> reservations, List<Client> subscribedClients, String additionalEquipment, Instructor instructor) {
		super(name, description, price, photos, numberOfPerson, rulesOfConduct, additionalServices, cancellationConditions, deleted, address, quickReservations, reservations, subscribedClients);
		this.additionalEquipment = additionalEquipment;
		this.instructor = instructor;
	}
}
