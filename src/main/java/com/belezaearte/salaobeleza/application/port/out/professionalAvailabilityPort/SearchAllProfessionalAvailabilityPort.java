package com.belezaearte.salaobeleza.application.port.out.professionalAvailabilityPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.ProfessionalAvailability;

import java.util.List;

public interface SearchAllProfessionalAvailabilityPort {
    // Todas as faixas de todos os profissionais (usado no relatorio de ocupacao).
    List<ProfessionalAvailability> searchAll ();
}
