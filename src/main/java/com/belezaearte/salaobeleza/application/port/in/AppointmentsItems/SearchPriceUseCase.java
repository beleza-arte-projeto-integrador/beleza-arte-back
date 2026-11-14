package com.belezaearte.salaobeleza.application.port.in.AppointmentsItems;

import com.belezaearte.salaobeleza.adapter.out.persistence.AppointmentsItems;
import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsItemsDto;

import java.math.BigDecimal;
import java.util.List;

public interface SearchPriceUseCase {
    List<AppointmentsItemsDto> searchPrice (BigDecimal price);
}
