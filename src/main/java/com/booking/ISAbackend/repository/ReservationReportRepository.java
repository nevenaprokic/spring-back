package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.ReservationReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationReportRepository extends JpaRepository<ReservationReport, Integer> {

    @Query("SELECT r FROM ReservationReport r WHERE r.penalOption = true AND r.reviewed = false AND r.automaticallyPenal=false")
    List<ReservationReport> findAllNotReviewedForPenalty();
}
