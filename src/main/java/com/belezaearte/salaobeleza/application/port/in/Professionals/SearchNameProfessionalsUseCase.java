package com.belezaearte.salaobeleza.application.port.in.Professionals;

import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalsDto;

import java.util.List;

public interface SearchNameProfessionalsUseCase {
    List<ProfessionalsDto> searchName (String name);
}
