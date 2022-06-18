package com.booking.ISAbackend.service.impl;

import com.booking.ISAbackend.model.Client;
import com.booking.ISAbackend.dto.*;
import com.booking.ISAbackend.exceptions.*;
import com.booking.ISAbackend.model.*;
import com.booking.ISAbackend.repository.*;
import com.booking.ISAbackend.service.*;
import com.booking.ISAbackend.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Service
public class AdventureServiceImpl implements AdventureService {
    @Autowired
    private UserService userService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AdventureReporitory adventureRepository;

    @Autowired
    private PhotoRepository photoRepositorys;

    @Autowired
    private AdditionalServiceRepository additionalServiceRepository;

    @Autowired
    private MarkService markService;
    
    @Autowired
    private AdditionalServiceService additionalServiceService;
    @Autowired
    private PhotoService photoService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    @Transactional
    @CacheEvict(value="instructors", allEntries=true)
    public int addAdventure(NewAdventureDTO adventure) throws AdventureAlreadyExistsException, InvalidPriceException, InvalidPeopleNumberException, RequiredFiledException, InvalidAddressException, IOException {
        Instructor instructor = userService.findInstructorByEmail(adventure.getOwnerEmail());
        if(!isAdventureAlreadyExists(adventure.getOfferName(), instructor.getAdventures())){
            if(validateAdventure(adventure)){
                return saveAdventure(adventure, instructor).getId();
            }
        }
        else{
            throw new AdventureAlreadyExistsException("You already have adventure with same name. Name has to be unique!");
        }

        return -1;
    }

    @Override
    @Transactional
    public List<AdventureDTO> getInstructorAdventures(String email) throws IOException { //ubaciti ocenu
        List<Adventure> adventures = adventureRepository.findAdventureByInstructorEmail(email);
        List<AdventureDTO>  adventureDTOList = new ArrayList<AdventureDTO>();
        if(adventures != null){
            for (Adventure a: adventures
                 ) {
                //String ownerEmail, String offerName, String description, String price, List<String> pictures, String peopleNum, String rulesOfConduct, List<AdditionalServiceDTO> additionalServices, String cancelationConditions, String street, String city, String state, String additionalEquipment
                AdventureDTO dto = new AdventureDTO(a.getId(), email,
                        a.getName(),
                        a.getDescription(),
                        String.valueOf(a.getPrice()),
                        getPhoto(a),
                        String.valueOf(a.getNumberOfPerson()),
                        a.getRulesOfConduct(),
                        getAdditionalServices(a),
                        a.getCancellationConditions(),
                        a.getAddress().getStreet(),
                        a.getAddress().getCity(),
                        a.getAddress().getState(),
                        a.getAdditionalEquipment());

                dto.setMark(markService.getMark(a.getId()));
                adventureDTOList.add(dto);

            }

        }
        return adventureDTOList;
    }

    @Override
        public void addAdditionalServices(List<HashMap<String, String>> additionalServiceDTOs, int offerId) throws InvalidPriceException, RequiredFiledException {
        Optional<Adventure> adventure = adventureRepository.findById(offerId);
        if(adventure.isPresent() && Validator.isValidAdditionalServices(additionalServiceDTOs)){
            Adventure a = adventure.get();
            List<AdditionalService> additionalServices = additionalServiceService.convertServicesFromDTO(additionalServiceDTOs);
            a.setAdditionalServices(additionalServices);
            adventureRepository.save(a);
        }

    }

    @Override
    @Transactional
    public AdventureDetailsDTO findAdventureById(int id) throws AdventureNotFoundException, IOException {
        Optional<Adventure> adventure = adventureRepository.findById(id);
        if(adventure.isPresent()){
            Adventure a = adventure.get();
            AdventureDetailsDTO dto = new AdventureDetailsDTO(a.getId(),
                    a.getInstructor().getEmail(),
                    a.getName(),
                    a.getDescription(),
                    String.valueOf(a.getPrice()),
                    convertPhotosToBytes(a.getPhotos()),
                    String.valueOf(a.getNumberOfPerson()),
                    a.getRulesOfConduct(),
                    getAdditionalServices(a),
                    a.getCancellationConditions(),
                    a.getAddress().getStreet(),
                    a.getAddress().getCity(),
                    a.getAddress().getState(),
                    a.getAdditionalEquipment());

            dto.setMark(markService.getMark(a.getId()));

            return dto;
        }
        else{
            throw new AdventureNotFoundException("Adventure not found");
        }
    }

    @Override
    @Transactional
    @CacheEvict(value="instructors", allEntries=true)
    public void updateAdventure(AdventureDTO adventureInfo, int adventureId) throws InvalidPriceException, InvalidPeopleNumberException, RequiredFiledException, InvalidAddressException, IOException {
            Adventure adventure = findAdventureByI(adventureId);
            String instructorEmail = adventure.getInstructor().getEmail();
            if (adventure != null && validateUpdateAdventure(adventureInfo)){
                adventure.setName(adventureInfo.getOfferName());
                adventure.setPrice(Double.valueOf(adventureInfo.getPrice()));
                adventure.setNumberOfPerson(Integer.valueOf(adventureInfo.getPeopleNum()));
                adventure.setDescription(adventureInfo.getDescription());
                adventure.setAdditionalEquipment(adventureInfo.getAdditionalEquipment());
                adventure.setRulesOfConduct(adventureInfo.getRulesOfConduct());
                adventure.setCancellationConditions(adventureInfo.getCancelationConditions());
                adventure.setPhotos(updateAdventurePhotos(adventureInfo.getPhotos(), adventure.getPhotos(), instructorEmail));

                updateAdventuerAddress(adventure.getAddress(), new AddressDTO(adventureInfo.getStreet(), adventureInfo.getCity(), adventureInfo.getState()));
                adventureRepository.save(adventure);
            }
    }

    @Override
    public Adventure findAdventureByI(int id) {
        Optional<Adventure> adventure = adventureRepository.findById(id);
        return adventure.orElse(null);
    }

    @Override
    @Transactional
    public void updateAdventureAdditionalServices(List<HashMap<String, String>> newServices, int offerID) throws InvalidPriceException, RequiredFiledException {
        Adventure adventure = findAdventureByI(offerID);
        List<AdditionalService> serviceToRemove = new ArrayList<AdditionalService>();
        List<AdditionalService> currentAdditionalServices = adventure.getAdditionalServices();
        if(adventure != null && Validator.isValidAdditionalServices(newServices)){
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
            adventure.setAdditionalServices(currentAdditionalServices);
            adventureRepository.save(adventure);

        }
    }

    @Override
    @Transactional
    public List<AdventureDTO> searchAdventuresByInstructor(String name, Integer maxPeople, String address, Double price, String email) throws IOException {
        List<Adventure> matchingAdventures = adventureRepository.searchAdventureByInstructorEmail(name,maxPeople,address,price,email);
        List<AdventureDTO> adventureDTOs = new ArrayList<AdventureDTO>();
        for (Adventure a: matchingAdventures){
            AdventureDTO dto = new AdventureDTO(a.getId(), a.getInstructor().getEmail(), a.getName(), a.getDescription(), String.valueOf(a.getPrice()), getPhoto(a));
            dto.setMark(markService.getMark(a.getId()));
            adventureDTOs.add(dto);
        }
        return adventureDTOs;
    }

    @Override
    public Boolean chechUpdateAllowed(int adventureId) {
        List<Reservation> reservations = reservationRepository.findAllByOfferId(adventureId);
        LocalDate today = LocalDate.now();
        for(Reservation r:reservations){
            if((today.compareTo(r.getEndDate())<0)){
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Adventure> nonAvailableAdventures(LocalDate date) {
        return reservationRepository.nonAvailableAdventures(date);
    }

    @Override
    public List<Adventure> getInstructorsAdventuresById(Integer id) throws IOException {
        Optional<Instructor> i = instructorRepository.findById(id);
        if(i.isPresent())
            return adventureRepository.findAllByInstructor(i.get());
        else
            return new ArrayList<>();
    }


    @Override
    @Transactional
    public List<AdventureDTO> findAll(int page, int pageSize) throws IOException {
        Page<Adventure> adventures = adventureRepository.findAllActiveAdventures(PageRequest.of(page, pageSize));
        int adventuresNum = adventureRepository.getAdventuresNumber();
        List<AdventureDTO> adventureDTOS = new ArrayList<>();
        for(Adventure a: adventures.getContent()){
            String ownerEmail = instructorRepository.findInstructorByAdventure(a.getId()).getEmail();
            AdventureDTO dto = new AdventureDTO(a.getId(), ownerEmail,
                    a.getName(),
                    a.getDescription(),
                    String.valueOf(a.getPrice()),
                    getPhoto(a),
                    String.valueOf(a.getNumberOfPerson()),
                    a.getRulesOfConduct(),
                    getAdditionalServices(a),
                    a.getCancellationConditions(),
                    a.getAddress().getStreet(),
                    a.getAddress().getCity(),
                    a.getAddress().getState(),
                    a.getAdditionalEquipment());

            dto.setMark(markService.getMark(a.getId()));
            dto.setOfferNumber(adventuresNum);
            dto.setOwnerName(a.getInstructor().getFirstName() + " " + a.getInstructor().getLastName());
            adventureDTOS.add(dto);
        }
        return adventureDTOS;
    }


    private List<byte[]> convertPhotosToBytes(List<Photo> photos) throws IOException {
        List<byte[]> photosInBytes = new ArrayList<byte[]>();
        for (Photo p : photos) {
            String folder = "./src/main/frontend/src/components/images/";
            Path path = Paths.get(folder + p.getPath());
            BufferedImage image = ImageIO.read(new FileInputStream(path.toString()));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", out);
            byte[] imageByte = out.toByteArray();

            photosInBytes.add(imageByte);
        }
        return photosInBytes;
    }

    private List<AdditionalServiceDTO> getAdditionalServices(Adventure a) {
        List<AdditionalServiceDTO> additionalServiceDTOList = new ArrayList<AdditionalServiceDTO>();
        for (AdditionalService service: a.getAdditionalServices()
             ) {
            AdditionalServiceDTO dto = new AdditionalServiceDTO(a.getId(), service.getName(), service.getPrice());
            additionalServiceDTOList.add(dto);
        }
        return  additionalServiceDTOList;
    }

    private List<String> getPhoto(Adventure a) throws IOException {
        List<String> photos = new ArrayList<>();
        for(Photo p: a.getPhotos()){
            String pathFile = "./src/main/frontend/src/components/images/" + p.getPath();
            byte[] bytes = Files.readAllBytes(Paths.get(pathFile));
            String photoData = Base64.getEncoder().encodeToString(bytes);
            photos.add(photoData);
        }
        return photos;
    }

    private boolean validateAdventure(NewAdventureDTO adventure) throws InvalidPriceException, InvalidAddressException, RequiredFiledException, InvalidPeopleNumberException {
        boolean validationResult = Validator.isValidPrice(adventure.getPrice()) &&
                                    Validator.isValidAdress(adventure.getStreet(), adventure.getCity(), adventure.getState()) &&
                                    Validator. isValidPeopleNumber(adventure.getPeopleNum()) &&
                                    (!adventure.getCancelationConditions().isEmpty()) &&
                                    (!adventure.getDescription().isEmpty());
        return validationResult;
    }

    private boolean validateUpdateAdventure(AdventureDTO adventure) throws InvalidPriceException, InvalidAddressException, RequiredFiledException, InvalidPeopleNumberException {
        boolean validationResult = Validator.isValidPrice(adventure.getPrice()) &&
                Validator.isValidAdress(adventure.getStreet(), adventure.getCity(), adventure.getState()) &&
                Validator. isValidPeopleNumber(adventure.getPeopleNum()) &&
                (!adventure.getCancelationConditions().isEmpty()) &&
                (!adventure.getDescription().isEmpty());
        return validationResult;
    }

    private Adventure saveAdventure(NewAdventureDTO adventure, Instructor instructor) throws IOException {
        //name, description, price, photos, numberOfPerson, rulesOfConduct, additionalServices, cancellationConditions, deleted, address, quickReservations, reservations, subscribedClients, equipment, intructor

        List<QuickReservation> quickReservations = new ArrayList<QuickReservation>();
        List<Reservation> reservations = new ArrayList<Reservation>();
        List<Client> subscribedClients = new ArrayList<Client>();
        String additionalEquipment = adventure.getAdditionalEquipment();
        Boolean deleted = false;
        Address address = new Address(adventure.getStreet(), adventure.getCity(), adventure.getState());
        List<AdditionalService> additionalServices = new ArrayList<AdditionalService>();

        addressRepository.save(address);

        Adventure newAdventure = new Adventure(adventure.getOfferName(),
                adventure.getDescription(),
                Double.valueOf(adventure.getPrice()),
                photoService.convertPhotosFromDTO(adventure.getPhotos(), instructor.getEmail()),
                Integer.valueOf(adventure.getPeopleNum()),
                adventure.getRulesOfConduct(),
                additionalServices,
                adventure.getCancelationConditions(),
                deleted,
                address,
                quickReservations,
                reservations,
                subscribedClients,
                additionalEquipment,
                instructor);

        newAdventure.setNumberOfReservations(0l);
        newAdventure.setNumberOfModify(0l);
        newAdventure.setNumberOfQuickReservation(0l);
        adventureRepository.save(newAdventure);

        return adventureRepository.save(newAdventure);

    }

    private boolean isAdventureAlreadyExists(String adventureName, List<Adventure> existingAdventures){
        for (Adventure adventure: existingAdventures
             ) {
            if(adventure.getName().equals(adventureName)){
                return true;
            }
        }
        return  false;
    }

    private List<Photo> updateAdventurePhotos(List<String> newPhotos, List<Photo> oldPhotos, String ownerEmail) throws IOException {
        //dobaviti stare slike, obrisati i postaviti nove
        photoService.removeOldPhotos(oldPhotos);
        return photoService.ConvertBase64Photo(newPhotos, ownerEmail);

    }

    private Address updateAdventuerAddress(Address oldAddres, AddressDTO newAddress){
        if(!oldAddres.getStreet().equals(newAddress.getStreet()) |
            !oldAddres.getCity().equals(newAddress.getCity()) |
            !oldAddres.getState().equals(newAddress.getState())){
            Address address = new Address(newAddress.getStreet(), newAddress.getCity(), newAddress.getState());
            addressRepository.save(address);
            return address;
        }
        return oldAddres;

    }
}
