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
public class ShipOwner extends Owner {

	@OneToMany(mappedBy = "shipOwner", fetch = FetchType.LAZY)
	private List<Ship> ships;

	public List<Ship> getShips() {
		return ships;
	}
}
