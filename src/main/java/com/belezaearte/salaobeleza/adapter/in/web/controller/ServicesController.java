package com.belezaearte.salaobeleza.adapter.in.web.controller;

import com.belezaearte.salaobeleza.application.domain.dto.ServicesDto;
import com.belezaearte.salaobeleza.application.port.in.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @Autowired
    private SearchDescriptionServicesUseCase searchDescriptionServicesUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchDescription",
            produces = {"application/json"}
    )

    public ResponseEntity<List<ServicesDto>> searchDescription(@RequestParam String description){
        List<ServicesDto> searchDescription = searchDescriptionServicesUseCase.searchDescription(description);
        return ResponseEntity.ok(searchDescription);
    }
    @Autowired
   private SearchDurationServicesUseCase searchDurationServicesUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchDuration",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ServicesDto>>searchDurationMinuts(@RequestParam Integer duration_minutes){
        List<ServicesDto> searchDuration = searchDurationServicesUseCase.searchDurationMinuts(duration_minutes);
        return ResponseEntity.ok(searchDuration);
    }
    @Autowired
    private SearchNameServicesUseCase searchNameServicesUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchName",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ServicesDto>>searchName (@RequestParam String name){
        List<ServicesDto> searchName = searchNameServicesUseCase.searchName(name);

        if (searchName.isEmpty()) {// corrige o retorno JSON 1 [] no Postman
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(searchName);
    }
    private SearchPriceServicesUseCase searchPriceServicesUseCase;
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchPrice",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ServicesDto>>searchPrice (@RequestParam BigDecimal price){
        List<ServicesDto> searchPrice = searchPriceServicesUseCase.searchPrice(price);
        return ResponseEntity.ok(searchPrice);
        }
}

