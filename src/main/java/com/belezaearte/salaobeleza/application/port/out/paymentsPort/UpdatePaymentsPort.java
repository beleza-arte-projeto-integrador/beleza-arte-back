package com.belezaearte.salaobeleza.application.port.out.paymentsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Payments;

import java.util.Optional;

public interface UpdatePaymentsPort {
    Optional<Payments> update (Payments payments);
}
