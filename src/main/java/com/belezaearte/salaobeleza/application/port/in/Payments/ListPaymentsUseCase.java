package com.belezaearte.salaobeleza.application.port.in.Payments;

import com.belezaearte.salaobeleza.application.domain.dto.PaymentsListDto;

import java.util.List;

public interface ListPaymentsUseCase {
    List<PaymentsListDto> listPayments (String status);
}
