package com.booking.ISAbackend.dto;

import com.booking.ISAbackend.model.Address;
import com.booking.ISAbackend.model.MyUser;

public class UserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String state;
    private String city;
    private String street;
    private String category;
    private int points;
    private int penalty;
    private int id;
    private String role;
    private int userNumber;

    public UserDTO(String email, String firstName, String lastName, String phoneNumber, String state, String city, String street, String category, int points, int penalty, int id, String role, int userNumber) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.state = state;
        this.city = city;
        this.street = street;
        this.category = category;
        this.points = points;
        this.penalty = penalty;
        this.id = id;
        this.role = role;
        this.userNumber = userNumber;
    }

    public UserDTO(String email, String firstName, String lastName, String phoneNumber, String state, String city, String street, String category, int points, int penalty, int id, String role) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.state = state;
        this.city = city;
        this.street = street;
        this.category = category;
        this.points = points;
        this.penalty = penalty;
        this.id = id;
        this.role = role;

    }

    public UserDTO(MyUser user, Address addres, String role, String category, int penalty, int points) {
        this.category = category;
        this.penalty = penalty;
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.city = addres.getCity();
        this.state = addres.getState();
        this.street = addres.getStreet();
        this.points = points;
        this.role = role;
        this.id = user.getId();
    }

    public UserDTO(MyUser user, Address addres, String role, String category) {
        this.category = category;
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.city = addres.getCity();
        this.state = addres.getState();
        this.street = addres.getStreet();
        this.role = role;
        this.id = user.getId();
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getCategory() {
        return category;
    }

    public int getPoints() {
        return points;
    }

    public int getPenalty() {
        return penalty;
    }

    public int getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public int getUserNumber(){
        return userNumber;
    }

    public void setUserNumber(int number){
        this.userNumber = number;
    }

}
