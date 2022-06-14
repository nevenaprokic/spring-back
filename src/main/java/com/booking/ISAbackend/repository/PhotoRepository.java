package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.Instructor;
import com.booking.ISAbackend.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {
}
