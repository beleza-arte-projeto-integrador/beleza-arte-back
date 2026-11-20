package com.belezaearte.salaobeleza.application.port.in.ProfessionalServiceAssignment;

import com.belezaearte.salaobeleza.adapter.out.persistence.Services;
import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalServiceAssignmentDto;

import java.util.List;

public interface SearchIdServiceUseCase {
    List<ProfessionalServiceAssignmentDto> searchIdService(Services idService);

}
