package com.belezaearte.salaobeleza.application.port.in.Payments;

import com.belezaearte.salaobeleza.application.domain.dto.PaymentsDto;

public interface PayPaymentsUseCase {
    PaymentsDto payPayments (PaymentsDto paymentsDto);
}
