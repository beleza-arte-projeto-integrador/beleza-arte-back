package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.ProfessionalServiceAssignment;
import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;
import com.belezaearte.salaobeleza.adapter.out.persistence.Services;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ProfessionalServiceAssignmentRepository;
import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalServiceAssignmentDto;
import com.belezaearte.salaobeleza.application.port.in.ProfessionalServiceAssignment.*;
import com.belezaearte.salaobeleza.application.port.out.professionalServiceAssignmentPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalServiceAssignmentService implements CreateProfessionalServiceAssignmentUseCase, DeleteProfessionalServiceAssignmentUseCase, SearchIdProfessionalUseCase, SearchIdServiceUseCase, UpdateProfessionalServiceAssignmentUseCase {

    @Autowired
    private CreateProfessionalServiceAssignmentPort createProfessionalServiceAssignmentPort;
    @Autowired
    private DeleteProfessionalServiceAssignmentPort deleteProfessionalServiceAssignmentPort;
    @Autowired
    private SearchIdProfessionalPort searchIdProfessionalPort;
    @Autowired
    private SearchIdServicePort searchIdServicePort;
    @Autowired
    private UpdateProfessionalServiceAssignmentPort updateProfessionalServiceAssignmentPort;
    @Autowired
    private ProfessionalServiceAssignmentRepository professionalServiceAssignmentRepository;

    @Override
    public ProfessionalServiceAssignmentDto createProfessionalsServices(ProfessionalServiceAssignmentDto professionalServiceAssignmentDto, Integer idProfessional, Integer idService) {
        Professionals professionals = Professionals.builder().id(idProfessional).build();
        Services services = Services.builder().id(idService).build();

        ProfessionalServiceAssignment professionalServiceAssignmentEntity = ProfessionalServiceAssignment.builder()
                .id(professionalServiceAssignmentDto.getId())
                .idProfessional(professionals)
                .idService(services)
                .build();
        try {

            Optional<ProfessionalServiceAssignment> professionalServiceAssignmentSaved = createProfessionalServiceAssignmentPort.create(professionalServiceAssignmentEntity);

            return professionalServiceAssignmentDto.builder()
                    .id(professionalServiceAssignmentSaved.get().getId())
                    .idProfessionals(professionalServiceAssignmentSaved.get().getIdProfessional().getId())
                    .idServices(professionalServiceAssignmentSaved.get().getIdService().getId())
                    .build();
        } catch (Exception e) {
            throw new BusinessOutboundException("Could not create the ProfessionalService. Please check whether it has already been registered.");
        }
    }

    @Override
    public void deleteProfessionalServiceAssignmentDto(Integer id) {
        Optional<ProfessionalServiceAssignment> professionalServiceAssignmentOptional = professionalServiceAssignmentRepository.findById(id);

        if (professionalServiceAssignmentOptional.isPresent()) {
            deleteProfessionalServiceAssignmentPort.delete(id);
        } else {
            throw new BusinessOutboundException("Could not delete the ProfessionalService. Please check whether the ProfessionalService exists.");
        }
    }

    @Override
    public List<ProfessionalServiceAssignmentDto> searchIdProfessional(Professionals idProfessional) {
        List<ProfessionalServiceAssignment> professionalServiceAssignments = searchIdProfessionalPort.searchIdProfessional(idProfessional);
        if (!professionalServiceAssignments.isEmpty()) {
            return professionalServiceAssignments.stream().map(professionalServiceAssignment -> ProfessionalServiceAssignmentDto.builder()
                            .id(professionalServiceAssignment.getId())
                            .idProfessionals(professionalServiceAssignment.getIdProfessional().getId())
                            .idServices(professionalServiceAssignment.getIdService().getId())
                            .build())
                    .toList();
        } else {
            throw new BusinessOutboundException("Could not search for the Professional's ID. Please check whether the ID exists.");
        }
    }

    @Override
    public List<ProfessionalServiceAssignmentDto> searchIdService(Services idService) {
        List<ProfessionalServiceAssignment> professionalServiceAssignments = searchIdServicePort.searchIdService(idService);
        if (!professionalServiceAssignments.isEmpty()) {
            return professionalServiceAssignments.stream().map(professionalServiceAssignment -> ProfessionalServiceAssignmentDto.builder()
                            .id(professionalServiceAssignment.getId())
                            .idProfessionals(professionalServiceAssignment.getIdProfessional().getId())
                            .idServices(professionalServiceAssignment.getIdService().getId())
                            .build())
                    .toList();
        } else {
            throw new BusinessOutboundException("Could not search for the Service's ID. Please check whether the ID exists.");
        }
    }

    @Override
    public ProfessionalServiceAssignmentDto updateProfessionalServices(ProfessionalServiceAssignmentDto professionalsServiceDto, Integer idProfessional, Integer idService) {
        Professionals professionals = Professionals.builder().id(idProfessional).build();
        Services services = Services.builder().id(idService).build();

        ProfessionalServiceAssignment professionalServiceAssignmentEntity = ProfessionalServiceAssignment.builder()
                .id(professionalsServiceDto.getId())
                .idProfessional(professionals)
                .idService(services)
                .build();

        Optional<ProfessionalServiceAssignment> professionalServiceAssignmentUpdate = updateProfessionalServiceAssignmentPort.update(professionalServiceAssignmentEntity);

        if (professionalServiceAssignmentUpdate == null) {
            throw new BusinessOutboundException("Could not update the ProfessionalService. Please check whether the Appointment exists.");
        }
            return ProfessionalServiceAssignmentDto.builder()
                    .id(professionalServiceAssignmentUpdate.get().getId())
                    .idProfessionals(professionalServiceAssignmentUpdate.get().getIdProfessional().getId())
                    .idServices(professionalServiceAssignmentUpdate.get().getIdService().getId())
                    .build();

        }

    }
