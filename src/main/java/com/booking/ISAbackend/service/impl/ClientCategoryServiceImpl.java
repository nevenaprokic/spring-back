package com.booking.ISAbackend.service.impl;

import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.ClientCategory;
import com.booking.ISAbackend.model.OwnerCategory;
import com.booking.ISAbackend.repository.ClientCategoryRepository;
import com.booking.ISAbackend.service.ClientCategoryService;
import com.booking.ISAbackend.validation.Validator;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClientCategoryServiceImpl implements ClientCategoryService {

    @Autowired
    ClientCategoryRepository clientCategoryRepository;

    @Override
    public List<ClientCategory> findAll() {
        return clientCategoryRepository.findAll(Sort.by(Sort.Direction.ASC, "lowLimitPoints"));
    }

    @Override
    public void updateClientCategory(ClientCategory clientCategoryData) throws OverlappingCategoryBoundaryException, ExistingCategoryNameException, AutomaticallyChangesCategoryIntervalException, InvalidBoundaryException, InvalidPercentException, InvalidPointsNumberException {
        ClientCategory category = clientCategoryRepository.findById(clientCategoryData.getId());
        if(checkUniqueCategoryName(clientCategoryData)){
            if(categoryValidation(clientCategoryData)){
                if(checkIntervalOverlaping(clientCategoryData)){
                    clientCategoryRepository.save(clientCategoryData);
                    checkDistanceBetweenCategoryIntervals(category);
                }
            }

        }
    }

    @Override
    public List<ClientCategory> findCategoryByReservationPoints(Integer points) {
        return clientCategoryRepository.findByMatchingInterval(points);
    }

    @Override
    public void addClientCategory(ClientCategory clientCategoryData) throws ExistingCategoryNameException, OverlappingCategoryBoundaryException, AutomaticallyChangesCategoryIntervalException, InvalidBoundaryException, InvalidPercentException, InvalidPointsNumberException {
        if(checkUniqueCategoryName(clientCategoryData)) {
            if(categoryValidation(clientCategoryData)){
                if (checkIntervalOverlaping(clientCategoryData)) {
                    ClientCategory category = clientCategoryRepository.save(clientCategoryData);
                    checkDistanceBetweenCategoryIntervals(category);
                }
            }

        }
    }

    @Override
    public boolean delete(int id) {
        ClientCategory category = clientCategoryRepository.findById(id);
        if(category != null) {
            clientCategoryRepository.delete(category);
            recalculateDistanceBetweenCategories();
            return true;
        }
        return false;
    }

    public boolean checkUniqueCategoryName(ClientCategory category) throws ExistingCategoryNameException {
        List<ClientCategory> categories = clientCategoryRepository.findByName(category.getName());
        if (categories.size() >1 ) throw new ExistingCategoryNameException();
        else if (categories.size() == 1 && categories.get(0).getId() != category.getId()) throw new ExistingCategoryNameException();
        return true;
    }

    public boolean categoryValidation(ClientCategory category) throws InvalidPercentException, InvalidPointsNumberException, InvalidBoundaryException {
        return Validator.isValidPercentNum(category.getDiscount())
                && Validator.isValidBoundaryNum(category.getLowLimitPoints())
                && Validator.isValidBoundaryNum(category.getHeighLimitPoints())
                && Validator.isValidReservationPointsNum(category.getReservationPoints())
                && (category.getLowLimitPoints() <= category.getHeighLimitPoints());
    }


    private boolean checkIntervalOverlaping(ClientCategory category) throws OverlappingCategoryBoundaryException {
        int start = category.getLowLimitPoints();
        int end = category.getHeighLimitPoints();
        String name = category.getName();
        List<ClientCategory> overlapingIntervals = clientCategoryRepository.findByInterval(start, end);
        if (overlapingIntervals.size() > 1)
        {
            throw new OverlappingCategoryBoundaryException();
        }
        if (overlapingIntervals.size() == 1 && !overlapingIntervals.get(0).getId().equals(category.getId()))
        {
            throw new OverlappingCategoryBoundaryException();
        }
        return true;

    }

    private void checkDistanceBetweenCategoryIntervals(ClientCategory changedCategory) throws AutomaticallyChangesCategoryIntervalException {
        List<ClientCategory> categories = clientCategoryRepository.findAll(Sort.by(Sort.Direction.ASC, "lowLimitPoints"));
        boolean changedHappend = false;
        for(int i=1; i< categories.size(); i++) {
            ClientCategory current = categories.get(i);

            ClientCategory previous = categories.get(i-1);
            if (current.getLowLimitPoints() - previous.getHeighLimitPoints() > 1) {
                changedHappend = true;
                if(current.getId().equals(changedCategory.getId())){
                    previous.setHeighLimitPoints(current.getLowLimitPoints() - 1);
                    clientCategoryRepository.save(previous);
                }
                else{
                    current.setLowLimitPoints(previous.getHeighLimitPoints() + 1);
                    clientCategoryRepository.save(current);
                }
            }

            if(changedHappend){
                throw new AutomaticallyChangesCategoryIntervalException();
            }
        }
    }

    private void recalculateDistanceBetweenCategories(){
        List<ClientCategory> categories = clientCategoryRepository.findAll(Sort.by(Sort.Direction.ASC, "lowLimitPoints"));
        for(int i=1; i< categories.size(); i++) {
            ClientCategory current = categories.get(i);
            ClientCategory previous = categories.get(i-1);
            if (current.getLowLimitPoints() - previous.getHeighLimitPoints() > 1) {
                    previous.setHeighLimitPoints(current.getLowLimitPoints() - 1);
                    clientCategoryRepository.save(previous);
                }
            }
    }





}
