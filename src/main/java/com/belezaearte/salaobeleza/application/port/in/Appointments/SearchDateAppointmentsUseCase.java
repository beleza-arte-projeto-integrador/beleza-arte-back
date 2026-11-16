package com.belezaearte.salaobeleza.application.port.in.Appointments;

import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsDto;

import java.time.LocalDate;
import java.util.List;

public interface SearchDateAppointmentsUseCase {
    List<AppointmentsDto> searchDate (LocalDate date);
}
