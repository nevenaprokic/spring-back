package com.booking.ISAbackend.service;

import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.ClientCategory;
import com.booking.ISAbackend.model.OwnerCategory;

import java.util.List;

public interface OwnerCategoryService {
    List<OwnerCategory> findAll();
    List<OwnerCategory> findByReservationpoints(int points);
    void updateOwnerCategory(OwnerCategory ownerCategoryData) throws OverlappingCategoryBoundaryException, ExistingCategoryNameException, AutomaticallyChangesCategoryIntervalException, InvalidBoundaryException, InvalidPercentException, InvalidPointsNumberException;
    void addOwnerCategory(OwnerCategory ownerCategoryData) throws AutomaticallyChangesCategoryIntervalException, OverlappingCategoryBoundaryException, ExistingCategoryNameException, InvalidBoundaryException, InvalidPercentException, InvalidPointsNumberException;
    boolean delete(int id);
}
