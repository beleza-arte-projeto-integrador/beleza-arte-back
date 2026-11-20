package com.belezaearte.salaobeleza.application.port.out.professionalServiceAssignmentPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.ProfessionalServiceAssignment;
import com.belezaearte.salaobeleza.adapter.out.persistence.Services;

import java.util.List;

public interface SearchIdServicePort {
    List<ProfessionalServiceAssignment> searchIdService (Services idService);

}
