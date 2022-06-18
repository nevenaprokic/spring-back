package com.booking.ISAbackend.model;
import javax.persistence.*;

@Entity
public class DeleteRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String description;

	private Boolean deleted = false;

	@OneToOne
	private MyUser myUser;

	@Version
	@Column(name = "optlock", columnDefinition = "integer DEFAULT 0", nullable = false)
	private Long version;

	public DeleteRequest(){}

	public DeleteRequest(MyUser myUser) {
		this.myUser = myUser;
	}

	public DeleteRequest(String description, MyUser myUser) {
		this.description = description;
		this.myUser = myUser;
		this.deleted = false;
	}

	public Integer getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public MyUser getMyUser() {
		return myUser;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public void setMyUser(MyUser myUser) {
		this.myUser = myUser;
	}

	public void setId(int id) {this.id = id;}
}
