package com.belezaearte.salaobeleza.application.port.in.AppointmentsItems;

import com.belezaearte.salaobeleza.adapter.out.persistence.AppointmentsItems;
import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsItemsDto;

import java.time.OffsetDateTime;
import java.util.List;

public interface SearchStartDateUseCase {
    List<AppointmentsItemsDto> SearchStartDate (OffsetDateTime start_at);
}
