package com.belezaearte.salaobeleza.application.port.out.appointmentsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;

import java.util.List;

public interface SearchNotesAppointmentsPort {
    List<Appointments> searchNotes (String notes);
}
