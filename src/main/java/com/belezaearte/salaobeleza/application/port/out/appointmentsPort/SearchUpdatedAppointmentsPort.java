package com.belezaearte.salaobeleza.application.port.out.appointmentsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;

import java.time.OffsetDateTime;
import java.util.List;

public interface SearchUpdatedAppointmentsPort {
    List<Appointments> searchUpdated (OffsetDateTime updated);

}
