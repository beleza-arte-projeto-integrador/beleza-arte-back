package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ServicesRepository;
import com.belezaearte.salaobeleza.application.port.out.servicesPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ServicesAdapter implements CreateServicesPort, DeleteServicesPort, UpdateServicesPort, SearchCreatedServicesPort, SearchDescriptionServicesPort, SearchDurationServicesPort, SearchNameServicesPort, SearchPriceServicesPort, SearchCategoryPort {
    private final ServicesRepository servicesRepository;
    @Override
    @Transactional
    public Services create(Services services){
        return servicesRepository.saveAndFlush(services);
    }
    @Override
    public void delete(Integer id){
        if(servicesRepository.existsById(id)){
            servicesRepository.deleteById(id);
        } else{
        throw new BusinessOutboundException("Erro: Verifique se existe o"+ id);
        }
    }
    @Override
    public Services update(Services services){
        Optional<Services> servicesOptional = servicesRepository.findById(services.getId());

        if(servicesOptional.isPresent()){
            Services servicesUpdate = servicesOptional.get();

            servicesUpdate.setName(services.getName());
            servicesUpdate.setCreated(services.getCreated());
            servicesUpdate.setDescription(services.getDescription());
            servicesUpdate.setDuration_minutes(services.getDuration_minutes());
            servicesUpdate.setCategory(services.getCategory());
            servicesUpdate.setPrice(services.getPrice());

            return servicesRepository.saveAndFlush(servicesUpdate);
        }
        return null;
    }
    @Override
    public List<Services> searchCreated(OffsetDateTime searchCreation){
        Services services = Services.builder()
                .created(searchCreation).build();

        Example<Services> example = Example.of(services);
        return servicesRepository.findAll(example);
    }
    @Override
    public List<Services> searchDescription (String description){
        Services services = Services.builder()
                .description(description).build();

        Example<Services> example = Example.of(services);
        return servicesRepository.findAll(example);
    }
    @Override
    public List<Services> searchDurationMinuts(Integer duration_minutes){
        Services services = Services.builder()
                .duration_minutes(duration_minutes).build();

        Example<Services> example = Example.of(services);
        return servicesRepository.findAll(example);
    }
    @Override
    public  List<Services> searchName (String name){
        Services services = Services.builder()
                .name(name).build();

        Example<Services> example = Example.of(services);
        return servicesRepository.findAll(example);
    }
    @Override
    public List<Services> searchPrice (BigDecimal price){
        Services services = Services.builder()
                .price(price).build();

        Example<Services> example = Example.of(services);
        return servicesRepository.findAll(example);
    }
    @Override
    public List<Services> categories (String category){
        Services services = Services.builder()
                .category(category).build();

        Example<Services> example = Example.of(services);
        return servicesRepository.findAll(example);
    }
}
