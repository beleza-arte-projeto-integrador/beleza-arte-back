package com.belezaearte.salaobeleza.application.port.in.Professionals;

import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalsDto;

import java.util.List;

public interface SearchPhoneProfessionalsUseCase {
    List<ProfessionalsDto> searchPhone (String phone);
}
