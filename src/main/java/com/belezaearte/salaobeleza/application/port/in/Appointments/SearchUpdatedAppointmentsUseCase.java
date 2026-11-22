package com.belezaearte.salaobeleza.application.port.in.Appointments;

import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsDto;

import java.time.OffsetDateTime;
import java.util.List;

public interface SearchUpdatedAppointmentsUseCase {
    List<AppointmentsDto> searchUpdated (OffsetDateTime updated);

}
