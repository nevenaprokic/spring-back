package com.booking.ISAbackend.service;

import com.booking.ISAbackend.dto.AdventureDTO;
import com.booking.ISAbackend.dto.AdventureDetailsDTO;
import com.booking.ISAbackend.dto.NewAdventureDTO;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.Adventure;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface AdventureService {
    int addAdventure(NewAdventureDTO adventure) throws AdventureAlreadyExistsException, InvalidPriceException, InvalidPeopleNumberException, RequiredFiledException, InvalidAddressException, IOException;

    List<AdventureDTO> getInstructorAdventures(String email) throws IOException;

    void addAdditionalServices(List<HashMap<String, String>> additionalServices, int offerID) throws InvalidPriceException, RequiredFiledException;

    AdventureDetailsDTO findAdventureById(int parseInt) throws AdventureNotFoundException, IOException;

    void updateAdventure(AdventureDTO adventureInfo, int adventureId) throws InvalidPriceException, InvalidPeopleNumberException, RequiredFiledException, InvalidAddressException, IOException;

    Adventure findAdventureByI(int id);

    void updateAdventureAdditionalServices(List<HashMap<String, String>> additionalServices, int offerID) throws InvalidPriceException, RequiredFiledException;

    List<AdventureDTO> searchAdventuresByInstructor(String name, Integer maxPeople, String address, Double price, String email) throws IOException;

    Boolean chechUpdateAllowed(int adventureId);

    List<Adventure> nonAvailableAdventures(LocalDate date);

    List<Adventure> getInstructorsAdventuresById(Integer id) throws IOException;
    List<AdventureDTO> findAll(int page, int pageSize) throws IOException;
}
