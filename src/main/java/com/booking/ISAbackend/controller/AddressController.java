package com.booking.ISAbackend.controller;



import com.booking.ISAbackend.dto.AddressDTO;
import com.booking.ISAbackend.model.Address;
import com.booking.ISAbackend.service.CottageService;
import com.booking.ISAbackend.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private OfferService offerService;

    @GetMapping("get-info")
    public ResponseEntity<AddressDTO> getAddressInfo(@RequestParam String id){
        try{
            AddressDTO address = offerService.findAddressByOfferId(Integer.parseInt(id));

            if(address == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

            return  ResponseEntity.ok(address);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }
}
