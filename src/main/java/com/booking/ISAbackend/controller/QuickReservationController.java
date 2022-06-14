package com.booking.ISAbackend.controller;

import com.booking.ISAbackend.dto.NewQuickReservationDTO;
import com.booking.ISAbackend.dto.QuickReservationDTO;
import com.booking.ISAbackend.dto.ReservationParamsDTO;
import com.booking.ISAbackend.exceptions.OfferNotAvailableException;
import com.booking.ISAbackend.service.OfferService;
import com.booking.ISAbackend.service.QuickReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("quick-reservation")
public class QuickReservationController {
    @Autowired
    private QuickReservationService quickReservationService;
    @Autowired
    private OfferService offerService;

    @GetMapping("get")
    public ResponseEntity<List<QuickReservationDTO>> getQuickReservationsByOfferId(@RequestParam String id){
        try {
            List<QuickReservationDTO> quickReservations = quickReservationService.findQuickReservationByOfferId(Integer.parseInt(id));

            return ResponseEntity.ok(quickReservations);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("already-exist")
    @PreAuthorize("hasAnyAuthority('COTTAGE_OWNER','INSTRUCTOR','SHIP_OWNER')")
    public ResponseEntity<Boolean> alreadyExistQuickReservationForOffer(@RequestParam String offerId, @RequestParam String startDate, @RequestParam String dateNumber){
        try {
            Boolean check = quickReservationService.checkQuickReservationByOfferId(Integer.parseInt(offerId), startDate,Integer.parseInt(dateNumber));

            return ResponseEntity.ok(check);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("available-period")
    @PreAuthorize("hasAnyAuthority('COTTAGE_OWNER','INSTRUCTOR','SHIP_OWNER')")
    public ResponseEntity<Boolean> isAvailablePeriod(@RequestParam String offerId, @RequestParam String startDate, @RequestParam String dateNumber){
        try {
            Boolean check = offerService.checkUnavailableDate(Integer.parseInt(offerId), startDate,Integer.parseInt(dateNumber));
            return ResponseEntity.ok(check);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("add")
    @PreAuthorize("hasAnyAuthority('COTTAGE_OWNER','INSTRUCTOR','SHIP_OWNER')")
    public ResponseEntity<String> addNewQuickReservation(@RequestBody NewQuickReservationDTO dto){
        try{
            Integer quickReservationId =  quickReservationService.addNewQuickReservation(dto);
            return ResponseEntity.ok(String.valueOf(quickReservationId));
        }catch (ObjectOptimisticLockingFailureException ex){
            return ResponseEntity.status(400).body("Someone has made reservation for this offer at the same time. Please try again and create new quick reservation.");
        }catch(Exception ex){
            return ResponseEntity.status(400).body("Something went wrong, please try again.");
        }
    }

    @PostMapping("add-additional-services")
    @PreAuthorize("hasAnyAuthority('COTTAGE_OWNER','INSTRUCTOR','SHIP_OWNER')")
    public ResponseEntity<String> addAdditionalServiceForCottage(@RequestBody Map<String, Object> data){
        try{
            HashMap<String, Object> paramsMap =  (HashMap<String, Object>) data.get("params");
            int id = Integer.parseInt(paramsMap.get("quickId").toString());
            List<HashMap<String, String>> additionalServiceDTO = (List<HashMap<String, String>>) paramsMap.get("additionalServiceDTO");

            quickReservationService.addAdditionalServices(additionalServiceDTO, id);
            return ResponseEntity.ok().body("Successfully added new quick reservation");
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
