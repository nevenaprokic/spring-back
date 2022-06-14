package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.Cottage;
import com.booking.ISAbackend.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Integer> {
    List<Ship> findShipByShipOwnerEmail(String email);
    Ship findShipById(Integer id);

    @Query("SELECT c FROM Ship c JOIN FETCH c.address WHERE (lower(c.address.city) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.city, '%'))" +
            " OR lower(c.address.street) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.street, '%'))"+
            " OR lower(c.address.state) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.state, '%')))"+
            " AND (lower(c.name) LIKE lower(concat('%', :name, '%')) OR lower(:name) LIKE lower(concat('%', c.name, '%')))"+
            " AND (c.numberOfPerson = :maxPeople OR :maxPeople = -1) AND (c.price <= :price OR :price = -1) ")
    List<Ship> searchShips(@Param("name") String name, @Param("maxPeople") int maxPeople, @Param("address")String address, @Param("price") double price);

    @Query("SELECT c FROM Ship c JOIN FETCH c.address WHERE (lower(c.address.city) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.city, '%'))" +
            " OR lower(c.address.street) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.street, '%'))"+
            " OR lower(c.address.state) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.state, '%')))"+
            " AND (lower(c.name) LIKE lower(concat('%', :name, '%')) OR lower(:name) LIKE lower(concat('%', c.name, '%')))"+
            " AND lower(c.description) LIKE lower(concat('%', :description, '%')) OR lower(:description) LIKE lower(concat('%', c.description, '%')) ")
    List<Ship> searchShipsClient(@Param("name") String name, @Param("description") String description, @Param("address")String address);

    @Query("SELECT s FROM Ship s JOIN FETCH s.address WHERE (lower(s.address.city) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', s.address.city, '%'))" +
            " OR lower(s.address.street) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', s.address.street, '%'))"+
            " OR lower(s.address.state) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', s.address.state, '%')))"+
            " AND (lower(s.name) LIKE lower(concat('%', :name, '%')) OR lower(:name) LIKE lower(concat('%', s.name, '%')))"+
            " AND (s.numberOfPerson = :maxPeople OR :maxPeople = -1) AND (s.price <= :price OR :price = -1) AND (s.shipOwner.email = :email) ")
    List<Ship> searchShipsByShipOwnerEmail(@Param("name") String name, @Param("maxPeople") int maxPeople, @Param("address")String address, @Param("price") double price, @Param("email") String email);

    @Query("SELECT s.id FROM Ship s")
    List<Integer> getShipsId();
}
