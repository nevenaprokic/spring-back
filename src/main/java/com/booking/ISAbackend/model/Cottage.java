package com.booking.ISAbackend.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Cottage extends Offer{

	@Column(nullable = false)
	private Integer roomNumber;

	@Column(nullable = false)
	private Integer bedNumber;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "my_user_id")
	private CottageOwner cottageOwner;


	public Cottage() {

	}

	public Integer getRoomNumber() {return roomNumber;}
	public  Integer getBedNumber() {return  bedNumber;}
	public CottageOwner getCottageOwner() {
		return cottageOwner;
	}

	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}

	public void setBedNumber(Integer bedNumber) {
		this.bedNumber = bedNumber;
	}

	public void setCottageOwner(CottageOwner cottageOwner) {
		this.cottageOwner = cottageOwner;
	}

	public Cottage(String name, String description, Double price, List<Photo> photos, Integer numberOfPerson, String rulesOfConduct, List<AdditionalService> additionalServices, String cancellationConditions, Boolean deleted, Address address, List<QuickReservation> quickReservations, List<Reservation> reservations, List<Client> subscribedClients, Integer roomNumber, Integer bedNumber, CottageOwner cottageOwner) {
		super(name, description, price, photos, numberOfPerson, rulesOfConduct, additionalServices, cancellationConditions, deleted, address, quickReservations, reservations, subscribedClients);
		this.roomNumber = roomNumber;
		this.bedNumber = bedNumber;
		this.cottageOwner = cottageOwner;
	}
}
