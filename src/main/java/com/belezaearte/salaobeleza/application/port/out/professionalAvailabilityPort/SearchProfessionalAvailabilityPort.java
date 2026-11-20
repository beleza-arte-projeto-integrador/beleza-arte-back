package com.belezaearte.salaobeleza.application.port.out.professionalAvailabilityPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.ProfessionalAvailability;

import java.util.List;

public interface SearchProfessionalAvailabilityPort {
    // Todas as faixas de um profissional.
    List<ProfessionalAvailability> searchProfessional (Integer professionalId);

    // Faixas de um profissional em um dia da semana (0 = domingo ... 6 = sabado).
    List<ProfessionalAvailability> searchProfessionalWeekday (Integer professionalId, Short weekday);
}
