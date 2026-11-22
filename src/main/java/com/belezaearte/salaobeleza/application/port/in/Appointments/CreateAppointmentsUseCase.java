package com.belezaearte.salaobeleza.application.port.in.Appointments;

import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsDto;

public interface CreateAppointmentsUseCase {
    AppointmentsDto create (AppointmentsDto appointmentsDto, Integer idClient);
}
