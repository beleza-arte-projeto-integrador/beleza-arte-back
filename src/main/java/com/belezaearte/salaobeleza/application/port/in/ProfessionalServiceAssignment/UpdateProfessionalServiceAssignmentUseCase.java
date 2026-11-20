package com.belezaearte.salaobeleza.application.port.in.ProfessionalServiceAssignment;

import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalServiceAssignmentDto;

public interface UpdateProfessionalServiceAssignmentUseCase {
    ProfessionalServiceAssignmentDto updateProfessionalServices (ProfessionalServiceAssignmentDto professionalsServiceDto, Integer idProfessional, Integer idService);

}
