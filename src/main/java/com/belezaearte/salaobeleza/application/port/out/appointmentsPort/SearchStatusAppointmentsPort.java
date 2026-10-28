package com.belezaearte.salaobeleza.application.port.out.appointmentsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;

import java.util.List;

public interface SearchStatusAppointmentsPort {
    List<Appointments> searchStatus (String status);
}
