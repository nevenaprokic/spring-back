package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.Mark;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Integer>{
    @Query("SELECT m FROM Mark m INNER JOIN Reservation r ON m.reservation.id = r.id INNER JOIN Offer o ON o.id = r.offer.id AND o.id = ?1 WHERE m.approved = true")
    List<Mark> findAllMarkByOfferId(Integer idOffer);

    @Query("SELECT m FROM Mark m WHERE m.client.id=?1 AND m.reservation.id=?2")
    Optional<Mark> alreadyReviewed(Integer clientId, Integer reservationId);

    @Query("SELECT m FROM  Mark m WHERE  m.reviewed=false ")
    List<Mark> findAllNotApproved(Sort sendingTime);

    Mark findById(Long markId);
}