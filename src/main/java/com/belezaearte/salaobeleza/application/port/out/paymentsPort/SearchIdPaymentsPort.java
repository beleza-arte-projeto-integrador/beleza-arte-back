package com.belezaearte.salaobeleza.application.port.out.paymentsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Payments;

import java.util.Optional;

public interface SearchIdPaymentsPort {
    Optional<Payments> searchId (Integer id);
}
