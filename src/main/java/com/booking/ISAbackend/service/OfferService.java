package com.booking.ISAbackend.service;

import com.booking.ISAbackend.dto.AdditionalServiceDTO;
import com.booking.ISAbackend.dto.AddressDTO;
import com.booking.ISAbackend.exceptions.OfferNotFoundException;
import com.booking.ISAbackend.model.AdditionalService;
import com.booking.ISAbackend.model.Address;

import java.util.List;

public interface OfferService {
    AddressDTO findAddressByOfferId(Integer id);
    List<AdditionalServiceDTO> findAdditionalServiceByOffer(Integer id);
    void delete(Integer offerId) throws OfferNotFoundException;
    Boolean checkOperationAllowed(Integer offerId);
    Boolean checkUnavailableDate(Integer offerId,String startDate,Integer dateNumber);
}
