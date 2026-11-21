package com.belezaearte.salaobeleza.adapter.in.web.controller;

import com.belezaearte.salaobeleza.adapter.out.persistence.Clients;
import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsDto;
import com.belezaearte.salaobeleza.application.port.in.Appointments.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public AppointmentsDto createAppointments (@RequestBody AppointmentsDto appointmentsDto) {
        return createAppointmentsUseCase.createAppointments(appointmentsDto, appointmentsDto.getIdClient());
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
        return updateAppointmentsUseCase.updateAppointments(appointmentsDto, appointmentsDto.getIdClient());
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
    private SearchIdClientAppointmentsUseCase searchIdClientAppointmentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchIdClientAppointments",
            produces = {"application/json"}
    )
    public ResponseEntity<List<AppointmentsDto>> searchIdClientAppointments(@RequestParam Integer idClient) {
        Clients clients = Clients.builder().id(idClient).build();
        List<AppointmentsDto> appointmentsDto = searchIdClientAppointmentsUseCase.searchIdClient(clients);
        return ResponseEntity.ok(appointmentsDto);
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
        return ResponseEntity.ok(searchCreatedAppointmentsUseCase.searchCreatedAppointments(created));
    }

    @Autowired
    private SearchUpdatedAppointmentsUseCase searchUpdatedAppointmentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchUpdatedAppointments",
            produces = {"application/json"}
    )
    public ResponseEntity<List<AppointmentsDto>> searchUpdated(@RequestParam OffsetDateTime updated){
        List<AppointmentsDto> searchUpdated = searchUpdatedAppointmentsUseCase.searchUpdatedAppointments(updated);

        if(searchUpdated.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(searchUpdated);
    }

    @Autowired
    private SearchCancelledAppointmentsUseCase searchCancelledAppointmentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchCancelledAppointments",
            produces = {"application/json"}
    )
    public ResponseEntity<List<AppointmentsDto>> searchCancelled(@RequestParam OffsetDateTime cancelled){
        List<AppointmentsDto> searchCancelled = searchCancelledAppointmentsUseCase.searchCancelledAppointments(cancelled);

        if(searchCancelled.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(searchCancelled);
    }

    @Autowired
    private SearchCancelledByAppointmentsUseCase searchCancelledByAppointmentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchCancelledByAppointments",
            produces = {"application/json"}
    )
    public ResponseEntity<List<AppointmentsDto>> searchCancelledBy(@RequestParam String cancelledBy) {
        List<AppointmentsDto> appointmentsDtos = searchCancelledByAppointmentsUseCase.searchCancelledBy(cancelledBy);

        return ResponseEntity.ok(appointmentsDtos);
    }
}
