package com.booking.ISAbackend.repository;


import com.booking.ISAbackend.model.Complaint;
import com.booking.ISAbackend.model.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegistrationRequestRepository extends JpaRepository<RegistrationRequest, Integer> {

    @Query("SELECT r FROM RegistrationRequest r WHERE r.deleted = false")
    List<RegistrationRequest> findAllNotDeleted();
}
