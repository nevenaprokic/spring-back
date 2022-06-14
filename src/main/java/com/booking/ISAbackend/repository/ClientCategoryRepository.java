package com.booking.ISAbackend.repository;

import com.booking.ISAbackend.model.ClientCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface ClientCategoryRepository extends JpaRepository<ClientCategory, Integer> {
    List<ClientCategory> findByName(String name);
    List<ClientCategory> findAll();

    ClientCategory findById(long id);

    @Query("SELECT c FROM ClientCategory c WHERE" +
            " (?1 >= c.lowLimitPoints AND ?1 <= c.heighLimitPoints)  OR (?2 >= c.lowLimitPoints AND ?2 <= c.heighLimitPoints)")
    List<ClientCategory> findByInterval(int start, int end);

    @Query("SELECT c FROM ClientCategory c WHERE c.id <> ?1  ORDER BY c.lowLimitPoints DESC")
    ArrayList<ClientCategory> findAllExceptOne(int id);

    @Query("SELECT c FROM ClientCategory c WHERE ?1 >= c.lowLimitPoints AND ?1 <= c.heighLimitPoints")
    List<ClientCategory> findByMatchingInterval(int points);

}
