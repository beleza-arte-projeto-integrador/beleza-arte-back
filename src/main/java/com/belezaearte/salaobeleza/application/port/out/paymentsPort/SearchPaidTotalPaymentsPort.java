package com.belezaearte.salaobeleza.application.port.out.paymentsPort;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface SearchPaidTotalPaymentsPort {
    BigDecimal searchPaidTotal (OffsetDateTime start, OffsetDateTime end);
}
