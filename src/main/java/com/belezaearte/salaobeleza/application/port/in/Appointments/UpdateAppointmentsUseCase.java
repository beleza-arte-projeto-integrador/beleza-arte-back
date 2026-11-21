package com.belezaearte.salaobeleza.application.port.in.Appointments;

import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsDto;

public interface UpdateAppointmentsUseCase {
    AppointmentsDto updateAppointments(AppointmentsDto appointmentsDto, Integer idClient);
}
