package com.belezaearte.salaobeleza.application.port.out.AppointmentsItemsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.AppointmentsItems;

import java.util.Optional;

public interface SearchIdAppointmentsItemsPort {
    Optional<AppointmentsItems> searchId (Integer id);
}
