package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.Adventure;
import com.booking.ISAbackend.model.Instructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdventureReporitory extends JpaRepository<Adventure, Integer> {
    List<Adventure> findAdventureByInstructorEmail(String email);
    Optional<Adventure> findById(Integer id);

    @Query("SELECT a FROM Adventure a JOIN FETCH a.address WHERE (lower(a.address.city) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', a.address.city, '%'))" +
            " OR lower(a.address.street) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', a.address.street, '%'))"+
            " OR lower(a.address.state) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', a.address.state, '%')))"+
            " AND (lower(a.name) LIKE lower(concat('%', :name, '%')) OR lower(:name) LIKE lower(concat('%', a.name, '%')))"+
            " AND (a.numberOfPerson <= :maxPeople OR :maxPeople = -1) AND (a.price <= :price OR :price = -1) AND (a.instructor.email = :email) ")
    List<Adventure> searchAdventureByInstructorEmail(@Param("name") String name, @Param("maxPeople") int maxPeople, @Param("address")String address, @Param("price") double price, @Param("email") String email);

    List<Adventure> findAllByInstructor(Instructor i);

    @Query("SELECT a.id FROM Adventure a")
    List<Integer> getAdveturesId();

    @Query("SELECT a FROM Adventure a WHERE a.deleted = false")
    Page<Adventure> findAllActiveAdventures(PageRequest request); /*DODATI OVO U BROD I VIKENDICE I ZAMINEITI KOD FIND ALL*/

    @Query( "Select count(distinct a) FROM Adventure a  WHERE a.deleted = false")
    int getAdventuresNumber();



}
