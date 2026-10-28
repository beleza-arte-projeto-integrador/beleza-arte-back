package com.belezaearte.salaobeleza.application.port.in.Professionals;

import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalsDto;

public interface CreateProfessionalsUseCase {
    ProfessionalsDto createProfessionals (ProfessionalsDto professionalsDto);
}
