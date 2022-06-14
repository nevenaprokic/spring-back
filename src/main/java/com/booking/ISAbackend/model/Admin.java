package com.booking.ISAbackend.model;

import javax.persistence.*;

@Entity
public class Admin extends MyUser {

    @Column(nullable = false)
    private boolean defaultAdmin;

    @Column(nullable = false)
    private boolean firstLogin;

    public Admin() {
    }

    public Admin(String firstName, String lastName, String password, String phoneNumber, String email, Boolean deleted, Role role, Address address) {
        super(firstName, lastName, password, phoneNumber, email, deleted, role, address);
    }

    public Admin(String firstName, String lastName, String password, String phoneNumber, String email, Boolean deleted, Role role, Address address,
                 boolean firstLogin, boolean defaultAdmin) {
        super(firstName, lastName, password, phoneNumber, email, deleted, role, address);
        this.firstLogin = firstLogin;
        this.defaultAdmin = defaultAdmin;
    }

    public boolean isDefaultAdmin() {
        return defaultAdmin;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setDefaultAdmin(boolean defaultAdmin) {
        this.defaultAdmin = defaultAdmin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }
}
