package com.belezaearte.salaobeleza.adapter.in.web.controller;

import com.belezaearte.salaobeleza.application.domain.dto.ClientsDataExportDto;
import com.belezaearte.salaobeleza.application.domain.dto.ClientsDto;
import com.belezaearte.salaobeleza.application.port.in.Clients.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

// Os caminhos terminam em "Clients" (ex: /searchNameClients) porque
// /searchName, /searchEmail etc. já são usados pelo ProfessionalsController.
// Dois controllers com o mesmo caminho impedem a aplicação de subir.
@RestController
public class ClientsController {

    @Autowired
    private CreateClientsUseCase createClientsUseCase;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/createClients",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ClientsDto createClients(@RequestBody ClientsDto clientsDto) {
        return createClientsUseCase.createClients(clientsDto);
    }

    @Autowired
    private UpdateClientsUseCase updateClientsUseCase;

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/updateClients",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ClientsDto updateClients(@RequestBody ClientsDto clientsDto) {
        return updateClientsUseCase.updateClients(clientsDto);
    }

    @Autowired
    private DeleteClientsUseCase deleteClientsUseCase;

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/deleteClients"
    )
    public ResponseEntity<Void> deleteClients(@RequestParam Integer id) {
        deleteClientsUseCase.deleteClients(id);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    private SearchNameClientsUseCase searchNameClientsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchNameClients",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ClientsDto>> searchName(@RequestParam String name) {
        return ResponseEntity.ok(searchNameClientsUseCase.searchName(name));
    }

    @Autowired
    private SearchPhoneClientsUseCase searchPhoneClientsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchPhoneClients",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ClientsDto>> searchPhone(@RequestParam String phone) {
        return ResponseEntity.ok(searchPhoneClientsUseCase.searchPhone(phone));
    }

    @Autowired
    private SearchEmailClientsUseCase searchEmailClientsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchEmailClients",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ClientsDto>> searchEmail(@RequestParam String email) {
        return ResponseEntity.ok(searchEmailClientsUseCase.searchEmail(email));
    }

    @Autowired
    private SearchCreatedClientsUseCase searchCreatedClientsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchCreatedClients",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ClientsDto>> searchCreatedClients(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime created) {
        return ResponseEntity.ok(searchCreatedClientsUseCase.searchCreatedClients(created));
    }

    // LGPD: devolve todos os dados que o salão guarda sobre o cliente.
    @Autowired
    private ExportClientsDataUseCase exportClientsDataUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/exportClientsData",
            produces = {"application/json"}
    )
    public ResponseEntity<ClientsDataExportDto> exportClientsData(@RequestParam Integer id) {
        return ResponseEntity.ok(exportClientsDataUseCase.exportClientsData(id));
    }

    // LGPD: apaga os dados pessoais do cliente e mantém o histórico de agendamentos.
    @Autowired
    private AnonymizeClientsUseCase anonymizeClientsUseCase;

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/anonymizeClients",
            produces = {"application/json"}
    )
    public ClientsDto anonymizeClients(@RequestParam Integer id) {
        return anonymizeClientsUseCase.anonymizeClients(id);
    }
}
