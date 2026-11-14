package com.belezaearte.salaobeleza.application.port.in.AppointmentsItems;

import com.belezaearte.salaobeleza.adapter.out.persistence.Services;
import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsItemsDto;

public interface CreateAppointmentsItemsUseCase {
        AppointmentsItemsDto create (AppointmentsItemsDto appointmentsItemsDto);
}
