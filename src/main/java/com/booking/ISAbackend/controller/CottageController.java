package com.booking.ISAbackend.controller;

import com.booking.ISAbackend.dto.AdventureDTO;
import com.booking.ISAbackend.dto.CottageDTO;
import com.booking.ISAbackend.dto.OfferSearchParamsDTO;
import com.booking.ISAbackend.dto.NewCottageDTO;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.service.CottageService;
import com.booking.ISAbackend.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("cottage")
public class CottageController {
    @Autowired
    private CottageService cottageService;
    @Autowired
    private OfferService offerService;

    @GetMapping("get-cottages-by-owner-email")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<List<CottageDTO>> getCottageByCottageOwnerEmail(@RequestParam String email){
        try{
            List<CottageDTO> cottages = cottageService.findCottageByCottageOwnerEmail(email);
            return ResponseEntity.ok(cottages);
        }catch  (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get-info")
    public ResponseEntity<CottageDTO> getCottageInfo(@RequestParam String idCottage){
        try{
            CottageDTO cottage = cottageService.findCottageById(Integer.parseInt(idCottage));

            if(cottage == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

            return  ResponseEntity.ok(cottage);
        }catch  (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get-all")
    public ResponseEntity<List<CottageDTO>> getCottages() throws IOException {
        List<CottageDTO> cottages = cottageService.findAll();
        return ResponseEntity.ok(cottages);
//        try{
//            List<CottageDTO> cottages = cottageService.findAll();
//            return ResponseEntity.ok(cottages);
//        }catch  (Exception e){
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }
    }

    @GetMapping("search")
    public ResponseEntity<List<CottageDTO>> searchCottages(@RequestParam String name, @RequestParam String address, @RequestParam Integer maxPeople, @RequestParam Double price){

        try{
            List<CottageDTO> cottages = cottageService.searchCottages(name, maxPeople, address, price);
            return ResponseEntity.ok(cottages);
        }catch  (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("search-by-owner")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<List<CottageDTO>> searchCottagesByCottageOwner(@RequestParam String name, @RequestParam String address, @RequestParam Integer maxPeople, @RequestParam Double price, @RequestParam String cottageOwnerUsername){
        try{
            List<CottageDTO> cottages = cottageService.searchCottagesByCottageOwner(name, maxPeople, address, price, cottageOwnerUsername);
            return ResponseEntity.ok(cottages);
        }catch  (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("add")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<String> addCottage(@RequestParam("email") String ownerEmail,
                                             @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
                                             @RequestParam("offerName") String offerName,
                                             @RequestParam("price") String price,
                                             @RequestParam("description") String description,
                                             @RequestParam("street") String street,
                                             @RequestParam("city") String city,
                                             @RequestParam("state") String state,
                                             @RequestParam("rulesOfConduct") String rulesOfConduct,
                                             @RequestParam("cancelationConditions") String cancelationConditions,
                                             @RequestParam("peopleNum") String peopleNum,
                                             @RequestParam("roomNumber") String roomNumber,
                                             @RequestParam("bedNumber") String bedNumber){

        try {
            NewCottageDTO cottageDTO = new NewCottageDTO(ownerEmail, offerName, description, price, photos,
                    peopleNum, rulesOfConduct, cancelationConditions,
                    street, city, state, roomNumber, bedNumber);

            int cottageId = cottageService.addCottage(cottageDTO);
            return ResponseEntity.ok(String.valueOf(cottageId));
        } catch (InvalidPriceException | CottageAlreadyExistsException | InvalidPeopleNumberException | InvalidRoomNumberException | InvalidBedNumberException | RequiredFiledException | InvalidAddressException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e.getMessage());
        }catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Something went wrong, please try again.");
        }

    }
    @PostMapping("add-additional-services")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<String> addAdditionalServiceForCottage(@RequestBody Map<String, Object> data){
        try{
            HashMap<String, Object>paramsMap =  (HashMap<String, Object>) data.get("params");
            int id = Integer.parseInt(paramsMap.get("offerId").toString());
            List<HashMap<String, String>> additionalServiceDTO = (List<HashMap<String, String>>) paramsMap.get("additionalServiceDTO");

            cottageService.addAdditionalServices(additionalServiceDTO, id);
            return ResponseEntity.ok().body("Successfully added new cottage");
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("search-client")
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<List<CottageDTO>> searchCottagesClient(@RequestBody OfferSearchParamsDTO params){
        try{
            List<CottageDTO> cottages = cottageService.searchCottagesClient(params);
            return ResponseEntity.ok(cottages);
        }catch  (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("allowed-operation")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<Boolean> isAllowedCottageOperation(@RequestParam Integer cottageId){
        try{
            Boolean allowedOperation = offerService.checkOperationAllowed(cottageId);
            return ResponseEntity.ok(allowedOperation);
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("delete")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<String> deleteCottage(@RequestParam Integer cottageId){
        try{
            offerService.delete(cottageId);
            return ResponseEntity.ok().body("Successfully delete cottage");
        }catch (OfferNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e.getMessage());
        }catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Something went wrong, please try again.");
        }
    }
    @PutMapping("update")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<String> changeCottageData(@RequestBody CottageDTO newCottageData){
        try{
            cottageService.updateCottage(newCottageData, newCottageData.getId());
            return ResponseEntity.ok().body("Successfully update cottage.");
        }catch (ObjectOptimisticLockingFailureException ex){
            return ResponseEntity.status(400).body("Someone has made reservation for this offer at the same time. You can't make change.");
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("update-cottage-services")
    @PreAuthorize("hasAuthority('COTTAGE_OWNER')")
    public ResponseEntity<String> changeCottageAdditionalServices(@RequestBody Map<String, Object> data){
        try{
            HashMap<String, Object>paramsMap =  (HashMap<String, Object>) data.get("params");
            int id = Integer.parseInt(paramsMap.get("offerId").toString());
            List<HashMap<String, String>> additionalServiceDTOS = (List<HashMap<String, String>>) paramsMap.get("additionalServiceDTOS");

            cottageService.updateCottageAdditionalServices(additionalServiceDTOS, id);
            return ResponseEntity.ok().body("Successfully change cottage");
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}