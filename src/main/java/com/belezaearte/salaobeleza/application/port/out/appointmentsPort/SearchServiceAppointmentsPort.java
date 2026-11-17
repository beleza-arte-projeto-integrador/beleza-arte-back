package com.belezaearte.salaobeleza.application.port.out.appointmentsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;

import java.util.List;

public interface SearchServiceAppointmentsPort {
    List<Appointments> searchService (Integer idService);
}
