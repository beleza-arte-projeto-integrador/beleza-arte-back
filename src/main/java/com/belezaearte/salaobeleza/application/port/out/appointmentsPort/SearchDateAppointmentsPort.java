package com.belezaearte.salaobeleza.application.port.out.appointmentsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;

import java.time.OffsetDateTime;
import java.util.List;

public interface SearchDateAppointmentsPort {
    // Agendamentos com algum serviço começando entre start (incluso) e end (não incluso).
    List<Appointments> searchDate (OffsetDateTime start, OffsetDateTime end);
}
