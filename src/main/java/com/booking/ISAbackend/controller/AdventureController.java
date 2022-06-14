package com.booking.ISAbackend.controller;

import com.booking.ISAbackend.dto.*;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.Cottage;
import com.booking.ISAbackend.service.AdventureService;
import com.booking.ISAbackend.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("adventure")
public class AdventureController {

    @Autowired
    private AdventureService adventureService;

    @Autowired
    private OfferService offerService;

    @PostMapping(value = "add-adventure" )
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<String> addAdventure(@RequestParam("email") String ownerEmail,
                                               @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
                                               @RequestParam("offerName") String offerName,
                                               @RequestParam("price") String price,
                                               @RequestParam("description") String description,
                                               @RequestParam("street") String street,
                                               @RequestParam("city") String city,
                                               @RequestParam("state") String state,
                                               @RequestParam("rulesOfConduct") String rulesOfConduct,
                                               @RequestParam("cancelationConditions") String cancelationConditions,
                                               @RequestParam("additionalEquipment") String additionalEquipment,
                                               @RequestParam("peopleNum") String peopleNum){
        //provera da li je ulogovan i autorizacija
        try {
            NewAdventureDTO adventureDTO = new NewAdventureDTO(ownerEmail, offerName, description, price, photos,
                    peopleNum, rulesOfConduct, cancelationConditions,
                    street, city, state, additionalEquipment);

            int adventureId = adventureService.addAdventure(adventureDTO);
            return ResponseEntity.ok(String.valueOf(adventureId));

        }
        catch (InvalidPriceException | AdventureAlreadyExistsException |InvalidPeopleNumberException | RequiredFiledException | InvalidAddressException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("add-additional-services")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<String> addAdditionalServiceForAdventure(@RequestBody Map<String, Object> data){
        try{


            HashMap<String, Object>paramsMap =  (HashMap<String, Object>) data.get("params");
            int id = Integer.parseInt(paramsMap.get("offerId").toString());
            List<HashMap<String, String>> additionalServiceDTOS = (List<HashMap<String, String>>) paramsMap.get("additionalServiceDTOS");

            adventureService.addAdditionalServices(additionalServiceDTOS, id);
            return ResponseEntity.ok().body("Successfully added new adventure");
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("instructor-adventures/{email}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<List<AdventureDTO>> getInstructorAdventures(@PathVariable("email") String email){
        try{
            List<AdventureDTO> adventures = adventureService.getInstructorAdventures(email);
            return ResponseEntity.ok(adventures);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("details")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<AdventureDetailsDTO> getAdventureDetail(@RequestParam String id){
        try{
            AdventureDetailsDTO adventure = adventureService.findAdventureById(Integer.parseInt(id));
            return ResponseEntity.ok(adventure);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("update-adventure")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<String> changeAdventureData(@RequestBody AdventureDTO newAdventureData){
         try{
             adventureService.updateAdventure(newAdventureData, newAdventureData.getId());
             return ResponseEntity.ok().body("");
         }catch (Exception e){
             e.printStackTrace();
             return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
         }
    }

    @PostMapping("update-adventure-services")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<String> changeAdventrueAdditionalServices(@RequestBody Map<String, Object> data){
        try{
            HashMap<String, Object>paramsMap =  (HashMap<String, Object>) data.get("params");
            int id = Integer.parseInt(paramsMap.get("offerId").toString());
            List<HashMap<String, String>> additionalServiceDTOS = (List<HashMap<String, String>>) paramsMap.get("additionalServiceDTOS");

            adventureService.updateAdventureAdditionalServices(additionalServiceDTOS, id);
            return ResponseEntity.ok().body("Successfully change adventure");
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("search-adventures/{email}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<List<AdventureDTO>> searchAdventures(@RequestParam String name, @RequestParam String address, @RequestParam Integer maxPeople, @RequestParam Double price, @PathVariable("email") String email){

        try{
            System.out.println(name + " " + address + " " + maxPeople + " " + price + " " + email);
            List<AdventureDTO> advetures = adventureService.searchAdventuresByInstructor(name, maxPeople, address, price, email);
            return ResponseEntity.ok(advetures);
        }catch  (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("update-allowed/{email}/{adventureId}")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<Boolean> isAllowedAdventureUpdate(@PathVariable("email") String email, @PathVariable("adventureId") int adventureId){
        try{
            Boolean allowedUpdate = adventureService.chechUpdateAllowed(adventureId);
            return ResponseEntity.ok(allowedUpdate);
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("allowed-operation")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<Boolean> isAllowedAdventureOperation(@RequestParam Integer adventureId){
        try{
            Boolean allowedOperation = offerService.checkOperationAllowed(adventureId);
            return ResponseEntity.ok(allowedOperation);
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("delete")
    @PreAuthorize("hasAuthority('INSTRUCTOR')")
    public ResponseEntity<String> deleteAdventure(@RequestParam Integer adventureId){
        try{
            offerService.delete(adventureId);
            return ResponseEntity.ok().body("Successfully delete adventure");
        }catch (OfferNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e.getMessage());
        }catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Something went wrong, please try again.");
        }
    }
}
