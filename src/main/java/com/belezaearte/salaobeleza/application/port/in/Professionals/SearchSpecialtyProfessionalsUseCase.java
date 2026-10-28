package com.belezaearte.salaobeleza.application.port.in.Professionals;

import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalsDto;

import java.util.List;

public interface SearchSpecialtyProfessionalsUseCase {
    List<ProfessionalsDto> searchSpecialty (String specialty);
}
