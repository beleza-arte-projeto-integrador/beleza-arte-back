package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Resources;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ResourcesRepository;
import com.belezaearte.salaobeleza.application.domain.dto.ResourcesDto;
import com.belezaearte.salaobeleza.application.port.in.Resources.*;
import com.belezaearte.salaobeleza.application.port.out.resourcesPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourcesService implements CreateResourcesUseCase, DeleteResourcesUseCase, SearchActiveResourcesUseCase, SearchNameResourcesUseCase, SearchTypeResourcesUseCase, UpdateResourcesUseCase {

    @Autowired
    private CreateResourcesPort createResourcesPort;
    @Autowired
    private DeleteResourcesPort deleteResourcesPort;
    @Autowired
    private SearchActiveResourcesPort searchActiveResourcesPort;
    @Autowired
    private SearchNameResourcesPort searchNameResourcesPort;
    @Autowired
    private SearchTypeResourcesPort searchTypeResourcesPort;
    @Autowired
    private UpdateResourcesPort updateResourcesPort;
    @Autowired
    private ResourcesRepository resourcesRepository;

    @Override
    public ResourcesDto createResources (ResourcesDto resourcesDto){
        Resources resourcesEntity = Resources.builder()
                .id(resourcesDto.getId())
                .name(resourcesDto.getName())
                .type(resourcesDto.getType())
                .active(resourcesDto.getActive())
                .build();

        try {
            Optional<Resources> resourcesSaved = createResourcesPort.create(resourcesEntity);

            return ResourcesDto.builder()
                    .id(resourcesSaved.get().getId())
                    .name(resourcesSaved.get().getName())
                    .type(resourcesSaved.get().getType())
                    .active(resourcesSaved.get().getActive())
                    .build();
        }
        catch (Exception e){
            throw new BusinessOutboundException("Could not create resources, Check if the resources is already registered");
        }
    }

    @Override
    public void deleteResources(Integer id) {
        Optional<Resources> resourcesOptional = resourcesRepository.findById(id);

        if (resourcesOptional.isPresent()){
            deleteResourcesPort.delete(id);
        }
        else {
            throw new BusinessOutboundException("Could not delete resources! Please check if the resources exist.");
        }
    }

    @Override
    public List<ResourcesDto> searchActive(Boolean active) {
        List<Resources> resources = searchActiveResourcesPort.searchActive(active);
        if (!resources.isEmpty()){
            return resources.stream().map(resource -> ResourcesDto.builder()
                            .id(resource.getId())
                            .name(resource.getName())
                            .type(resource.getType())
                            .active(resource.getActive())
                            .build())
                    .toList();
        }
        else {
            throw new BusinessOutboundException("Unable to locate resources! The requested resources was not found in the system!");
        }
    }

    @Override
    public List<ResourcesDto> searchName(String name) {
        List<Resources> resources = searchNameResourcesPort.searchName(name);
        if (!resources.isEmpty()){
            return resources.stream().map(resource -> ResourcesDto.builder()
                            .id(resource.getId())
                            .name(resource.getName())
                            .type(resource.getType())
                            .active(resource.getActive())
                            .build())
                    .toList();
        }
        else {
            throw new BusinessOutboundException("Unable to locate resources! The requested resources was not found in the system!");
        }
    }

    @Override
    public List<ResourcesDto> searchType(String type) {
        List<Resources> resources = searchTypeResourcesPort.searchType(type);
        if (!resources.isEmpty()){
            return resources.stream().map(resource -> ResourcesDto.builder()
                    .id(resource.getId())
                    .name(resource.getName())
                    .type(resource.getType())
                    .active(resource.getActive())
                    .build())
                    .toList();
        }
        else {
            throw new BusinessOutboundException("Unable to locate resources! The requested resources was not found in the system!");
        }
    }

    @Override
    public ResourcesDto updateResources(ResourcesDto resourcesDto) {
        Resources resourcesEntity = Resources.builder()
                .id(resourcesDto.getId())
                .name(resourcesDto.getName())
                .type(resourcesDto.getType())
                .active(resourcesDto.getActive())
                .build();

        Optional<Resources> resourcesUpdate = updateResourcesPort.update(resourcesEntity);

        if (resourcesUpdate == null){
            throw new BusinessOutboundException("Could not update resources, Check if the resources exists");
        }
        return ResourcesDto.builder()
                .id(resourcesUpdate.get().getId())
                .name(resourcesUpdate.get().getName())
                .type(resourcesUpdate.get().getType())
                .active(resourcesUpdate.get().getActive())
                .build();
    }
}
