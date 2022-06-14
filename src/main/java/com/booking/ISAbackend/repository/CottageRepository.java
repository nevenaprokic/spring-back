package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.Cottage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CottageRepository extends JpaRepository<Cottage, Integer> {
    List<Cottage> findCottageByCottageOwnerEmail(String email);
    Cottage findCottageById(Integer id);

    @Query("SELECT c FROM Cottage c JOIN FETCH c.address WHERE (lower(c.address.city) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.city, '%'))" +
            " OR lower(c.address.street) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.street, '%'))"+
            " OR lower(c.address.state) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.state, '%')))"+
            " AND (lower(c.name) LIKE lower(concat('%', :name, '%')) OR lower(:name) LIKE lower(concat('%', c.name, '%')))"+
            " AND (c.numberOfPerson = :maxPeople OR :maxPeople = -1) AND (c.price <= :price OR :price = -1) ")
    List<Cottage> searchCottages(@Param("name") String name, @Param("maxPeople") int maxPeople, @Param("address")String address, @Param("price") double price);

    @Query("SELECT c FROM Cottage c JOIN FETCH c.address WHERE (lower(c.address.city) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.city, '%'))" +
            " OR lower(c.address.street) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.street, '%'))"+
            " OR lower(c.address.state) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.state, '%')))"+
            " AND (lower(c.name) LIKE lower(concat('%', :name, '%')) OR lower(:name) LIKE lower(concat('%', c.name, '%')))"+
            " AND lower(c.description) LIKE lower(concat('%', :description, '%')) OR lower(:description) LIKE lower(concat('%', c.description, '%')) ")
    List<Cottage> searchCottagesClient(@Param("name") String name, @Param("description") String description, @Param("address")String address);

    @Query("SELECT c FROM Cottage c JOIN FETCH c.address WHERE (lower(c.address.city) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.city, '%'))" +
            " OR lower(c.address.street) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.street, '%'))"+
            " OR lower(c.address.state) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.state, '%')))"+
            " AND (lower(c.name) LIKE lower(concat('%', :name, '%')) OR lower(:name) LIKE lower(concat('%', c.name, '%')))"+
            " AND (c.numberOfPerson = :maxPeople OR :maxPeople = -1) AND (c.price <= :price OR :price = -1) AND (c.cottageOwner.email = :email) ")
    List<Cottage> searchCottagesByCottageOwnerEmail(@Param("name") String name, @Param("maxPeople") int maxPeople, @Param("address")String address, @Param("price") double price, @Param("email") String email);

    @Query("SELECT c.id FROM Cottage c")
    List<Integer> getCottagesId();
}
