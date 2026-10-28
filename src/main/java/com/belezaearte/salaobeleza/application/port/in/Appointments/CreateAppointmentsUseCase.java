package com.belezaearte.salaobeleza.application.port.in.Appointments;

import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsDto;

public interface CreateAppointmentsUseCase {
    AppointmentsDto createAppointments (AppointmentsDto appointmentsDto, Integer idClient, Integer idProfessional, Integer idService);
}
