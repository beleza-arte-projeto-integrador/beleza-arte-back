package com.belezaearte.salaobeleza.application.port.out.appointmentsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;

import java.math.BigDecimal;
import java.util.List;

public interface SearchPriceAppointmentsPort {
    List<Appointments> searchPrice (BigDecimal price);
}
