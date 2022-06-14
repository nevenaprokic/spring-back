package com.booking.ISAbackend.repository;


import com.booking.ISAbackend.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.ISAbackend.model.MyUser;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<MyUser, Integer>{
	MyUser findByEmail(String email);

    @Query(value = "SELECT * FROM my_user WHERE email = ?1 and password= ?2", nativeQuery = true)
    MyUser isPasswordOldCorrect(String email, String oldPassword);
}
