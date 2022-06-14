package com.booking.ISAbackend.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Ship extends Offer{

	private String type;
	private String size;
	private Integer motorNumber;
	private Integer motorPower;
	private Integer maxSpeed;
	private String navigationEquipment;
	private String additionalEquipment;

	public Ship(String name, String description, Double price, List<Photo> photos, Integer numberOfPerson, String rulesOfConduct, List<AdditionalService> additionalServices, String cancellationConditions, Boolean deleted, Address address, List<QuickReservation> quickReservations, List<Reservation> reservations, List<Client> subscribedClients, String type, String size, Integer motorNumber, Integer motorPower, Integer maxSpeed, String navigationEquipment, String additionalEquipment, ShipOwner shipOwner) {
		super(name, description, price, photos, numberOfPerson, rulesOfConduct, additionalServices, cancellationConditions, deleted, address, quickReservations, reservations, subscribedClients);
		this.type = type;
		this.size = size;
		this.motorNumber = motorNumber;
		this.motorPower = motorPower;
		this.maxSpeed = maxSpeed;
		this.navigationEquipment = navigationEquipment;
		this.additionalEquipment = additionalEquipment;
		this.shipOwner = shipOwner;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ship_owner_id")
	private ShipOwner shipOwner;

	public Ship() {

	}

	public String getType() {
		return type;
	}

	public String getSize() {
		return size;
	}

	public Integer getMotorNumber() {
		return motorNumber;
	}

	public Integer getMotorPower() {
		return motorPower;
	}

	public Integer getMaxSpeed() {
		return maxSpeed;
	}

	public String getNavigationEquipment() {
		return navigationEquipment;
	}

	public String getAdditionalEquipment() {
		return additionalEquipment;
	}

	public ShipOwner getShipOwner() {
		return shipOwner;
	}
}
