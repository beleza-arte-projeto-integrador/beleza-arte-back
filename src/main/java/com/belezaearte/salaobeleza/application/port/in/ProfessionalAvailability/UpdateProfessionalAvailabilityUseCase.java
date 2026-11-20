package com.belezaearte.salaobeleza.application.port.in.ProfessionalAvailability;

import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalAvailabilityDto;

public interface UpdateProfessionalAvailabilityUseCase {
    ProfessionalAvailabilityDto updateProfessionalAvailability (ProfessionalAvailabilityDto professionalAvailabilityDto);
}
