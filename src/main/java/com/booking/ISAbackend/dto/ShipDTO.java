package com.booking.ISAbackend.dto;

import com.booking.ISAbackend.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ShipDTO {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private List<String> photos;
    private Integer numberOfPerson;
    private String rulesOfConduct;
    private String cancellationConditions;
    private String type;
    private String size;
    private Integer motorNumber;
    private Integer motorPower;
    private Integer maxSpeed;
    private String navigationEquipment;
    private String additionalEquipment;
    private List<AdditionalServiceDTO> additionalServices;
    private Double mark;
    private String street;
    private String city;
    private String state;

    public ShipDTO(Integer id, String name, String description, Double price, List<String> photos, Integer numberOfPerson, String rulesOfConduct, String cancellationConditions, String type, String size, Integer motorNumber, Integer motorPower, Integer maxSpeed, String navigationEquipment, String additionalEquipment) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.photos = photos;
        this.numberOfPerson = numberOfPerson;
        this.rulesOfConduct = rulesOfConduct;
        this.cancellationConditions = cancellationConditions;
        this.type = type;
        this.size = size;
        this.motorNumber = motorNumber;
        this.motorPower = motorPower;
        this.maxSpeed = maxSpeed;
        this.navigationEquipment = navigationEquipment;
        this.additionalEquipment = additionalEquipment;


    }
    public ShipDTO(Ship s) throws IOException {
        this.id = s.getId();
        this.name = s.getName();
        this.description = s.getDescription();
        this.price = s.getPrice();
        this.photos = getPhoto(s);
        this.numberOfPerson = s.getNumberOfPerson();
        this.rulesOfConduct = s.getRulesOfConduct();
        this.cancellationConditions = s.getCancellationConditions();
        this.type = s.getType();
        this.size = s.getSize();
        this.motorNumber = s.getMotorNumber();
        this.motorPower = s.getMotorPower();
        this.maxSpeed = s.getMaxSpeed();
        this.navigationEquipment = s.getNavigationEquipment();
        this.additionalEquipment = s.getAdditionalEquipment();
        this.additionalServices = getAdditionalServices(s);
        this.street = s.getAddress().getStreet();
        this.city = s.getAddress().getCity();
        this.state = s.getAddress().getState();
    }

    private List<String> getPhoto(Ship ship) throws IOException {
        List<String> photos = new ArrayList<>();
        for(Photo p: ship.getPhotos()){
            String pathFile = "./src/main/frontend/src/components/images/" + p.getPath();
            byte[] bytes = Files.readAllBytes(Paths.get(pathFile));
            String photoData = Base64.getEncoder().encodeToString(bytes);
            photos.add(photoData);
        }
        return photos;
    }

    private List<AdditionalServiceDTO> getAdditionalServices(Ship s) {
        List<AdditionalServiceDTO> additionalServiceDTOList = new ArrayList<AdditionalServiceDTO>();
        for (AdditionalService service: s.getAdditionalServices()
        ) {
            AdditionalServiceDTO dto = new AdditionalServiceDTO(service.getId(), service.getName(), service.getPrice());
            additionalServiceDTOList.add(dto);
        }
        return  additionalServiceDTOList;
    }


    public ShipDTO() {

    }

    public Integer getId() {return id;}

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

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public Integer getMotorNumber() {
        return motorNumber;
    }

    public Integer getMotorPower() {
        return motorPower;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public String getNavigationEquipment() {
        return navigationEquipment;
    }

    public String getAdditionalEquipment() {
        return additionalEquipment;
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

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }
}
