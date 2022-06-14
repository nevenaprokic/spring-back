package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;

public interface AdminRepository extends JpaRepository<Admin, CriteriaBuilder.In> {

    Admin findByEmail(String email);
}
