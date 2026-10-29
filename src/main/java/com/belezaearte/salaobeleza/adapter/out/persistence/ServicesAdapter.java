package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ServicesRepository;
import com.belezaearte.salaobeleza.application.port.out.servicesPort.CreateServicesPort;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ServicesAdapter implements CreateServicesPort {
    private final ServicesRepository servicesRepository;
    @Override
    @Transactional
    public Services create(Services services){
        return servicesRepository.saveAndFlush(services);
    }
    public void deleteServices(Integer id){
        if(servicesRepository.existsById(id)){
            servicesRepository.deleteById(id);
        } else{

        }
    }

}
