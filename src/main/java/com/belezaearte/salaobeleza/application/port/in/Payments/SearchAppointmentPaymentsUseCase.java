package com.belezaearte.salaobeleza.application.port.in.Payments;

import com.belezaearte.salaobeleza.application.domain.dto.PaymentsDto;

import java.util.List;

public interface SearchAppointmentPaymentsUseCase {
    List<PaymentsDto> searchAppointment (Integer idAppointment);
}
