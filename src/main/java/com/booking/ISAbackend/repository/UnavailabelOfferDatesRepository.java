package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.Reservation;
import com.booking.ISAbackend.model.UnavailableOfferDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UnavailabelOfferDatesRepository extends JpaRepository<UnavailableOfferDates, Integer> {
    List<UnavailableOfferDates> findByOfferId(Integer id);

    @Query("SELECT u FROM UnavailableOfferDates u WHERE u.offer.id = ?3 AND"  +
            "(( ?1 >= u.startDate AND ?1 <= u.endDate) OR ( ?2 >= u.startDate AND ?2 <= u.endDate))")
    List<UnavailableOfferDates> findDatesByOfferInInterval(LocalDate from, LocalDate to, int offerId);
}
