package com.belezaearte.salaobeleza.application.port.out.appointmentsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;
import com.belezaearte.salaobeleza.adapter.out.persistence.Clients;

import java.util.List;

public interface SearchClientAppointmentsPort {
    List<Appointments> searchClient (Clients idClient);
}
