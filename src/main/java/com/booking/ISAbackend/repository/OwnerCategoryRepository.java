package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.ClientCategory;
import com.booking.ISAbackend.model.OwnerCategory;
import com.booking.ISAbackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OwnerCategoryRepository extends JpaRepository<OwnerCategory, Integer> {
    List<OwnerCategory> findByName(String name);
    List<OwnerCategory> findAll();

    @Query("SELECT c FROM OwnerCategory c WHERE ?1 >= c.lowLimitPoints AND ?1 <= c.heighLimitPoints")
    List<OwnerCategory> findByMatchingInterval(int points);

    @Query("SELECT c FROM OwnerCategory c WHERE" +
            " (?1 >= c.lowLimitPoints AND ?1 <= c.heighLimitPoints)  OR (?2 >= c.lowLimitPoints AND ?2 <= c.heighLimitPoints)")
    List<OwnerCategory> findByInterval(int start, int end);

    OwnerCategory findById(long id);
}
