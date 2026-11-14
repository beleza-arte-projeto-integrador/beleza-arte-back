package com.belezaearte.salaobeleza.adapter.in.web.controller;

import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsItemsDto;
import com.belezaearte.salaobeleza.application.port.in.AppointmentsItems.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointmentsItems")

public class AppointmentsItemsController {
    @Autowired
    private SearchEndDateUseCase searchEndDateUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchEndDate",
            produces = {"application/json"}
    )
    public ResponseEntity<List<AppointmentsItemsDto>> searchEndDate(@RequestParam OffsetDateTime end_at) {
        List<AppointmentsItemsDto> searchEndDate = searchEndDateUseCase.searchEndDate(end_at);

        if (searchEndDate.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(searchEndDate);
    }

    @Autowired
    private SearchStartDateUseCase searchStartDateUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchStartDate",
            produces = {"application/json"}
    )
    public ResponseEntity<List<AppointmentsItemsDto>> SearchStartDate(@RequestParam OffsetDateTime start_at) {
        List<AppointmentsItemsDto> searchStartDate = searchStartDateUseCase.SearchStartDate(start_at);

        if (searchStartDate.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(searchStartDate);
    }

    @Autowired
    private SearchPriceUseCase searchPriceUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchPrice",
            produces = {"application/json"}
    )
    public ResponseEntity<List<AppointmentsItemsDto>> searchPrice(@RequestParam BigDecimal price) {
        List<AppointmentsItemsDto> searchPrice = searchPriceUseCase.searchPrice(price);

        if (searchPrice.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(searchPrice);
    }

    @Autowired
    private CreateAppointmentsItemsUseCase createAppointmentsItemsUseCase;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/createAppointmentsItems",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public AppointmentsItemsDto create(@RequestBody AppointmentsItemsDto appointmentsItemsDto) {
        return createAppointmentsItemsUseCase.create(appointmentsItemsDto);
    }

    @Autowired
    private DeleteAppointmentsItemsUseCase deleteAppointmentsItemsUseCase;

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/deleteAppointmentsItems"
    )
    public ResponseEntity<Void> delete(@RequestParam Integer id) {
        deleteAppointmentsItemsUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    private UpdateAppointmentsItemsUseCase updateAppointmentsItemsUseCase;
    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/updateAppointmentsItems",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public AppointmentsItemsDto update(@RequestBody AppointmentsItemsDto appointmentsItemsDto) {
        return updateAppointmentsItemsUseCase.update(appointmentsItemsDto);
    }
}