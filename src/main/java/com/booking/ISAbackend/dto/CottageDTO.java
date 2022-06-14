package com.booking.ISAbackend.dto;;

import com.booking.ISAbackend.model.AdditionalService;
import com.booking.ISAbackend.model.Adventure;
import com.booking.ISAbackend.model.Cottage;
import com.booking.ISAbackend.model.Photo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CottageDTO {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private List<String> photos;
    private Integer numberOfPerson;
    private String rulesOfConduct;
    private String cancellationConditions;
    private Integer roomNumber;
    private Integer bedNumber;
    private String street;
    private String city;
    private String state;
    private List<AdditionalServiceDTO> additionalServices;
    private Double mark;


    public CottageDTO(Integer id,String name, String description, Double price, List<String> photos, Integer numberOfPerson, String rulesOfConduct, String cancellationConditions, Integer roomNumber, Integer bedNumber) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.photos = photos;
        this.numberOfPerson = numberOfPerson;
        this.rulesOfConduct = rulesOfConduct;
        this.cancellationConditions = cancellationConditions;
        this.roomNumber = roomNumber;
        this.bedNumber = bedNumber;
    }

    public CottageDTO() {

    }

    public CottageDTO(Cottage c) throws IOException {
        this.id = c.getId();
        this.name = c.getName();
        this.description = c.getDescription();
        this.price = c.getPrice();
        this.photos = getPhoto(c);
        this.numberOfPerson = c.getNumberOfPerson();
        this.rulesOfConduct = c.getRulesOfConduct();
        this.cancellationConditions = c.getCancellationConditions();
        this.roomNumber = c.getRoomNumber();
        this.bedNumber = c.getBedNumber();
        this.street = c.getAddress().getStreet();
        this.city = c.getAddress().getCity();
        this.state = c.getAddress().getState();
        this.additionalServices = getAdditionalServices(c);
    }

    private List<String> getPhoto(Cottage c) throws IOException {
        List<String> photos = new ArrayList<>();
        for(Photo p: c.getPhotos()){
            String pathFile = "./src/main/frontend/src/components/images/" + p.getPath();
            byte[] bytes = Files.readAllBytes(Paths.get(pathFile));
            String photoData = Base64.getEncoder().encodeToString(bytes);
            photos.add(photoData);
        }
        return photos;
    }

    private List<AdditionalServiceDTO> getAdditionalServices(Cottage c) {
        List<AdditionalServiceDTO> additionalServiceDTOList = new ArrayList<AdditionalServiceDTO>();
        for (AdditionalService service: c.getAdditionalServices()
        ) {
            AdditionalServiceDTO dto = new AdditionalServiceDTO(service.getId(), service.getName(), service.getPrice());
            additionalServiceDTOList.add(dto);
        }
        return  additionalServiceDTOList;
    }

    public Integer getId() {return  id;}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public Integer getNumberOfPerson() {
        return numberOfPerson;
    }

    public String getRulesOfConduct() {
        return rulesOfConduct;
    }

    public String getCancellationConditions() {
        return cancellationConditions;
    }

    public Integer getRoomNumber(){return  roomNumber;}

    public Integer getBedNumber() {return  bedNumber;}

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public List<AdditionalServiceDTO> getAdditionalServices() {
        return additionalServices;
    }

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
    }
}
