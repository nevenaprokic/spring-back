package com.booking.ISAbackend.dto;

import com.booking.ISAbackend.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class OfferDTO {

    private Integer id;
    private String name;
    private List<String> photos;
    private String street;
    private String city;
    private String state;
    private String description;
    private Double price;
    private String rulesOfConduct;
    private Integer numberOfPerson;
    private List<AdditionalServiceDTO> additionalServices;
    private String cancellationConditions;

    public OfferDTO(){}

    public OfferDTO(Offer o) throws IOException {
        this.id = o.getId();
        this.name = o.getName();
        this.street = o.getAddress().getStreet();
        this.city = o.getAddress().getCity();
        this.state = o.getAddress().getState();
        this.description = o.getDescription();
        this.price = o.getPrice();
        this.rulesOfConduct = o.getRulesOfConduct();
        this.numberOfPerson = o.getNumberOfPerson();
        this.cancellationConditions = o.getCancellationConditions();
        this.photos = getPhoto(o);
        this.additionalServices = getAdditionalServices(o);
    }

    private List<AdditionalServiceDTO> getAdditionalServices(Offer a) {
        List<AdditionalServiceDTO> additionalServiceDTOList = new ArrayList<AdditionalServiceDTO>();
        for (AdditionalService service: a.getAdditionalServices()) {
            AdditionalServiceDTO dto = new AdditionalServiceDTO(a.getId(), service.getName(), service.getPrice());
            additionalServiceDTOList.add(dto);
        }
        return  additionalServiceDTOList;
    }

    private List<String> getPhoto(Offer a) throws IOException {
        List<String> photos = new ArrayList<>();
        for(Photo p: a.getPhotos()){
            String pathFile = "./src/main/frontend/src/components/images/" + p.getPath();
            byte[] bytes = Files.readAllBytes(Paths.get(pathFile));
            String photoData = Base64.getEncoder().encodeToString(bytes);
            photos.add(photoData);
        }
        return photos;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getRulesOfConduct() {
        return rulesOfConduct;
    }

    public Integer getNumberOfPerson() {
        return numberOfPerson;
    }

    public List<AdditionalServiceDTO> getAdditionalServices() {
        return additionalServices;
    }

    public String getCancellationConditions() {
        return cancellationConditions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setRulesOfConduct(String rulesOfConduct) {
        this.rulesOfConduct = rulesOfConduct;
    }

    public void setNumberOfPerson(Integer numberOfPerson) {
        this.numberOfPerson = numberOfPerson;
    }

    public void setCancellationConditions(String cancellationConditions) {
        this.cancellationConditions = cancellationConditions;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public void setAdditionalServices(List<AdditionalServiceDTO> additionalServices) {
        this.additionalServices = additionalServices;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public Integer getId() {
        return id;
    }
}
