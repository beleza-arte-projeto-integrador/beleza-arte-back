package com.belezaearte.salaobeleza.adapter.in.web.controller;

import com.belezaearte.salaobeleza.application.domain.dto.ServicesDto;
import com.belezaearte.salaobeleza.application.port.in.Services.CreateServicesUseCase;
import com.belezaearte.salaobeleza.application.port.in.Services.DeleteServicesUseCase;
import com.belezaearte.salaobeleza.application.port.in.Services.SearchCreatedServicesUseCase;
import com.belezaearte.salaobeleza.application.port.in.Services.UpdateServicesUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/service")

public class ServicesController {
    @Autowired
   private CreateServicesUseCase createServicesUseCase;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/createServices",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ServicesDto create (@RequestBody ServicesDto servicesDto) {

        return createServicesUseCase.create(servicesDto);
    }
    @Autowired
    private DeleteServicesUseCase deleteServicesUseCase;

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/deleteServices"
    )
    public ResponseEntity<Void> delete(@RequestParam Integer id) {
        deleteServicesUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
    @Autowired
    private UpdateServicesUseCase updateServicesUseCase;

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/updateServices",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ServicesDto update(@RequestBody ServicesDto servicesDto) {
        return updateServicesUseCase.update(servicesDto);
    }
    @Autowired
    private SearchCreatedServicesUseCase searchCreatedServicesUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchCreated",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ServicesDto>> searchCreated(@RequestParam LocalDateTime searchCreation){
        List<ServicesDto> searchCreated = searchCreatedServicesUseCase.searchCreated(searchCreation);
        return ResponseEntity.ok(searchCreated);
    }

//SearchDescriptionServicesUseCase,SearchDurationServicesUseCase,SearchNameServicesUseCase,SearchPriceServicesUseCase
}
