package com.booking.ISAbackend.service.impl;

import com.booking.ISAbackend.dto.*;
import com.booking.ISAbackend.model.Client;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.repository.AdditionalServiceRepository;
import com.booking.ISAbackend.repository.AddressRepository;
import com.booking.ISAbackend.repository.CottageRepository;
import com.booking.ISAbackend.repository.ReservationRepository;
import com.booking.ISAbackend.service.*;
import com.booking.ISAbackend.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CottageServiceImpl implements CottageService {

    @Autowired
    private CottageRepository cottageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AdditionalServiceService additionalServiceService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private MarkService markService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AdditionalServiceRepository additionalServiceRepository;


    @Override
    @Transactional
    public List<CottageDTO> findAll() throws IOException {
        List<Cottage> cottages = cottageRepository.findAll();
        List<CottageDTO> dto = new ArrayList<>();
        for(Cottage c: cottages){
            CottageDTO cottageDTO = new CottageDTO(c);
            cottageDTO.setMark(markService.getMark(c.getId()));
            dto.add(cottageDTO);
        }
        return dto;
    }

    @Override
    @Transactional
    public List<CottageDTO> findCottageByCottageOwnerEmail(String email) throws IOException {
        List<Cottage> cottages = cottageRepository.findCottageByCottageOwnerEmail(email);
        List<CottageDTO> dto = new ArrayList<>();
        for(Cottage c: cottages){
            CottageDTO cottageDTO = new CottageDTO(c);
            cottageDTO.setMark(markService.getMark(c.getId()));
            dto.add(cottageDTO);
        }
        return dto;
    }

    @Override
    @Transactional
    public CottageDTO findCottageById(Integer id) throws IOException {
        Cottage cottage = cottageRepository.findCottageById(id);
        CottageDTO cottageDTO = new CottageDTO(cottage);
        cottageDTO.setMark(markService.getMark(cottage.getId()));
        return  cottageDTO;
    }

    @Override
    public Address findAddressByCottageId(Integer id){
        Cottage cottage = cottageRepository.findCottageById(id);
        Address address = cottage.getAddress();
        return address;
    }

    @Override
    @Transactional
    public List<CottageDTO> searchCottages(String name, Integer maxPeople, String address, Double price) throws IOException {
        List<Cottage> cottages = cottageRepository.searchCottages(name, maxPeople, address, price);
        List<CottageDTO> dto = new ArrayList<>();
        for(Cottage c: cottages){
            CottageDTO cottageDTO = new CottageDTO(c);
            cottageDTO.setMark(markService.getMark(c.getId()));
            dto.add(cottageDTO);
        }
        return dto;
    }

    @Override
    @Transactional
    public List<CottageDTO> searchCottagesClient(OfferSearchParamsDTO params) throws IOException {
        List<Cottage> cottages = cottageRepository.searchCottagesClient(params.getName(), params.getDescription(), params.getAddress());
        List<Cottage> nonAvailableCottages = reservationRepository.nonAvailableCottages(params.getDate());

        List<Cottage> availableCottages = cottages.stream()
                .filter(element -> !nonAvailableCottages.contains(element))
                .collect(Collectors.toList());

        List<CottageDTO> dto = new ArrayList<>();
        for(Cottage c: availableCottages){
            CottageDTO cottageDTO = new CottageDTO(c);
            cottageDTO.setMark(markService.getMark(c.getId()));
            dto.add(cottageDTO);
        }
        return dto;
    }

    @Override
    @Transactional
    public List<CottageDTO> searchCottagesByCottageOwner(String name, Integer maxPeople, String address, Double price, String email) throws IOException {
        List<Cottage> cottages = cottageRepository.searchCottagesByCottageOwnerEmail(name, maxPeople, address, price, email);
        List<CottageDTO> dto = new ArrayList<>();
        for(Cottage c: cottages){
            CottageDTO cottageDTO = new CottageDTO(c);
            dto.add(cottageDTO);
        }
        return dto;
    }

    @Override
    @Transactional
    public int addCottage(NewCottageDTO cottageDTO) throws CottageAlreadyExistsException, InvalidPriceException, InvalidPeopleNumberException, RequiredFiledException, InvalidAddressException, InvalidBedNumberException, InvalidRoomNumberException, IOException {
        CottageOwner cottageOwner = userService.findCottageOwnerByEmail(cottageDTO.getOwnerEmail());
        if(!isCottageAlreadyExists(cottageDTO.getOfferName(), cottageOwner.getCottages())){
            if(validateCottage(cottageDTO)){
                return saveCottage(cottageDTO, cottageOwner).getId();
            }
        }
        else{
            throw new CottageAlreadyExistsException("You already have cottage with same name. Name has to be unique!");
        }
        return -1;

    }
    private boolean isCottageAlreadyExists(String cottageName, List<Cottage> existingCottages){
        for (Cottage cottage: existingCottages) {
            if(cottage.getName().equals(cottageName)){
                return true;
            }
        }
        return  false;
    }
    private boolean validateCottage(NewCottageDTO cottage) throws InvalidPriceException, InvalidAddressException, InvalidPeopleNumberException, InvalidRoomNumberException, InvalidBedNumberException {
        boolean validationResult = Validator.isValidPrice(cottage.getPrice()) &&
                Validator.isValidAdress(cottage.getStreet(), cottage.getCity(), cottage.getState()) &&
                Validator.isValidPeopleNumber(cottage.getPeopleNum()) &&
                Validator.isValidRoomNumber(cottage.getRoomNumber()) &&
                Validator.isValidBedNumber(cottage.getBedNumber()) &&
                (!cottage.getCancelationConditions().isEmpty()) &&
                (!cottage.getDescription().isEmpty());
        return validationResult;
    }

    private Cottage saveCottage(NewCottageDTO cottage, CottageOwner cottageOwner) throws IOException {
        List<QuickReservation> quickReservations = new ArrayList<QuickReservation>();
        List<Reservation> reservations = new ArrayList<Reservation>();
        List<Client> subscribedClients = new ArrayList<Client>();
        List<Photo> photos = new ArrayList<Photo>();
        Boolean deleted = false;
        List<AdditionalService> additionalServices = new ArrayList<AdditionalService>();
        Address address = new Address(cottage.getStreet(), cottage.getCity(), cottage.getState());

        addressRepository.save(address);

        Cottage newCottage = new Cottage(cottage.getOfferName(),
                cottage.getDescription(),
                Double.valueOf(cottage.getPrice()),
                photoService.convertPhotosFromDTO(cottage.getPhotos(), cottageOwner.getEmail()),
                Integer.valueOf(cottage.getPeopleNum()),
                cottage.getRulesOfConduct(),
                additionalServices,
                cottage.getCancelationConditions(),
                deleted,
                address,
                quickReservations,
                reservations,
                subscribedClients,
                Integer.parseInt(cottage.getRoomNumber()),
                Integer.parseInt(cottage.getBedNumber()),
                cottageOwner);

        return cottageRepository.save(newCottage);
    }
    @Override
    public void addAdditionalServices(List<HashMap<String, String>> additionalServiceDTOs, int offerId) throws InvalidPriceException, RequiredFiledException {
        Optional<Cottage> cottage = cottageRepository.findById(offerId);
        if(cottage.isPresent() && Validator.isValidAdditionalServices(additionalServiceDTOs)){
            Cottage c = cottage.get();
            List<AdditionalService> additionalServices = additionalServiceService.convertServicesFromDTO(additionalServiceDTOs);
            c.setAdditionalServices(additionalServices);
            cottageRepository.save(c);
        }

    }

    @Override
    @Transactional
    public void updateCottage(CottageDTO cottageDTO, Integer cottageId) throws IOException, InvalidPriceException, InvalidRoomNumberException, InvalidBedNumberException, InvalidPeopleNumberException, InvalidAddressException, InterruptedException {
        Cottage cottage = cottageRepository.findCottageById(cottageId);
        String cottageOwnerEmail = cottage.getCottageOwner().getEmail();
        System.out.println(cottageDTO.getName());
        if (cottage != null && validateUpdateCottage(cottageDTO)){
            cottage.setName(cottage.getName());
            cottage.setPrice(Double.valueOf(cottageDTO.getPrice()));
            cottage.setNumberOfPerson(Integer.valueOf(cottageDTO.getNumberOfPerson()));
            cottage.setDescription(cottageDTO.getDescription());
            cottage.setRulesOfConduct(cottageDTO.getRulesOfConduct());
            cottage.setCancellationConditions(cottageDTO.getCancellationConditions());
            cottage.setPhotos(updateCottagePhotos(cottageDTO.getPhotos(), cottage.getPhotos(), cottageOwnerEmail));

            cottage.setNumberOfModify(cottage.getNumberOfModify()+1);

            updateCottageAddress(cottage.getAddress(), new AddressDTO(cottageDTO.getStreet(), cottageDTO.getCity(), cottageDTO.getState()));

            Thread.sleep(cottage.getBedNumber()*2000);
            cottageRepository.save(cottage);
        }
    }
    private boolean validateUpdateCottage(CottageDTO cottageDTO) throws InvalidPriceException, InvalidAddressException, InvalidPeopleNumberException, InvalidBedNumberException, InvalidRoomNumberException {
        boolean validationResult = Validator.isValidPrice(String.valueOf(cottageDTO.getPrice())) &&
                Validator.isValidAdress(cottageDTO.getStreet(), cottageDTO.getCity(), cottageDTO.getState()) &&
                Validator.isValidPeopleNumber(String.valueOf(cottageDTO.getNumberOfPerson())) &&
                Validator.isValidBedNumber(String.valueOf(cottageDTO.getBedNumber())) &&
                Validator.isValidRoomNumber(String.valueOf(cottageDTO.getRoomNumber()))&&
                (!cottageDTO.getCancellationConditions().isEmpty()) &&
                (!cottageDTO.getDescription().isEmpty());
        return validationResult;
    }
    private List<Photo> updateCottagePhotos(List<String> newPhotos, List<Photo> oldPhotos, String ownerEmail) throws IOException {
        //dobaviti stare slike, obrisati i postaviti nove
        photoService.removeOldPhotos(oldPhotos);
        return photoService.ConvertBase64Photo(newPhotos, ownerEmail);

    }
    private Address updateCottageAddress(Address oldAddress, AddressDTO newAddress){
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
    public void updateCottageAdditionalServices(List<HashMap<String, String>> newServices, Integer offerID) throws InvalidPriceException, RequiredFiledException {
        Cottage cottage = cottageRepository.findCottageById(offerID);
        List<AdditionalService> currentAdditionalServices = cottage.getAdditionalServices();
        if(cottage != null && Validator.isValidAdditionalServices(newServices)){
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
            cottage.setAdditionalServices(currentAdditionalServices);
            cottageRepository.save(cottage);

        }
    }

}