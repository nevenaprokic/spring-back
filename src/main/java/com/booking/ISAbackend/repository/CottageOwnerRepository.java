package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.CottageOwner;
import com.booking.ISAbackend.model.Instructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CottageOwnerRepository extends JpaRepository<CottageOwner, Integer> {
    CottageOwner findById(int id);
    CottageOwner findByEmail(String email);

    @Query("SELECT DISTINCT c FROM CottageOwner  c INNER JOIN Cottage ct ON ct.id = ?1 WHERE ct.cottageOwner.id = c.id")
    CottageOwner findCottageOwnerByCottageId(int cottageId);

    @Query( "Select count(distinct c) FROM CottageOwner  c  WHERE c.deleted = false")
    int getNumberOfCottageOwners();

    @Query("SELECT c FROM CottageOwner c WHERE c.deleted = false")
    Page<CottageOwner> findAllActiveUsers(PageRequest request);
}
