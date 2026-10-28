package com.belezaearte.salaobeleza.application.port.in.Appointments;

import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsDto;

import java.math.BigDecimal;
import java.util.List;

public interface SearchPriceAppointmentsUseCase {
    List<AppointmentsDto> searchPrice (BigDecimal price);
}
