package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Integer> {
    Owner findById(int id);
    Owner findByEmail(String email);
}
