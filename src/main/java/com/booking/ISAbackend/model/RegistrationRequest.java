package com.booking.ISAbackend.model;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class RegistrationRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private String personType;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String phoneNumber;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private Boolean deleted;

	@Column(nullable = false)
	private LocalDate sendingTime;


	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private Address address;

	public RegistrationRequest(String description, String personType, String firstName, String lastName, String password, String phoneNumber, String email, Boolean deleted, Address address) {
		this.description = description;
		this.personType = personType;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.deleted = deleted;
		this.address = address;
	}

	public RegistrationRequest(String description, String personType, String firstName, String lastName, String password, String phoneNumber, String email, Boolean deleted, LocalDate sendingTime, Address address) {
		this.description = description;
		this.personType = personType;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.deleted = deleted;
		this.sendingTime = sendingTime;
		this.address = address;
	}

	public RegistrationRequest() {

	}

	public Integer getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public String getPersonType() {
		return personType;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPassword() {
		return password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public Address getAddress() {
		return address;
	}

	public LocalDate getSendingTime(){
		return  sendingTime;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public void setSendingTime(LocalDate sendingTime) {
		this.sendingTime = sendingTime;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
