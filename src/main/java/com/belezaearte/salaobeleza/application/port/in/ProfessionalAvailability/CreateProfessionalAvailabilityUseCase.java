package com.belezaearte.salaobeleza.application.port.in.ProfessionalAvailability;

import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalAvailabilityDto;

public interface CreateProfessionalAvailabilityUseCase {
    ProfessionalAvailabilityDto createProfessionalAvailability (ProfessionalAvailabilityDto professionalAvailabilityDto);
}
