package com.belezaearte.salaobeleza.application.port.in.Services;

import com.belezaearte.salaobeleza.application.domain.dto.ServicesDto;

import java.math.BigDecimal;
import java.util.List;

public interface SearchPriceServicesUseCase {
    List<ServicesDto> searchPrice (BigDecimal price);
}
