package com.belezaearte.salaobeleza.adapter.in.web.controller;

import com.belezaearte.salaobeleza.application.domain.dto.ResourcesDto;
import com.belezaearte.salaobeleza.application.port.in.Resources.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ResourcesController {

    @Autowired
    private CreateResourcesUseCase createResourcesUseCase;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/createResources",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResourcesDto createdResources(@RequestBody ResourcesDto resourcesDto){
        return createResourcesUseCase.createResources(resourcesDto);
    }

    @Autowired
    private DeleteResourcesUseCase deleteResourcesUseCase;

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/deleteResources"
    )
    public ResponseEntity<Void> deleteResources(@RequestParam Integer id){
        deleteResourcesUseCase.deleteResources(id);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    private SearchActiveResourcesUseCase searchActiveResourcesUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchActiveResources",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ResourcesDto>> searchActive(Boolean active) {
        List<ResourcesDto> resources = searchActiveResourcesUseCase.searchActive(active);
        return ResponseEntity.ok(resources);
    }

    @Autowired
    private SearchNameResourcesUseCase searchNameResourcesUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchNameResources",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ResourcesDto>> searchName(String name) {
        List<ResourcesDto> resources = searchNameResourcesUseCase.searchName(name);
        return ResponseEntity.ok(resources);
    }

    @Autowired
    private SearchTypeResourcesUseCase searchTypeResourcesUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchTypeResources",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ResourcesDto>> searchType(String type) {
        List<ResourcesDto> resources = searchTypeResourcesUseCase.searchType(type);
        return ResponseEntity.ok(resources);
    }

    @Autowired
    private UpdateResourcesUseCase updateResourcesUseCase;

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/updateResources",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResourcesDto updateResources(@RequestBody ResourcesDto resourcesDto){
        return updateResourcesUseCase.updateResources(resourcesDto);
    }
}
