package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findAllByOfferId(Integer id);
  
    @Query("SELECT r FROM Reservation r JOIN FETCH r.client WHERE r.client.id = ?1 AND r.endDate > CURRENT_DATE")
    List<Reservation> findClientsUpcomingReservations(Integer id);

    @Query("SELECT r FROM Reservation r INNER JOIN Cottage c ON r.offer.id = c.id INNER JOIN Owner ow ON ow.id = c.cottageOwner.id AND ow.email = ?1")
    List<Reservation> findByCottageOwnerEmail(String email);

    @Query("SELECT r FROM Reservation r INNER JOIN Ship s ON r.offer.id = s.id INNER JOIN Owner ow ON ow.id = s.shipOwner.id AND ow.email = ?1")
    List<Reservation> findByShipOwnerEmail(String email);

    @Query("SELECT r FROM Reservation r INNER JOIN Adventure a ON r.offer.id = a.id INNER JOIN Owner ow ON ow.id = a.instructor.id AND ow.email = ?1")
    List<Reservation> findByInstructorEmail(String email);

    @Query("SELECT DISTINCT c FROM Reservation r INNER JOIN Cottage c ON r.offer.id = c.id WHERE" +
            " (r.startDate <= ?1 AND r.endDate >= ?1 AND r.deleted=false)")
    List<Cottage> nonAvailableCottages(LocalDate date);

    @Query("SELECT DISTINCT c FROM Reservation r INNER JOIN Ship c ON r.offer.id = c.id WHERE" +
            " (r.startDate <= ?1 AND r.endDate >= ?1 AND r.deleted=false)")
    List<Ship> nonAvailableShips(LocalDate date);

    @Query("SELECT DISTINCT c FROM Reservation r INNER JOIN Adventure c ON r.offer.id = c.id WHERE" +
            " (r.startDate <= ?1 AND r.endDate >= ?1 AND r.deleted=false)")
    List<Adventure> nonAvailableAdventures(LocalDate date);

    @Modifying
    @Query("UPDATE Reservation r SET r.deleted = true WHERE r.offer.id = ?1")
    void deleteByOfferId(Integer id);

    @Query("SELECT r FROM Reservation r INNER JOIN Cottage c ON r.offer.id = c.id INNER JOIN Owner ow ON ow.id = c.cottageOwner.id AND ow.email = ?1 WHERE  r.endDate < ?2")
    List<Reservation> findPastReservationByCottageOwnerEmail(String email, LocalDate today);

    @Query("SELECT r FROM Reservation r INNER JOIN Offer o ON r.offer.id = o.id WHERE" +
            " (r.startDate >= ?1 AND r.endDate <= ?2)")
    List<Reservation> findByOfferIdAndDates(LocalDate from, LocalDate to);

    @Query("SELECT r FROM Reservation r INNER JOIN Ship s ON r.offer.id = s.id INNER JOIN Owner ow ON ow.id = s.shipOwner.id AND ow.email = ?1 WHERE  r.endDate < ?2")
    List<Reservation> findPastReservationByShipOwnerEmail(String email, LocalDate today);

    @Query("SELECT r FROM Reservation r INNER JOIN Adventure a ON r.offer.id = a.id INNER JOIN Owner ow ON ow.id = a.instructor.id AND ow.email = ?1 WHERE  r.endDate < ?2")
    List<Reservation> findPastReservationByInstructorEmail(String email, LocalDate today);

    @Query("SELECT r FROM Reservation r INNER JOIN Cottage c ON r.offer.id = c.id INNER JOIN Owner ow ON ow.id = c.cottageOwner.id AND ow.email = ?1 WHERE  r.startDate <= ?2 AND r.endDate >= ?2")
    List<Reservation> findCurrentByOwnerEmail(String email, LocalDate today);

    @Query("SELECT r FROM Reservation r INNER JOIN Ship s ON r.offer.id = s.id INNER JOIN Owner ow ON ow.id = s.shipOwner.id  AND ow.email = ?1 WHERE  r.startDate <= ?2 AND r.endDate >= ?2")
    List<Reservation> findCurrentByShipOwnerEmail(String email, LocalDate today);

    @Query("SELECT r FROM Reservation r INNER JOIN Adventure a ON r.offer.id = a.id INNER JOIN Owner ow ON ow.id = a.instructor.id AND ow.email = ?1 WHERE  r.startDate <= ?2 AND r.endDate >= ?2")
    List<Reservation> findCurrentByInstructorEmail(String email, LocalDate today);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.client WHERE r.client.email = ?1")
    List<Reservation> findByClientEmail( String email);

    @Query("SELECT r FROM Reservation r INNER JOIN Cottage ctg ON r.offer.id = ctg.id INNER JOIN Client c ON c.id = r.client.id AND c.email = ?1 WHERE  r.endDate < ?2")
    List<Reservation> getPastCottageReservationsByClient(String email, LocalDate today);

    @Query("SELECT r FROM Reservation r INNER JOIN Ship ctg ON r.offer.id = ctg.id INNER JOIN Client c ON c.id = r.client.id AND c.email = ?1 WHERE  r.endDate < ?2")
    List<Reservation> getPastShipReservationsByClient(String email, LocalDate today);

    @Query("SELECT r FROM Reservation r INNER JOIN Adventure ctg ON r.offer.id = ctg.id INNER JOIN Client c ON c.id = r.client.id AND c.email = ?1 WHERE  r.endDate < ?2")
    List<Reservation> getPastAdventureReservationsByClient(String email, LocalDate today);

    @Query("SELECT r.id FROM Reservation r INNER JOIN Cottage c ON r.offer.id = c.id INNER JOIN Owner ow ON ow.id = c.cottageOwner.id AND ow.email = ?1 INNER JOIN ReservationReport rr ON rr.reservation.id = r.id WHERE r.endDate < ?2 ")
    List<Integer> findReservationWithReportByCottageOwnerEmail(String email, LocalDate today);

    @Query("SELECT r.id FROM Reservation r INNER JOIN Ship s ON r.offer.id = s.id INNER JOIN Owner ow ON ow.id = s.shipOwner.id AND ow.email = ?1 INNER JOIN ReservationReport rr ON rr.reservation.id = r.id WHERE r.endDate < ?2 ")
    List<Integer> findReservationWithReportByShipOwnerEmail(String email, LocalDate today);
  
    @Query("SELECT r FROM Reservation r INNER JOIN Cottage ctg ON r.offer.id = ctg.id INNER JOIN Client c ON c.id = r.client.id AND c.email = ?1 WHERE  r.startDate >= ?2")
    List<Reservation> getUpcomingCottageReservationsByClient(String email, LocalDate today);

    @Query("SELECT r FROM Reservation r INNER JOIN Ship ctg ON r.offer.id = ctg.id INNER JOIN Client c ON c.id = r.client.id AND c.email = ?1 WHERE  r.startDate >= ?2")
    List<Reservation> getUpcomingShipReservationsByClient(String email, LocalDate today);

    @Query("SELECT r FROM Reservation r INNER JOIN Adventure ctg ON r.offer.id = ctg.id INNER JOIN Client c ON c.id = r.client.id AND c.email = ?1 WHERE  r.startDate >= ?2")
    List<Reservation> getUpcomingAdventureReservationsByClient(String email, LocalDate today);

    @Modifying
    @Query("UPDATE Reservation r SET r.deleted = true WHERE r.id = ?1")
    void deleteById(Integer id);

    @Query("SELECT r.id FROM Reservation r WHERE r.id = ?1 AND ?3 > ?2")
    Integer checkCancelCondition(Integer id, LocalDate boundary, LocalDate today);

    @Query("SELECT r.id FROM Client c INNER JOIN Reservation  r ON c.id = r.client.id INNER JOIN Offer o ON r.offer.id = o.id WHERE r.deleted=true AND c.email = ?1 AND r.startDate = ?2 AND o.id = ?3")
    Optional<Integer> checkIfCanceled(String email, LocalDate date, Integer offerId);

    @Query("SELECT r FROM Reservation r INNER JOIN Cottage c ON r.offer.id = c.id INNER JOIN Owner ow ON ow.id = c.cottageOwner.id AND ow.email = ?1")
    List<Reservation> findAllByCottageOwnerEmail(String email);

    @Query("SELECT r FROM Reservation r INNER JOIN Ship s ON r.offer.id = s.id INNER JOIN Owner ow ON ow.id = s.shipOwner.id AND ow.email = ?1")
    List<Reservation> findAllByShipOwnerEmail(String email);

    @Query("SELECT r FROM Reservation r INNER JOIN Adventure a ON r.offer.id = a.id INNER JOIN Owner ow ON ow.id = a.instructor.id AND ow.email = ?1")
    List<Reservation> findAllByInstructorEmail(String email);

    @Query("SELECT r.id FROM Reservation r INNER JOIN Adventure a ON r.offer.id = a.id " +
            "INNER JOIN Owner ow ON ow.id = a.instructor.id " +"AND ow.email = ?1 " +
            "INNER JOIN ReservationReport rr ON rr.reservation.id = r.id WHERE r.endDate < ?2 ")
    List<Integer> findReservationWithReportByInstructorEmeil(String ownerEmail, LocalDate today);

    @Query("SELECT r FROM Reservation r INNER JOIN Cottage c ON r.offer.id = c.id INNER JOIN Owner ow ON ow.id = c.cottageOwner.id AND ow.email = ?1 WHERE  r.startDate >= ?2")
    List<Reservation> findFutureByCottageOwnerEmail(String email, LocalDate today);

    @Query("SELECT r FROM Reservation r INNER JOIN Ship s ON r.offer.id = s.id INNER JOIN Owner ow ON ow.id = s.shipOwner.id  AND ow.email = ?1 WHERE  r.startDate >= ?2")
    List<Reservation> findFutureByShipOwnerEmail(String email, LocalDate today);

    @Query("SELECT r FROM Reservation r INNER JOIN Adventure a ON r.offer.id = a.id INNER JOIN Owner ow ON ow.id = a.instructor.id AND ow.email = ?1 WHERE  r.startDate >= ?2")
    List<Reservation> findFutureByInstructorEmail(String email, LocalDate today);

    @Query("SELECT r FROM Reservation  r INNER  JOIN Cottage  c ON r.offer.id = c.id WHERE r.startDate >= ?1 AND r.endDate <= ?2")
    List<Reservation> findAllPastCottageReservations(LocalDate startDay, LocalDate endDate);

    @Query("SELECT r FROM Reservation  r INNER  JOIN Ship  s ON r.offer.id = s.id WHERE r.startDate >= ?1 AND r.endDate <= ?2")
    List<Reservation> findAllPastShipReservations(LocalDate startDay, LocalDate endDate);

    @Query("SELECT r FROM Reservation  r INNER  JOIN Adventure  a ON r.offer.id = a.id WHERE r.startDate >= ?1 AND r.endDate <= ?2")
    List<Reservation> findAllPastAdventureReservations(LocalDate startDay, LocalDate endDate);

    @Query("SELECT r FROM Reservation r WHERE r.deleted = false AND r.offer.id = ?1 AND r.endDate < ?2")
    List<Reservation> findAllPasedByOfferId(Integer id, LocalDate today);

}
