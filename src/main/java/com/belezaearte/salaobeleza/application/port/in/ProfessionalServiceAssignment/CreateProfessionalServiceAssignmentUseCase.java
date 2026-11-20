package com.belezaearte.salaobeleza.application.port.in.ProfessionalServiceAssignment;

import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalServiceAssignmentDto;

public interface CreateProfessionalServiceAssignmentUseCase {
    ProfessionalServiceAssignmentDto createProfessionalsServices (ProfessionalServiceAssignmentDto professionalsServiceDto, Integer idProfessional, Integer idService);

}
