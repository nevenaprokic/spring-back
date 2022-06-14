package com.booking.ISAbackend.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Instructor extends Owner{

	@Column(nullable = false)
	private String biography;
	
	@OneToMany(mappedBy = "instructor", fetch = FetchType.LAZY)
	private List<Adventure> adventures;

	public Instructor() {

	}

	public String getBiography(){
		return biography;
	}

	public Instructor(String firstName, String lastName, String password, String phoneNumber, String email, Boolean deleted, Role role, Address address, Integer points, String biography) {
		super(firstName, lastName, password, phoneNumber, email, deleted, role, address, points);
		this.biography = biography;
	}

	public List<Adventure> getAdventures() {
		return adventures;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}
}