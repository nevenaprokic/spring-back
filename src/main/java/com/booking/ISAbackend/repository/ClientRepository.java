package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.Client;
import com.booking.ISAbackend.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findByEmail(String email);

    @Query("SELECT c.penal from Client c WHERE c.email = ?1")
    Integer getPenalties(String email);

    @Modifying
    @Query("UPDATE Client c SET c.penal = 0 WHERE c.penal <> 0")
    void removePenalties();

    @Query("SELECT c.subscribedOffers from Client c WHERE c.email = ?1")
    List<Offer> getSubscriptions(String email);
}
