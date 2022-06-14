package com.booking.ISAbackend.repository;


import com.booking.ISAbackend.model.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Integer> {

}
