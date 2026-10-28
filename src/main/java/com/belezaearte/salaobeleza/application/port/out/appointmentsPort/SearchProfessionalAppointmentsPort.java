package com.belezaearte.salaobeleza.application.port.out.appointmentsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;
import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;

import java.util.List;

public interface SearchProfessionalAppointmentsPort {
    List<Appointments> searchProfessional (Professionals idProfessional);
}
