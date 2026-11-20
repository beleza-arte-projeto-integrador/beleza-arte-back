package com.belezaearte.salaobeleza.application.port.in.ProfessionalAvailability;

import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalAvailabilityDto;

import java.util.List;

public interface SearchProfessionalAvailabilityUseCase {
    List<ProfessionalAvailabilityDto> searchProfessionalAvailability (Integer idProfessional);
}
