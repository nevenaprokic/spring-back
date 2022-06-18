package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;

public interface AdminRepository extends JpaRepository<Admin, CriteriaBuilder.In> {

    Admin findByEmail(String email);

    @Query( "Select count(distinct a) FROM Admin a WHERE a.email <> ?1 AND a.deleted = false")
    int getNumberOfAdmins(String currentAdmin);

    Admin findById(long id);

    @Query("SELECT a FROM Admin a WHERE a.deleted = false")
    Page<Admin> findAllActiveUsers(PageRequest request);
}
