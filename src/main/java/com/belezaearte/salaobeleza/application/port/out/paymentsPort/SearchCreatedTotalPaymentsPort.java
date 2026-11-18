package com.belezaearte.salaobeleza.application.port.out.paymentsPort;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface SearchCreatedTotalPaymentsPort {
    BigDecimal searchCreatedTotal (String status, OffsetDateTime start, OffsetDateTime end);
}
