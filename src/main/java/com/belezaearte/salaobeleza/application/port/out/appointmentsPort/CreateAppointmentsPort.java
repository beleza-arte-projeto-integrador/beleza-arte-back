package com.belezaearte.salaobeleza.application.port.out.appointmentsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;

import java.util.Optional;

public interface CreateAppointmentsPort {
    Optional<Appointments> create (Appointments appointments);
}
