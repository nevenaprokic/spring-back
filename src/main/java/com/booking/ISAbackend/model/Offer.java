package com.booking.ISAbackend.model;

import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.*;

@Entity
@Where(clause = "deleted = false")
@Inheritance(strategy = InheritanceType.JOINED)
public class Offer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String description;
	private Double price;
	@OneToMany(fetch = FetchType.LAZY)
	private List<Photo> photos;
	private Integer numberOfPerson;
	private String rulesOfConduct;
	@OneToMany(fetch = FetchType.LAZY)
	private List<AdditionalService> additionalServices;
	private String cancellationConditions;
	private Boolean deleted;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private Address address;
	
	@OneToMany(mappedBy = "offer", fetch = FetchType.LAZY)
	private List<QuickReservation> quickReservations;

	@OneToMany(mappedBy = "offer", fetch = FetchType.LAZY)
	private List<Reservation> reservations;

	@OneToMany(mappedBy = "offer", fetch = FetchType.LAZY)
	private List<UnavailableOfferDates> unavailableDate;

	@ManyToMany(mappedBy = "subscribedOffers")
	private List<Client> subscribedClients;

	@Version
	@Column(name = "optlock", columnDefinition = "integer DEFAULT 0", nullable = false)
	private Long version;

	@Column(nullable = false, columnDefinition = "integer DEFAULT 0")
	private Long numberOfReservations;

	@Column(nullable = false, columnDefinition = "integer DEFAULT 0")
	private Long numberOfQuickReservations;

	@Column(nullable = false, columnDefinition = "integer DEFAULT 0")
	private Long numberOfModify;


	public Offer(String name, String description, Double price, List<Photo> photos, Integer numberOfPerson, String rulesOfConduct, List<AdditionalService> additionalServices, String cancellationConditions, Boolean deleted, Address address, List<QuickReservation> quickReservations, List<Reservation> reservations, List<Client> subscribedClients) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.photos = photos;
		this.numberOfPerson = numberOfPerson;
		this.rulesOfConduct = rulesOfConduct;
		this.additionalServices = additionalServices;
		this.cancellationConditions = cancellationConditions;
		this.deleted = deleted;
		this.address = address;
		this.quickReservations = quickReservations;
		this.reservations = reservations;
		this.subscribedClients = subscribedClients;
	}

	public Offer() {

	}
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	public Integer getId() {return id;}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Double getPrice() {
		return price;
	}

	public List<Photo> getPhotos() {
		List<Photo> notDeleted = new ArrayList<>();
		for(Photo photo: photos){
			notDeleted.add(photo);
		}
		return notDeleted;
	}

	public Integer getNumberOfPerson() {
		return numberOfPerson;
	}

	public String getRulesOfConduct() {
		return rulesOfConduct;
	}

	public List<AdditionalService> getAdditionalServices() {
		return additionalServices;
	}

	public String getCancellationConditions() {
		return cancellationConditions;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public Address getAddress() {
		return address;
	}

	public List<QuickReservation> getQuickReservations() {
		return quickReservations;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public List<Client> getSubscribedClients() {
		return subscribedClients;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setUnavailableDate(List<UnavailableOfferDates> unavailableDate) {
		this.unavailableDate = unavailableDate;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}

	public void setNumberOfPerson(Integer numberOfPerson) {
		this.numberOfPerson = numberOfPerson;
	}

	public void setRulesOfConduct(String rulesOfConduct) {
		this.rulesOfConduct = rulesOfConduct;
	}

	public void setAdditionalServices(List<AdditionalService> additionalServices) {
		this.additionalServices = additionalServices;
	}

	public void setCancellationConditions(String cancellationConditions) {
		this.cancellationConditions = cancellationConditions;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setQuickReservations(List<QuickReservation> quickReservations) {
		this.quickReservations = quickReservations;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public void setSubscribedClients(List<Client> subscribedClients) {
		this.subscribedClients = subscribedClients;
	}

	public List<UnavailableOfferDates> getUnavailableDate() {
		return unavailableDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Offer offer = (Offer) o;
		return id.equals(offer.id) && Objects.equals(name, offer.name) && Objects.equals(description, offer.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, description);
	}

	public void setNumberOfReservations(Long numberOfReservations) {
		this.numberOfReservations = numberOfReservations;
	}

	public Long getNumberOfReservations() {
		return numberOfReservations;
	}

	public Long getNumberOfQuickReservation() {
		return numberOfQuickReservations;
	}

	public void setNumberOfQuickReservation(Long numberOfQuickReservation) {
		this.numberOfQuickReservations = numberOfQuickReservation;
	}

	public Long getNumberOfModify() {
		return numberOfModify;
	}

	public void setNumberOfModify(Long numberOfModify) {
		this.numberOfModify = numberOfModify;
	}
}
