package com.belezaearte.salaobeleza.application.port.in.Services;

import com.belezaearte.salaobeleza.application.domain.dto.ServicesDto;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchCreatedServicesUseCase {
    List<ServicesDto> searchCreated(LocalDateTime searchCreation);
}
