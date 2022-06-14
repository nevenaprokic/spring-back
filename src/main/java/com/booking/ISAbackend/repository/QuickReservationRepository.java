package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.QuickReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuickReservationRepository extends JpaRepository<QuickReservation, Integer> {
    List<QuickReservation> findQuickReservationsByOfferId(Integer id);

    @Modifying
    @Query("DELETE FROM QuickReservation q WHERE q.offer.id = ?1")
    void deleteByOfferId(Integer id);
}
