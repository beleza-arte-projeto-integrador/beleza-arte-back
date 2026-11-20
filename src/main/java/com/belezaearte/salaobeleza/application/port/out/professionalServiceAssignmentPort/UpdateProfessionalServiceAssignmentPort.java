package com.belezaearte.salaobeleza.application.port.out.professionalServiceAssignmentPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.ProfessionalServiceAssignment;

import java.util.Optional;

public interface UpdateProfessionalServiceAssignmentPort {
    Optional<ProfessionalServiceAssignment> update (ProfessionalServiceAssignment updateProfessionalsService);

}
