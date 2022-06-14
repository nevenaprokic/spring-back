package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
  
}
