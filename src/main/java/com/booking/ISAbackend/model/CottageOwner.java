package com.booking.ISAbackend.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class CottageOwner extends Owner{
	
	@OneToMany(mappedBy = "cottageOwner", fetch = FetchType.LAZY)
	private List<Cottage> cottages;

	public List<Cottage> getCottages() {
		return cottages;
	}
}
