package com.booking.ISAbackend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = false)
	private String city;
	@Column(nullable = false)
	private String state;
	@Column(nullable = false)
	private String street;

    public Address(String street, String city, String state) {
		this.street = street;
		this.city = city;
		this.state = state;
    }


	public Address() {

	}

	public Integer getId() {
		return id;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getStreet() {
		return street;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setId(int id){ this.id = id;}
}
