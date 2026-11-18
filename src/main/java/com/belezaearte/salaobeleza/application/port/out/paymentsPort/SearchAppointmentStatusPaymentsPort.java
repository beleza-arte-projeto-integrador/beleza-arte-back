package com.belezaearte.salaobeleza.application.port.out.paymentsPort;

import java.util.Optional;

public interface SearchAppointmentStatusPaymentsPort {
    Optional<String> searchAppointmentStatus (Integer idAppointment);
}
