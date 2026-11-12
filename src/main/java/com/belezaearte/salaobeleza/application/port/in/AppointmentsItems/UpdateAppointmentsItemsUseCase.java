package com.belezaearte.salaobeleza.application.port.in.AppointmentsItems;

import com.belezaearte.salaobeleza.adapter.out.persistence.AppointmentsItems;
import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsItemsDto;

public interface UpdateAppointmentsItemsUseCase {
        AppointmentsItemsDto update (AppointmentsItemsDto appointmentsItemsDto);
}
