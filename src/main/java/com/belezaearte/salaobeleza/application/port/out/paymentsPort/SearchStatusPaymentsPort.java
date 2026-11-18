package com.belezaearte.salaobeleza.application.port.out.paymentsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Payments;

import java.util.List;

public interface SearchStatusPaymentsPort {
    List<Payments> searchStatus (String status);
}
