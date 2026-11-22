package com.belezaearte.salaobeleza.application.port.in.Appointments;

import com.belezaearte.salaobeleza.adapter.out.persistence.Clients;
import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsDto;

import java.util.List;

public interface SearchIdClientAppointmentsUseCase {
    List<AppointmentsDto> searchIdClient(Clients idClient);
}
