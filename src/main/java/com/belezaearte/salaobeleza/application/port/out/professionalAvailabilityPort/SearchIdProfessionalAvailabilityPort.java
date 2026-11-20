package com.belezaearte.salaobeleza.application.port.out.professionalAvailabilityPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.ProfessionalAvailability;

import java.util.Optional;

public interface SearchIdProfessionalAvailabilityPort {
    // Busca uma faixa de horario pelo id.
    Optional<ProfessionalAvailability> searchId (Integer id);
}
