package com.belezaearte.salaobeleza.application.port.in.Payments;

import com.belezaearte.salaobeleza.application.domain.dto.PaymentsRevenueDto;

import java.time.LocalDate;

public interface SearchRevenuePaymentsUseCase {
    PaymentsRevenueDto searchRevenue (String period, LocalDate date);
}
