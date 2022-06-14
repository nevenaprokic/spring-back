package com.booking.ISAbackend.model;

import java.util.List;
import javax.persistence.*;

@Entity
public class Owner extends MyUser {

	@OneToMany(fetch = FetchType.LAZY)
	private List<Transaction> transaction;

//	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	private OwnerCategory ownerCategory;
	private Integer points;

	public Owner(String firstName, String lastName, String password, String phoneNumber, String email, Boolean deleted, Role role, Address address, Integer points ) {
		super(firstName, lastName, password, phoneNumber, email, deleted, role, address);
		this.points = points;
	}

	public Owner() {
		super();
	}

//	public OwnerCategory getOwnerCategory(){
//		return ownerCategory;
//	}
//
//	public void setOwnerCategory(OwnerCategory category){ this.ownerCategory = category;}

	public Integer getPoints() { return points;}
	public void setPoints(Integer points) {this.points = points; }
}