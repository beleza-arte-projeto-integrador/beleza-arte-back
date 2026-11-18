package com.belezaearte.salaobeleza.application.port.out.paymentsPort;

import java.math.BigDecimal;

public interface SearchItemsTotalPaymentsPort {
    BigDecimal searchItemsTotal (Integer idAppointment);
}
