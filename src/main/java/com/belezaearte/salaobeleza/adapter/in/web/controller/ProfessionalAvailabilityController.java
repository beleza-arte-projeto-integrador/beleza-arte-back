package com.belezaearte.salaobeleza.adapter.in.web.controller;

import com.belezaearte.salaobeleza.application.domain.dto.AvailableTimeDto;
import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalAvailabilityDto;
import com.belezaearte.salaobeleza.application.port.in.ProfessionalAvailability.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// Disponibilidade (horario de trabalho) dos profissionais.
// Responsavel: Matheus
@RestController
public class ProfessionalAvailabilityController {

    @Autowired
    private CreateProfessionalAvailabilityUseCase createProfessionalAvailabilityUseCase;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/createProfessionalAvailability",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ProfessionalAvailabilityDto createProfessionalAvailability(@RequestBody ProfessionalAvailabilityDto professionalAvailabilityDto) {
        return createProfessionalAvailabilityUseCase.createProfessionalAvailability(professionalAvailabilityDto);
    }

    @Autowired
    private UpdateProfessionalAvailabilityUseCase updateProfessionalAvailabilityUseCase;

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/updateProfessionalAvailability",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ProfessionalAvailabilityDto updateProfessionalAvailability(@RequestBody ProfessionalAvailabilityDto professionalAvailabilityDto) {
        return updateProfessionalAvailabilityUseCase.updateProfessionalAvailability(professionalAvailabilityDto);
    }

    @Autowired
    private DeleteProfessionalAvailabilityUseCase deleteProfessionalAvailabilityUseCase;

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/deleteProfessionalAvailability"
    )
    public ResponseEntity<Void> deleteProfessionalAvailability(@RequestParam Integer id) {
        deleteProfessionalAvailabilityUseCase.deleteProfessionalAvailability(id);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    private SearchProfessionalAvailabilityUseCase searchProfessionalAvailabilityUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchProfessionalAvailability",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ProfessionalAvailabilityDto>> searchProfessionalAvailability(@RequestParam Integer idProfessional) {
        return ResponseEntity.ok(searchProfessionalAvailabilityUseCase.searchProfessionalAvailability(idProfessional));
    }

    @Autowired
    private SearchAvailableTimesUseCase searchAvailableTimesUseCase;

    // Ex: /searchAvailableTimes?idProfessional=1&date=2026-11-24&durationMinutes=45
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchAvailableTimes",
            produces = {"application/json"}
    )
    public ResponseEntity<List<AvailableTimeDto>> searchAvailableTimes(
            @RequestParam Integer idProfessional,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Integer durationMinutes,
            @RequestParam(required = false) Integer intervalMinutes) {
        return ResponseEntity.ok(searchAvailableTimesUseCase.searchAvailableTimes(idProfessional, date, durationMinutes, intervalMinutes));
    }
}
