package com.booking.ISAbackend.controller;

import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.ClientCategory;
import com.booking.ISAbackend.model.OwnerCategory;
import com.booking.ISAbackend.service.ClientCategoryService;
import com.booking.ISAbackend.service.OwnerCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("loyalty")
public class LoyaltyProgramController {


    @Autowired
    OwnerCategoryService ownerCategoryService;

    @Autowired
    ClientCategoryService clientCategoryService;

    @GetMapping("owner-categories")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COTTAGE_OWNER','INSTRUCTOR','SHIP_OWNER')")
    public ResponseEntity<List<OwnerCategory>> getAllOwnerCategories(){
        try{
            return ResponseEntity.ok(ownerCategoryService.findAll());
        }catch (Exception e){
            e.printStackTrace();;
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("client-categories")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public ResponseEntity<List<ClientCategory>> getAllCLinetCategories(){
        try{
            return ResponseEntity.ok(clientCategoryService.findAll());
        }catch (Exception e){
            e.printStackTrace();;
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("update-client-category")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateLoyaltyClientCategory(@RequestBody ClientCategory updateCategory){
        try {
            clientCategoryService.updateClientCategory(updateCategory);
            return ResponseEntity.ok("Successfully update client category");
        } catch (AutomaticallyChangesCategoryIntervalException e) {
            return ResponseEntity.status(200).body("Successfully update client category, but other boundaries have been moved so that there is no gap between categories .");
        } catch (ExistingCategoryNameException e) {
            return ResponseEntity.status(400).body("Category with the same name already exits");
        } catch (OverlappingCategoryBoundaryException e) {
            return ResponseEntity.status(400).body("The new limit values overlap with the existing ones.");
        }catch (InvalidBoundaryException e) {
            return ResponseEntity.status(400).body("Limit values must be positive hole numbers");
        } catch (InvalidPercentException e) {
            return ResponseEntity.status(400).body("Percent must be number between 0 and 100 with maximum of 2 decimal places are allowed");
        } catch (InvalidPointsNumberException e) {
            return ResponseEntity.status(400).body("Points number must be positive hole number");
        }catch (Exception e){
            return ResponseEntity.status(400).body("Something went wrong");
        }
    }

    @PutMapping("update-owner-category")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateLoyaltyOwnerCategory(@RequestBody OwnerCategory updateCategory){
        try {
            ownerCategoryService.updateOwnerCategory(updateCategory);
            return ResponseEntity.ok("Successfully update owner category");
        } catch (AutomaticallyChangesCategoryIntervalException e) {
            return ResponseEntity.status(200).body("Successfully update owner category, but other boundaries have been moved so that there is no gap between categories .");
        } catch (ExistingCategoryNameException e) {
            return ResponseEntity.status(400).body("Category with the same name already exits");
        } catch (OverlappingCategoryBoundaryException e) {
            return ResponseEntity.status(400).body("The new limit values overlap with the existing ones.");
        }catch (InvalidBoundaryException e) {
            return ResponseEntity.status(400).body("Limit values must be positive hole numbers");
        } catch (InvalidPercentException e) {
            return ResponseEntity.status(400).body("Percent must be number between 0 and 100 with maximum of 2 decimal places are allowed");
        } catch (InvalidPointsNumberException e) {
            return ResponseEntity.status(400).body("Points number must be positive hole number");
        }catch (Exception e){
            return ResponseEntity.status(400).body("Something went wrong");
        }
    }

    @PostMapping("add-client-category")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addClientCategory(@RequestBody ClientCategory clientCategoryData){
        try {
            clientCategoryService.addClientCategory(clientCategoryData);
            return ResponseEntity.ok("Successfully update client category");
        } catch (AutomaticallyChangesCategoryIntervalException e) {
            return ResponseEntity.status(200).body("Successfully update client category, but other boundaries have been moved so that there is no gap between categories .");
        } catch (ExistingCategoryNameException e) {
            return ResponseEntity.status(400).body("Category with the same name already exits");
        } catch (OverlappingCategoryBoundaryException e) {
            return ResponseEntity.status(400).body("The new limit values overlap with the existing ones.");
        }catch (InvalidBoundaryException e) {
            return ResponseEntity.status(400).body("Limit values must be positive hole numbers");
        } catch (InvalidPercentException e) {
            return ResponseEntity.status(400).body("Percent must be number between 0 and 100 with maximum of 2 decimal places are allowed");
        } catch (InvalidPointsNumberException e) {
            return ResponseEntity.status(400).body("Points number must be positive hole number");
        }catch (Exception e){
            return ResponseEntity.status(400).body("Something went wrong");
        }
    }

    @PostMapping("add-owner-category")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addOwnerCategory(@RequestBody OwnerCategory ownerCategoryData){
        try {
            ownerCategoryService.addOwnerCategory(ownerCategoryData);
            return ResponseEntity.ok("Successfully update owner category");
        } catch (AutomaticallyChangesCategoryIntervalException e) {
            return ResponseEntity.status(200).body("Successfully update owner category, but other boundaries have been moved so that there is no gap between categories .");
        } catch (ExistingCategoryNameException e) {
            return ResponseEntity.status(400).body("Category with the same name already exits");
        } catch (OverlappingCategoryBoundaryException e) {
            return ResponseEntity.status(400).body("The new limit values overlap with the existing ones.");
        } catch (InvalidBoundaryException e) {
            return ResponseEntity.status(400).body("Limit values must be positive hole numbers");
        } catch (InvalidPercentException e) {
            return ResponseEntity.status(400).body("Percent must be number between 0 and 100 with maximum of 2 decimal places are allowed");
        } catch (InvalidPointsNumberException e) {
            return ResponseEntity.status(400).body("Points number must be positive hole number");
        }catch (Exception e){
            return ResponseEntity.status(400).body("Something went wrong");
        }
    }

    @DeleteMapping (value = "/delete-client-category/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteClientCategory(@PathVariable("id") int id){

        boolean deleted = clientCategoryService.delete(id);
        if(deleted) return ResponseEntity.ok("Successfully deleted category");
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/delete-owner-category/{is}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteOwnerCategory(@PathVariable("id") int id){

        boolean deleted = ownerCategoryService.delete(id);
        if(deleted) return ResponseEntity.ok("Successfully deleted category");
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
