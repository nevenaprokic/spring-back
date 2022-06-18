package com.booking.ISAbackend.service.impl;

import com.booking.ISAbackend.dto.*;
import com.booking.ISAbackend.model.Client;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.repository.AdditionalServiceRepository;
import com.booking.ISAbackend.repository.AddressRepository;
import com.booking.ISAbackend.repository.ReservationRepository;
import com.booking.ISAbackend.repository.ShipRepository;
import com.booking.ISAbackend.service.*;
import com.booking.ISAbackend.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShipServiceImpl implements ShipService {
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PhotoService photoService;
    @Autowired
    private AdditionalServiceService additionalServiceService;
    @Autowired
    private MarkService markService;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AdditionalServiceRepository additionalServiceRepository;

    @Override
    @Transactional
    @Cacheable("ships")
    public List<ShipDTO> findAll() throws IOException {
        List<Ship> ships = shipRepository.findAllActiveShips();
        List<ShipDTO> dto = new ArrayList<>();
        for(Ship s: ships){
            ShipDTO shipDTO = new ShipDTO(s);
            shipDTO.setMark(markService.getMark(s.getId()));
            dto.add(shipDTO);
        }
        return dto;
    }

    @Override
    @Transactional
    public List<ShipDTO> findShipByShipOwnerEmail(String email) throws IOException {
        List<Ship> ships = shipRepository.findShipByShipOwnerEmail(email);
        List<ShipDTO> dto = new ArrayList<>();
        for (Ship ship : ships) {
            ShipDTO shipDTO = new ShipDTO(ship);
            shipDTO.setMark(markService.getMark(ship.getId()));
            dto.add(shipDTO);
        }
        return dto;
    }

    @Override
    @Transactional
    public ShipDTO findShipById(Integer id) throws IOException {
        Ship ship = shipRepository.findShipById(id);
        ShipDTO shipDTO = new ShipDTO(ship);
        shipDTO.setMark(markService.getMark(ship.getId()));
        return shipDTO;
    }

    @Override
    public Address findAddressByShipId(Integer id) {
        Ship ship = shipRepository.findShipById(id);
        Address address = ship.getAddress();
        return address;
    }

    @Override
    @Transactional
    public List<ShipDTO> searchShipsClient(OfferSearchParamsDTO params) throws IOException {
        List<Ship> ships = shipRepository.searchShipsClient(params.getName(), params.getDescription(), params.getDescription());
        List<Ship> nonAvailableShips = reservationRepository.nonAvailableShips(params.getDate());

        List<Ship> availableShips = ships.stream()
                .filter(element -> !nonAvailableShips.contains(element))
                .collect(Collectors.toList());

        List<ShipDTO> dto = new ArrayList<>();
        for(Ship c: availableShips){
            ShipDTO shipDTO = new ShipDTO(c);
            shipDTO.setMark(markService.getMark(c.getId()));
            dto.add(shipDTO);
        }
        return dto;
    }

    @Override
    @Transactional
    public List<ShipDTO> searchShips(String name, Integer maxPeople, String address, Double price) throws IOException {
        List<Ship> ships = shipRepository.searchShips(name, maxPeople, address, price);
        List<ShipDTO> dto = new ArrayList<>();
        for(Ship s: ships){
            ShipDTO shipDTO = new ShipDTO(s);
            shipDTO.setMark(markService.getMark(s.getId()));
            dto.add(shipDTO);
        }
        return dto;
    }

    @Override
    @Transactional
    public List<ShipDTO> searchShipByShipOwner(String name, Integer maxPeople, String address, Double price, String email) throws IOException {
        List<Ship> ships = shipRepository.searchShipsByShipOwnerEmail(name, maxPeople, address, price, email);
        List<ShipDTO> dto = new ArrayList<>();
        for(Ship s: ships){
            ShipDTO shipDTO = new ShipDTO(s);
            shipDTO.setMark(markService.getMark(s.getId()));
            dto.add(shipDTO);
        }
        return dto;
    }

    @Override
    @Transactional
    @CacheEvict(value="ships", allEntries=true)
    public int addShip(NewShipDTO shipDTO) throws InvalidMotorNumberException, InvalidPriceException, InvalidMaxSpeedException, InvalidSizeException, InvalidMotorPowerException, InvalidPeopleNumberException, InvalidAddressException, ShipAlreadyExistsException, IOException {
        ShipOwner shipOwner = userService.findShipOwnerByEmail(shipDTO.getOwnerEmail());
        if(!isShipAlreadyExists(shipDTO.getOfferName(), shipOwner.getShips())){
            if(validateShip(shipDTO)){
                return saveShip(shipDTO, shipOwner).getId();
            }
        }
        else{
            throw new ShipAlreadyExistsException("You already have ship with same name. Name has to be unique!");
        }
        return -1;
    }
    private boolean isShipAlreadyExists(String shipName, List<Ship> existingShips){
        for (Ship ship: existingShips) {
            if(ship.getName().equals(shipName)){
                return true;
            }
        }
        return  false;
    }
    private boolean validateShip(NewShipDTO ship) throws  InvalidPriceException, InvalidAddressException, InvalidPeopleNumberException, InvalidSizeException, InvalidMotorNumberException, InvalidMotorPowerException, InvalidMaxSpeedException {
        boolean validationResult = Validator.isValidPrice(ship.getPrice()) &&
                Validator.isValidAdress(ship.getStreet(), ship.getCity(), ship.getState()) &&
                Validator.isValidPeopleNumber(ship.getPeopleNum()) &&
                Validator.isValidSize(ship.getSize()) &&
                Validator.isValidMotorNumber(ship.getMotorNumber()) &&
                Validator.isValidMotorPower(ship.getMotorPower()) &&
                Validator.isValidMaxSpeed(ship.getMaxSpeed()) &&
                (!ship.getType().isEmpty()) &&
                (!ship.getCancelationConditions().isEmpty()) &&
                (!ship.getDescription().isEmpty());
        return validationResult;
    }
    private Ship saveShip(NewShipDTO ship, ShipOwner shipOwner) throws IOException {
        List<QuickReservation> quickReservations = new ArrayList<QuickReservation>();
        List<Reservation> reservations = new ArrayList<Reservation>();
        List<Client> subscribedClients = new ArrayList<Client>();
        List<Photo> photos = new ArrayList<Photo>();
        Boolean deleted = false;
        List<AdditionalService> additionalServices = new ArrayList<AdditionalService>();
        Address address = new Address(ship.getStreet(), ship.getCity(), ship.getState());

        addressRepository.save(address);

        Ship newShip = new Ship(ship.getOfferName(),
                ship.getDescription(),
                Double.valueOf(ship.getPrice()),
                photoService.convertPhotosFromDTO(ship.getPhotos(), shipOwner.getEmail()),
                Integer.valueOf(ship.getPeopleNum()),
                ship.getRulesOfConduct(),
                additionalServices,
                ship.getCancelationConditions(),
                deleted,
                address,
                quickReservations,
                reservations,
                subscribedClients,
                ship.getType(),
                ship.getSize(),
                Integer.parseInt(ship.getMotorNumber()),
                Integer.parseInt(ship.getMotorPower()),
                Integer.parseInt(ship.getMaxSpeed()),
                ship.getNavigationEquipment(),
                ship.getAdditionalEquipment(),
                shipOwner);

        return shipRepository.save(newShip);
    }
    @Override
    public void addAdditionalServices(List<HashMap<String, String>> additionalServiceDTOs, int offerId) throws InvalidPriceException, RequiredFiledException {
        Optional<Ship> ship = shipRepository.findById(offerId);
        if(ship.isPresent() && Validator.isValidAdditionalServices(additionalServiceDTOs)){
            Ship c = ship.get();
            List<AdditionalService> additionalServices = additionalServiceService.convertServicesFromDTO(additionalServiceDTOs);
            c.setAdditionalServices(additionalServices);
            shipRepository.save(c);
        }
    }
    @Override
    @Transactional
    @CacheEvict(value="ships", allEntries=true)
    public void updateShip(ShipDTO shipDTO, Integer shipId) throws IOException, InvalidPriceException, InvalidPeopleNumberException, InvalidAddressException, InvalidMotorNumberException, InvalidMaxSpeedException, InvalidSizeException, InvalidMotorPowerException, InterruptedException {
        Ship ship = shipRepository.findShipById(shipId);
        String shipOwnerEmail = ship.getShipOwner().getEmail();
        if (ship != null && validateUpdateShip(shipDTO)){
            ship.setName(ship.getName());
            ship.setPrice(Double.valueOf(shipDTO.getPrice()));
            ship.setNumberOfPerson(Integer.valueOf(shipDTO.getNumberOfPerson()));
            ship.setDescription(shipDTO.getDescription());
            ship.setRulesOfConduct(shipDTO.getRulesOfConduct());
            ship.setCancellationConditions(shipDTO.getCancellationConditions());
            ship.setPhotos(updateShipPhotos(shipDTO.getPhotos(), ship.getPhotos(), shipOwnerEmail));

            ship.setNumberOfModify(ship.getNumberOfModify()+1);
            updateShipAddress(ship.getAddress(), new AddressDTO(shipDTO.getStreet(), shipDTO.getCity(), shipDTO.getState()));
            shipRepository.save(ship);
        }
    }
    private boolean validateUpdateShip(ShipDTO shipDTO) throws InvalidPriceException, InvalidAddressException, InvalidPeopleNumberException, InvalidSizeException, InvalidMotorNumberException, InvalidMotorPowerException, InvalidMaxSpeedException {
        boolean validationResult = Validator.isValidPrice(String.valueOf(shipDTO.getPrice())) &&
                Validator.isValidAdress(shipDTO.getStreet(), shipDTO.getCity(), shipDTO.getState()) &&
                Validator.isValidPeopleNumber(String.valueOf(shipDTO.getNumberOfPerson())) &&
                Validator.isValidSize(String.valueOf(shipDTO.getSize())) &&
                Validator.isValidMotorNumber(String.valueOf(shipDTO.getMotorNumber())) &&
                Validator.isValidMotorPower(String.valueOf(shipDTO.getMotorPower())) &&
                Validator.isValidMaxSpeed(String.valueOf(shipDTO.getMaxSpeed())) &&
                (!shipDTO.getType().isEmpty()) &&
                (!shipDTO.getCancellationConditions().isEmpty()) &&
                (!shipDTO.getDescription().isEmpty());
        return validationResult;
    }
    private List<Photo> updateShipPhotos(List<String> newPhotos, List<Photo> oldPhotos, String ownerEmail) throws IOException {
        //dobaviti stare slike, obrisati i postaviti nove
        photoService.removeOldPhotos(oldPhotos);
        return photoService.ConvertBase64Photo(newPhotos, ownerEmail);

    }
    private Address updateShipAddress(Address oldAddress, AddressDTO newAddress){
        if(!oldAddress.getStreet().equals(newAddress.getStreet()) |
                !oldAddress.getCity().equals(newAddress.getCity()) |
                !oldAddress.getState().equals(newAddress.getState())){
            Address address = new Address(newAddress.getStreet(), newAddress.getCity(), newAddress.getState());
            addressRepository.save(address);
            return address;
        }
        return oldAddress;

    }

    @Override
    @Transactional
    public void updateShipAdditionalServices(List<HashMap<String, String>> newServices, Integer offerID) throws InvalidPriceException, RequiredFiledException {
        Ship ship = shipRepository.findShipById(offerID);
        List<AdditionalService> currentAdditionalServices = ship.getAdditionalServices();
        if(ship != null && Validator.isValidAdditionalServices(newServices)){
            for(HashMap<String, String> newService:  newServices){
                if(additionalServiceService.isAdditionalServiceExists(currentAdditionalServices, newService.get("serviceName"))){
                    AdditionalService service = additionalServiceService.findAdditionalService(currentAdditionalServices, newService.get("serviceName"));
                    service.setPrice(Double.valueOf(String.valueOf(newService.get("servicePrice"))));
                    additionalServiceRepository.save(service);
                }
                else if(!newService.get("serviceName").equals("")){
                    AdditionalService service = new AdditionalService(newService.get("serviceName"), Double.valueOf(String.valueOf(newService.get("servicePrice"))));
                    currentAdditionalServices.add(service);
                    additionalServiceRepository.save(service);
                }
            }
            additionalServiceService.removeOfferServices(currentAdditionalServices, newServices);
            ship.setAdditionalServices(currentAdditionalServices);
            shipRepository.save(ship);

        }
    }

    @Override
    @Transactional
    public List<ShipDTO> findAllByPages(int page, int pageSize) throws IOException {
        Page<Ship> ships = shipRepository.findAllActiveShipsByPage(PageRequest.of(page, pageSize));
        int shipsNum = shipRepository.getNumberOfShips();
        List<ShipDTO> dto = new ArrayList<>();
        for(Ship s: ships.getContent()){
            ShipDTO shipDTO = new ShipDTO(s);
            shipDTO.setMark(markService.getMark(s.getId()));
            shipDTO.setOfferNumber(shipsNum);
            shipDTO.setOwnerName(s.getShipOwner().getFirstName() + " " + s.getShipOwner().getLastName());
            dto.add(shipDTO);
        }
        return dto;
    }



}
