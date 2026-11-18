package com.belezaearte.salaobeleza.application.port.out.paymentsPort;

import java.math.BigDecimal;

public interface SearchStatusTotalPaymentsPort {
    BigDecimal searchStatusTotal (String status);
}
