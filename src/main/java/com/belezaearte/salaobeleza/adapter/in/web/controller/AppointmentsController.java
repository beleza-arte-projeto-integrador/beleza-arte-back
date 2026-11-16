package com.belezaearte.salaobeleza.adapter.in.web.controller;

import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsDto;
import com.belezaearte.salaobeleza.application.port.in.Appointments.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

// Os caminhos terminam em "Appointments" (ex: /searchStatusAppointments), como em Clients,
// para não repetir caminhos que já existem em outros controllers.
@RestController
public class AppointmentsController {

    @Autowired
    private CreateAppointmentsUseCase createAppointmentsUseCase;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/createAppointments",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentsDto createAppointments(@RequestBody AppointmentsDto appointmentsDto) {
        return createAppointmentsUseCase.createAppointments(appointmentsDto);
    }

    @Autowired
    private UpdateAppointmentsUseCase updateAppointmentsUseCase;

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/updateAppointments",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public AppointmentsDto updateAppointments(@RequestBody AppointmentsDto appointmentsDto) {
        return updateAppointmentsUseCase.updateAppointments(appointmentsDto);
    }

    @Autowired
    private DeleteAppointmentsUseCase deleteAppointmentsUseCase;

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/deleteAppointments"
    )
    public ResponseEntity<Void> deleteAppointments(@RequestParam Integer id) {
        deleteAppointmentsUseCase.deleteAppointments(id);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    private SearchClientAppointmentsUseCase searchClientAppointmentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchClientAppointments",
            produces = {"application/json"}
    )
    public ResponseEntity<List<AppointmentsDto>> searchClient(@RequestParam Integer idClient) {
        return ResponseEntity.ok(searchClientAppointmentsUseCase.searchClient(idClient));
    }

    @Autowired
    private SearchProfessionalAppointmentsUseCase searchProfessionalAppointmentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchProfessionalAppointments",
            produces = {"application/json"}
    )
    public ResponseEntity<List<AppointmentsDto>> searchProfessional(@RequestParam Integer idProfessional) {
        return ResponseEntity.ok(searchProfessionalAppointmentsUseCase.searchProfessional(idProfessional));
    }

    @Autowired
    private SearchServiceAppointmentsUseCase searchServiceAppointmentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchServiceAppointments",
            produces = {"application/json"}
    )
    public ResponseEntity<List<AppointmentsDto>> searchService(@RequestParam Integer idService) {
        return ResponseEntity.ok(searchServiceAppointmentsUseCase.searchService(idService));
    }

    @Autowired
    private SearchStatusAppointmentsUseCase searchStatusAppointmentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchStatusAppointments",
            produces = {"application/json"}
    )
    public ResponseEntity<List<AppointmentsDto>> searchStatus(@RequestParam String status) {
        return ResponseEntity.ok(searchStatusAppointmentsUseCase.searchStatus(status));
    }

    @Autowired
    private SearchNotesAppointmentsUseCase searchNotesAppointmentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchNotesAppointments",
            produces = {"application/json"}
    )
    public ResponseEntity<List<AppointmentsDto>> searchNotes(@RequestParam String notes) {
        return ResponseEntity.ok(searchNotesAppointmentsUseCase.searchNotes(notes));
    }

    @Autowired
    private SearchPriceAppointmentsUseCase searchPriceAppointmentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchPriceAppointments",
            produces = {"application/json"}
    )
    public ResponseEntity<List<AppointmentsDto>> searchPrice(@RequestParam BigDecimal price) {
        return ResponseEntity.ok(searchPriceAppointmentsUseCase.searchPrice(price));
    }

    @Autowired
    private SearchDateAppointmentsUseCase searchDateAppointmentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchDateAppointments",
            produces = {"application/json"}
    )
    public ResponseEntity<List<AppointmentsDto>> searchDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(searchDateAppointmentsUseCase.searchDate(date));
    }

    @Autowired
    private SearchTimeAppointmentsUseCase searchTimeAppointmentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchTimeAppointments",
            produces = {"application/json"}
    )
    public ResponseEntity<List<AppointmentsDto>> searchTime(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startAt) {
        return ResponseEntity.ok(searchTimeAppointmentsUseCase.searchTime(startAt));
    }

    @Autowired
    private SearchCreatedAppointmentsUseCase searchCreatedAppointmentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchCreatedAppointments",
            produces = {"application/json"}
    )
    public ResponseEntity<List<AppointmentsDto>> searchCreated(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime created) {
        return ResponseEntity.ok(searchCreatedAppointmentsUseCase.searchCreated(created));
    }
}
