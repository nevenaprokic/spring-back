package com.booking.ISAbackend.service;


import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.ClientCategory;
import jdk.jfr.Category;

import java.util.List;

public interface ClientCategoryService {
    List<ClientCategory> findAll();

    void updateClientCategory(ClientCategory clientCategoryData) throws OverlappingCategoryBoundaryException, ExistingCategoryNameException, AutomaticallyChangesCategoryIntervalException, InvalidBoundaryException, InvalidPercentException, InvalidPointsNumberException;

    List<ClientCategory> findCategoryByReservationPoints(Integer points);
    void addClientCategory(ClientCategory clientCategoryData) throws ExistingCategoryNameException, OverlappingCategoryBoundaryException, AutomaticallyChangesCategoryIntervalException, InvalidBoundaryException, InvalidPercentException, InvalidPointsNumberException;

    boolean delete(int id);
}
