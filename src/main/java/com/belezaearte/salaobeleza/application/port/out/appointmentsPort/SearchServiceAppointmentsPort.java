package com.belezaearte.salaobeleza.application.port.out.appointmentsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;
import com.belezaearte.salaobeleza.adapter.out.persistence.Services;

import java.util.List;

public interface SearchServiceAppointmentsPort {
    List<Appointments> searchService (Services idService);
}
