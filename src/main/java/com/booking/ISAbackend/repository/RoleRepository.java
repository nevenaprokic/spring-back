package com.booking.ISAbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.ISAbackend.model.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
	List<Role> findByName(String name);
}
