package com.booking.ISAbackend.dto;

public class AdminDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String street;
    private String city;
    private String state;
    private boolean emailVerified;
    private boolean defaultAdmin;
    private boolean firstLogin;

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

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public boolean isEmailVerified() {return emailVerified;}
    public boolean isDefaultAdmin() {return defaultAdmin;}
    public boolean isFirstLogin() {return firstLogin;}

    public AdminDTO(String email, String firstName, String lastName, String phoneNumber,
                    String street, String city, String state, boolean emailVerified,
                    boolean defaultAdmin, boolean firstLogin) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.emailVerified = emailVerified;
        this.defaultAdmin = defaultAdmin;
        this.firstLogin = firstLogin;
    }
}
