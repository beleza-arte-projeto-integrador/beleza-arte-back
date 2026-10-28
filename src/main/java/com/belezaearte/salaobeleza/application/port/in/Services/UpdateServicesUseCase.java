package com.belezaearte.salaobeleza.application.port.in.Services;

import com.belezaearte.salaobeleza.application.domain.dto.ServicesDto;

public interface UpdateServicesUseCase {
    ServicesDto updateServices (ServicesDto servicesDto);
}
