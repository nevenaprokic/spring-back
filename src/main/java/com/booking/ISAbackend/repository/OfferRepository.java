package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.Cottage;
import com.booking.ISAbackend.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {
    Offer findOfferById(Integer id);

    @Modifying
    @Query("UPDATE Offer o SET o.deleted = true WHERE o.id = ?1")
    void updateDeleteByOfferId(Integer id);

    @Query("SELECT DISTINCT c FROM Reservation r INNER JOIN Offer c ON r.offer.id = c.id WHERE" +
            " (r.startDate <= ?1 AND r.endDate >= ?1) OR (r.startDate >= ?1 AND r.startDate <= ?2) ")
    List<Offer> nonAvailableOffers(LocalDate from, LocalDate to);
}
