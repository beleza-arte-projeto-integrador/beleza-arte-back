package com.belezaearte.salaobeleza.application.port.in.Professionals;

import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;
import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchCreatedProfessionalsUseCase {
    List<ProfessionalsDto> professionalCreated (LocalDateTime created);

}
