package com.belezaearte.salaobeleza.application.port.out.professionalAvailabilityPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.ProfessionalAvailability;

import java.util.Optional;

public interface CreateProfessionalAvailabilityPort {
    Optional<ProfessionalAvailability> create (ProfessionalAvailability professionalAvailability);
}
