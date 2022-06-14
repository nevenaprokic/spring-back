package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.dto.ComplaintDTO;
import com.booking.ISAbackend.model.AdditionalService;
import com.booking.ISAbackend.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {
    @Query("SELECT m FROM Complaint m WHERE m.client.id=?1 AND m.reservation.id=?2")
    Complaint alreadyReviewed(Integer id, Integer reservationId);

    @Query("SELECT c FROM Complaint c WHERE c.deleted = false")
    List<Complaint> findAllNotDeleted();


}
