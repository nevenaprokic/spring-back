package com.booking.ISAbackend.service.impl;

import com.booking.ISAbackend.dto.AdditionalServiceDTO;
import com.booking.ISAbackend.model.AdditionalService;
import com.booking.ISAbackend.model.Adventure;
import com.booking.ISAbackend.model.Offer;
import com.booking.ISAbackend.repository.AdditionalServiceRepository;
import com.booking.ISAbackend.service.AdditionalServiceService;
import com.booking.ISAbackend.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Service
public class AdditionalServiceServiceImpl implements AdditionalServiceService {
    @Autowired
    private AdditionalServiceRepository additionalServiceRepository;

    @Override
    public List<AdditionalService> convertServicesFromDTO(List<HashMap<String, String>> servicesDTO) {
        List<AdditionalService> additionalServices = new ArrayList<AdditionalService>();
        for (HashMap<String, String> serviceMap : servicesDTO) {
            if (!serviceMap.get("serviceName").equals("")) {
                AdditionalService additionalService = new AdditionalService(serviceMap.get("serviceName"), Double.valueOf(serviceMap.get("servicePrice")));
                additionalServices.add(additionalService);
                additionalServiceRepository.save(additionalService);
            }

        }
        return additionalServices;
    }

    @Override
    public boolean isAdditionalServiceExists(List<AdditionalService> currentAdditionalServices, String newServcename){
        for(AdditionalService oldService : currentAdditionalServices){
            if(oldService.getName().equals(newServcename)){
                return true;
            }
        }
        return false;
    }

    @Override
    public AdditionalService findAdditionalService(List<AdditionalService> currentAdditionalServices, String serviceName) {
        for(AdditionalService oldService : currentAdditionalServices){
            if(oldService.getName().equals(serviceName)){
                return oldService;
            }
        }
        return null;
    }

    @Override
    public void removeOfferServices(List<AdditionalService> oldServices, List<HashMap<String, String>> newServices){
        Iterator<AdditionalService> iterator = oldServices.iterator();
        while(iterator.hasNext()){
            AdditionalService service = iterator.next();
            if(isAdditionalServiceRemoved(newServices, service.getName())){
                iterator.remove();
                additionalServiceRepository.delete(service);
            }
        }
    }
    private boolean isAdditionalServiceRemoved(List<HashMap<String, String>> newServices, String oldServiceName){
        for(HashMap<String, String> service: newServices){
            if(service.get("serviceName").equals(oldServiceName)){
                return false;
            }
        }
        return true;
    }

    public List<AdditionalServiceDTO> getAdditionalServices(Offer offer) {
        List<AdditionalServiceDTO> additionalServiceDTOList = new ArrayList<AdditionalServiceDTO>();
        for (AdditionalService service: offer.getAdditionalServices()
        ) {
            AdditionalServiceDTO dto = new AdditionalServiceDTO(service.getName(), service.getPrice());
            additionalServiceDTOList.add(dto);
        }
        return  additionalServiceDTOList;
    }

    @Override
    public void removeAdditionalServices(List<AdditionalService> services){
        Iterator<AdditionalService> iterator = services.iterator();
        while(iterator.hasNext()){
            AdditionalService service = iterator.next();
            iterator.remove();
            additionalServiceRepository.delete(service);

        }
    }
}
