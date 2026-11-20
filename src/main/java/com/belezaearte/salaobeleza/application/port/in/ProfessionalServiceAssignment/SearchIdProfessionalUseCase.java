package com.belezaearte.salaobeleza.application.port.in.ProfessionalServiceAssignment;

import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;
import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalServiceAssignmentDto;

import java.util.List;

public interface SearchIdProfessionalUseCase {
    List<ProfessionalServiceAssignmentDto> searchIdProfessional(Professionals idProfessional);

}
