package com.belezaearte.salaobeleza.application.port.in.Services;

import com.belezaearte.salaobeleza.application.domain.dto.ServicesDto;

import java.util.List;

public interface SearchDescriptionServicesUseCase {
    List<ServicesDto> searchDescription (String description);
}
