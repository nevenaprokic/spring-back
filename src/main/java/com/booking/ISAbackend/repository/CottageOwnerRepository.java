package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.CottageOwner;
import com.booking.ISAbackend.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CottageOwnerRepository extends JpaRepository<CottageOwner, Integer> {
    CottageOwner findById(int id);
    CottageOwner findByEmail(String email);

    @Query("SELECT DISTINCT c FROM CottageOwner  c INNER JOIN Cottage ct ON ct.id = ?1 WHERE ct.cottageOwner.id = c.id")
    CottageOwner findCottageOwnerByCottageId(int cottageId);
}
