package com.booking.ISAbackend.service;

import com.booking.ISAbackend.dto.CottageDTO;
import com.booking.ISAbackend.dto.NewShipDTO;
import com.booking.ISAbackend.dto.OfferSearchParamsDTO;
import com.booking.ISAbackend.dto.ShipDTO;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.Address;
import com.booking.ISAbackend.model.Ship;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface ShipService {
    List<ShipDTO> findAll() throws IOException;
    List<ShipDTO> findShipByShipOwnerEmail(String email) throws IOException;
    ShipDTO findShipById(Integer id) throws IOException;
    Address findAddressByShipId(Integer id);
    List<ShipDTO> searchShipsClient(OfferSearchParamsDTO params) throws IOException;
    List<ShipDTO> searchShips(String name, Integer maxPeople, String address, Double price) throws IOException;
    List<ShipDTO> searchShipByShipOwner(String name, Integer maxPeople, String address, Double price, String email) throws IOException;
    int addShip(NewShipDTO shipDTO) throws InvalidMotorNumberException, InvalidMaxSpeedException, InvalidSizeException, InvalidMotorPowerException, InvalidPriceException, InvalidPeopleNumberException, ShipAlreadyExistsException, InvalidAddressException, IOException;
    void addAdditionalServices(List<HashMap<String, String>> additionalServiceDTOs, int offerId) throws InvalidPriceException, RequiredFiledException;
    void updateShip(ShipDTO shipDTO, Integer shipId) throws IOException, InvalidPriceException, InvalidRoomNumberException, InvalidBedNumberException, InvalidPeopleNumberException, InvalidAddressException, InvalidMotorNumberException, InvalidMaxSpeedException, InvalidSizeException, InvalidMotorPowerException;
    void updateShipAdditionalServices(List<HashMap<String, String>> newServices, Integer offerID) throws InvalidPriceException, RequiredFiledException;

}
