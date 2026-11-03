package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Services;
import com.belezaearte.salaobeleza.application.domain.dto.ServicesDto;
import com.belezaearte.salaobeleza.application.port.in.Services.*;
import com.belezaearte.salaobeleza.application.port.out.servicesPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServicesService implements CreateServicesUseCase,DeleteServicesUseCase,UpdateServicesUseCase,SearchCreatedServicesUseCase,SearchDescriptionServicesUseCase,SearchDurationServicesUseCase,SearchNameServicesUseCase,SearchPriceServicesUseCase {
    @Autowired
    public CreateServicesPort createServicesPort;

    @Override
    public ServicesDto create (ServicesDto servicesDto){
        if(servicesDto.getName() == null || servicesDto.getDescription() == null || servicesDto.getPrice() == null || servicesDto.getDuration_minutes() == null){
            throw new BusinessOutboundException("Erro: Campo não preenchido, por favor verifique se preencheu os dados corretamente");
        }
        Services servicesEntity = Services.builder()
                .id(servicesDto.getId())
                .name(servicesDto.getName())
                .description(servicesDto.getDescription())
                .price(servicesDto.getPrice())
                .duration_minutes(servicesDto.getDuration_minutes())
                .created(servicesDto.getCreated()).build();

        Services serviceSaved = createServicesPort.create(servicesEntity);

        return ServicesDto.builder()
                .name(serviceSaved.getName())
                .description(serviceSaved.getDescription())
                .price(serviceSaved.getPrice())
                .duration_minutes(serviceSaved.getDuration_minutes()).build();
    }
    @Autowired
    private DeleteServicesPort deleteServicesPort;

    @Override
    public void delete(Integer id) {
        deleteServicesPort.delete(id);
    }

    @Autowired
    private UpdateServicesPort updateServicesPort;

    @Override
    public ServicesDto update(ServicesDto servicesDto) {
        Services servicesEntity = Services.builder()
                .id(servicesDto.getId())
                .name(servicesDto.getName())
                .description(servicesDto.getDescription())
                .price(servicesDto.getPrice())
                .duration_minutes(servicesDto.getDuration_minutes())
                .created(servicesDto.getCreated()).build();

        Services servicesUpdate = updateServicesPort.update(servicesEntity);

        if (servicesUpdate == null) {
            throw new BusinessOutboundException("Erro: O Id detectado pode não estar correto, por favor verifique");
        }
        return ServicesDto.builder()
                .id(servicesDto.getId())
                .name(servicesDto.getName())
                .description(servicesDto.getDescription())
                .price(servicesDto.getPrice())
                .duration_minutes(servicesDto.getDuration_minutes())
                .created(servicesDto.getCreated()).build();
    }
    @Autowired
    private SearchCreatedServicesPort searchCreatedServicesPort;

    @Override
    public List<ServicesDto> searchCreated(LocalDateTime searchCreation){
        List<Services> servicesList = searchCreatedServicesPort.searchCreated(searchCreation);

        return servicesList.stream()
                .map(services -> ServicesDto.builder()
                        .id(services.getId())
                        .name(services.getName())
                        .description(services.getDescription())
                        .price(services.getPrice())
                        .duration_minutes(services.getDuration_minutes())
                        .created(services.getCreated())
                        .build())
                .toList();
      }
      @Autowired
    SearchDescriptionServicesPort searchDescriptionServicesPort;

    @Override
    public List<ServicesDto> searchDescription (String description){
        List<Services> servicesList = searchDescriptionServicesPort.searchDescription(description);

        return servicesList.stream().map(services -> ServicesDto.builder()
                        .id(services.getId())
                        .name(services.getName())
                        .description(services.getDescription())
                        .price(services.getPrice())
                        .duration_minutes(services.getDuration_minutes())
                        .created(services.getCreated())
                .build())
                .toList();
    }
    @Autowired
    SearchDurationServicesPort searchDurationServicesPort;

    @Override
    public List<ServicesDto> searchDurationMinuts(Integer duration_minutes){
        List<Services> servicesList = searchDurationServicesPort.searchDurationMinuts(duration_minutes);

        return servicesList.stream().map(services -> ServicesDto.builder()
                        .id(services.getId())
                        .name(services.getName())
                        .description(services.getDescription())
                        .price(services.getPrice())
                        .duration_minutes(services.getDuration_minutes())
                        .created(services.getCreated())
                        .build())
                .toList();
    }
    @Autowired
    SearchNameServicesPort searchNameServicesPort;

    @Override
    public  List<ServicesDto> searchName (String name){
        List<Services> servicesList = searchNameServicesPort.searchName(name);

        return servicesList.stream().map(services -> ServicesDto.builder()
                        .id(services.getId())
                        .name(services.getName())
                        .description(services.getDescription())
                        .created(services.getCreated())
                        .build())
                .toList();
    }
    @Autowired
    SearchPriceServicesPort searchPriceServicesPort;

    @Override
    public List<ServicesDto> searchPrice (BigDecimal price){
        List<Services> servicesList = searchPriceServicesPort.searchPrice(price);

        return servicesList.stream().map(services -> ServicesDto.builder()
                        .id(services.getId())
                        .name(services.getName())
                        .description(services.getDescription())
                        .price(services.getPrice())
                        .duration_minutes(services.getDuration_minutes())
                        .created(services.getCreated())
                        .build())
                .toList();
       }
    }
