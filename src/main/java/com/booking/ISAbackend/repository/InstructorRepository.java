package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.Instructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InstructorRepository extends JpaRepository<Instructor, Integer> {
    Instructor findById(int id);
    Instructor findByEmail(String email);

    @Query("SELECT c FROM Instructor c JOIN FETCH c.address WHERE (lower(c.address.city) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.city, '%'))" +
            " OR lower(c.address.street) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.street, '%'))"+
            " OR lower(c.address.state) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.state, '%')))" +
            " AND (lower(c.firstName) LIKE lower(concat('%', :firstName, '%')) OR lower(:firstName) LIKE lower(concat('%', c.firstName, '%')))"+
            "AND (lower(c.lastName) LIKE lower(concat('%', :lastName, '%')) OR lower(:lastName) LIKE lower(concat('%', c.lastName, '%')))" +
            "AND (lower(c.phoneNumber) LIKE lower(concat('%', :phoneNumber, '%')) OR lower(:phoneNumber) LIKE lower(concat('%', c.phoneNumber, '%')))"
    )
    List<Instructor> searchInstructors(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("address")String address, @Param("phoneNumber") String phoneNumber);

    @Query("SELECT c FROM Instructor c JOIN FETCH c.address WHERE (lower(c.address.city) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.city, '%'))" +
            " OR lower(c.address.street) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.street, '%'))"+
            " OR lower(c.address.state) LIKE lower(concat('%', :address, '%')) OR lower(:address) LIKE lower(concat('%', c.address.state, '%')))" +
            " AND (lower(c.firstName) LIKE lower(concat('%', :firstName, '%')) OR lower(:firstName) LIKE lower(concat('%', c.firstName, '%')))"+
            "AND (lower(c.lastName) LIKE lower(concat('%', :lastName, '%')) OR lower(:lastName) LIKE lower(concat('%', c.lastName, '%')))"
    )
    List<Instructor> searchInstructorsClient(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("address")String address);

    @Query("SELECT DISTINCT i FROM Instructor i INNER JOIN Adventure a ON a.id = ?1 WHERE a.instructor.id = i.id")
    Instructor findInstructorByAdventure(int adventureId);

    @Query( "Select count(distinct inst) FROM Instructor inst  WHERE inst.deleted = false")
    int getNumberOfInstructors();

    @Query("SELECT i FROM Instructor i WHERE i.deleted = false")
    Page<Instructor> findAllActiveUsersByPage(PageRequest request);

    @Query("SELECT i FROM Instructor i WHERE i.deleted = false")
    List<Instructor> findAllActiveInstructors();

}
