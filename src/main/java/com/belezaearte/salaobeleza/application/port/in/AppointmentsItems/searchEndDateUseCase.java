package com.belezaearte.salaobeleza.application.port.in.AppointmentsItems;

import com.belezaearte.salaobeleza.adapter.out.persistence.AppointmentsItems;
import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsItemsDto;

import java.time.OffsetDateTime;
import java.util.List;

public interface searchEndDateUseCase {
    List<AppointmentsItemsDto> searchEndDate (OffsetDateTime end_at);
}
