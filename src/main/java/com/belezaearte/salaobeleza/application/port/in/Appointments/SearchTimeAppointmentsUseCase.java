package com.belezaearte.salaobeleza.application.port.in.Appointments;

import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsDto;

import java.time.OffsetDateTime;
import java.util.List;

public interface SearchTimeAppointmentsUseCase {
    List<AppointmentsDto> searchTime (OffsetDateTime startAt);
}
