package com.booking.ISAbackend.service;

import com.booking.ISAbackend.dto.CottageDTO;
import com.booking.ISAbackend.dto.OfferSearchParamsDTO;
import com.booking.ISAbackend.dto.NewCottageDTO;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.Address;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface CottageService {
    List<CottageDTO> findAll() throws IOException;
    List<CottageDTO> findCottageByCottageOwnerEmail(String email) throws IOException;
    CottageDTO findCottageById(Integer id) throws IOException;
    Address findAddressByCottageId(Integer id);
    List<CottageDTO> searchCottages(String name, Integer maxPeople, String address, Double price) throws IOException;
    List<CottageDTO> searchCottagesClient(OfferSearchParamsDTO params) throws IOException;
    List<CottageDTO> searchCottagesByCottageOwner(String name, Integer maxPeople, String address, Double price, String email) throws IOException;
    int addCottage(NewCottageDTO cottageDTO) throws CottageAlreadyExistsException, InvalidPriceException, InvalidPeopleNumberException, RequiredFiledException, InvalidAddressException, InvalidBedNumberException, InvalidRoomNumberException, IOException;
    void addAdditionalServices(List<HashMap<String, String>> additionalServiceDTO, int offerId) throws InvalidPriceException, RequiredFiledException;
    void updateCottage(CottageDTO cottageDTO, Integer cottageId) throws IOException, InvalidPriceException, InvalidRoomNumberException, InvalidBedNumberException, InvalidPeopleNumberException, InvalidAddressException, InterruptedException;
    void updateCottageAdditionalServices(List<HashMap<String, String>> newServices, Integer offerID) throws InvalidPriceException, RequiredFiledException;
}