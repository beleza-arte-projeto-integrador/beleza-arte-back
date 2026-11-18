package com.belezaearte.salaobeleza.application.port.in.Payments;

import com.belezaearte.salaobeleza.application.domain.dto.PaymentsSummaryDto;

import java.time.YearMonth;

public interface SearchSummaryPaymentsUseCase {
    PaymentsSummaryDto searchSummary (YearMonth month);
}
