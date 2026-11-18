package com.belezaearte.salaobeleza.application.port.out.paymentsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Payments;

import java.util.List;

public interface SearchAppointmentPaymentsPort {
    List<Payments> searchAppointment (Integer idAppointment);
}
