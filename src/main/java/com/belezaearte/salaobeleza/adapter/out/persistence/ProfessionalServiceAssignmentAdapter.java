package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ProfessionalServiceAssignmentRepository;
import com.belezaearte.salaobeleza.application.port.out.professionalServiceAssignmentPort.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ProfessionalServiceAssignmentAdapter implements CreateProfessionalServiceAssignmentPort, DeleteProfessionalServiceAssignmentPort, SearchIdProfessionalPort, SearchIdServicePort, UpdateProfessionalServiceAssignmentPort {
    private final ProfessionalServiceAssignmentRepository professionalServiceAssignmentRepository;

    @Override
    public Optional<ProfessionalServiceAssignment> create(ProfessionalServiceAssignment createProfessional) {
        return Optional.of(professionalServiceAssignmentRepository.saveAndFlush(createProfessional));
    }

    @Override
    public void delete(Integer id) {
        professionalServiceAssignmentRepository.deleteById(id);
    }

    @Override
    public List<ProfessionalServiceAssignment> searchIdProfessional(Professionals idProfessional) {
        ProfessionalServiceAssignment professionalServiceAssignment = ProfessionalServiceAssignment.builder().idProfessional(idProfessional).build();
        Example<ProfessionalServiceAssignment> example = Example.of(professionalServiceAssignment);
        return professionalServiceAssignmentRepository.findAll(example);
    }

    @Override
    public List<ProfessionalServiceAssignment> searchIdService(Services idService) {
        ProfessionalServiceAssignment professionalServiceAssignment = ProfessionalServiceAssignment.builder().idService(idService).build();
        Example<ProfessionalServiceAssignment> example = Example.of(professionalServiceAssignment);
        return professionalServiceAssignmentRepository.findAll(example);
    }

    @Override
    public Optional<ProfessionalServiceAssignment> update(ProfessionalServiceAssignment updateProfessionalsService) {
        Optional<ProfessionalServiceAssignment> professionalServiceAssignmentOptional = professionalServiceAssignmentRepository.findById(updateProfessionalsService.getId());
        if (professionalServiceAssignmentOptional.isPresent()) {
            return Optional.of(professionalServiceAssignmentRepository.saveAndFlush(updateProfessionalsService));
        } else {
            return Optional.empty();
        }
    }
}
