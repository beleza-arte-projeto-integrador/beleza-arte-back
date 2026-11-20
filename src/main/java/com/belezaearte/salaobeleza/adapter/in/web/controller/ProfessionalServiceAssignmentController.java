package com.belezaearte.salaobeleza.adapter.in.web.controller;

import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;
import com.belezaearte.salaobeleza.adapter.out.persistence.Services;
import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalServiceAssignmentDto;
import com.belezaearte.salaobeleza.application.port.in.ProfessionalServiceAssignment.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProfessionalServiceAssignmentController {

    @Autowired
    private CreateProfessionalServiceAssignmentUseCase createProfessionalServiceAssignmentUseCase;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/createProfessionalServiceAssignment",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ProfessionalServiceAssignmentDto criarCliente(@RequestBody ProfessionalServiceAssignmentDto professionalServiceAssignmentDto) {
        return createProfessionalServiceAssignmentUseCase.createProfessionalsServices(professionalServiceAssignmentDto, professionalServiceAssignmentDto.getIdProfessionals(), professionalServiceAssignmentDto.getIdServices());
    }

    @Autowired
    private DeleteProfessionalServiceAssignmentUseCase deleteProfessionalServiceAssignmentUseCase;

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/deleteProfessionalServiceAssignment"
    )

    public ResponseEntity<Void> deleteProfessionalServiceAssignment(@RequestParam Integer id) {
        deleteProfessionalServiceAssignmentUseCase.deleteProfessionalServiceAssignmentDto(id);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    private SearchIdProfessionalUseCase searchIdProfessionalUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchIdProfessional",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ProfessionalServiceAssignmentDto>> searchIdProfessional(@RequestParam Integer idProfessional) {
        Professionals professionals = Professionals.builder().id(idProfessional).build();
        List<ProfessionalServiceAssignmentDto> professionalServiceAssignmentDto = searchIdProfessionalUseCase.searchIdProfessional(professionals);
        return ResponseEntity.ok(professionalServiceAssignmentDto);
    }

    @Autowired
    private SearchIdServiceUseCase searchIdServiceUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchIdService",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ProfessionalServiceAssignmentDto>> searchIdService(@RequestParam Integer idService) {
        Services services = Services.builder().id(idService).build();
        List<ProfessionalServiceAssignmentDto> professionalServiceAssignmentDto = searchIdServiceUseCase.searchIdService(services);
        return ResponseEntity.ok(professionalServiceAssignmentDto);
    }

    @Autowired
    private UpdateProfessionalServiceAssignmentUseCase updateProfessionalServiceAssignmentUseCase;

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/updateProfessionalServiceAssignment",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ProfessionalServiceAssignmentDto updateProfessionalServices(@RequestBody ProfessionalServiceAssignmentDto professionalsServiceDto) {
        return updateProfessionalServiceAssignmentUseCase.updateProfessionalServices(professionalsServiceDto, professionalsServiceDto.getIdProfessionals(), professionalsServiceDto.getIdServices());
    }

}
