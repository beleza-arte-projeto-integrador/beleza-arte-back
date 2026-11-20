package com.belezaearte.salaobeleza.application.port.out.professionalServiceAssignmentPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.ProfessionalServiceAssignment;
import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;

import java.util.List;

public interface SearchIdProfessionalPort {
    List<ProfessionalServiceAssignment> searchIdProfessional (Professionals idProfessional);

}
