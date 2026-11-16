package com.belezaearte.salaobeleza.application.port.in.Appointments;

import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsDto;

import java.util.List;

public interface SearchStatusAppointmentsUseCase {
    List<AppointmentsDto> searchStatus (String status);
}
