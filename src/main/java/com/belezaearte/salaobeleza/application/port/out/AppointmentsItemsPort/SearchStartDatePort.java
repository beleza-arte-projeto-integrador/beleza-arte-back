package com.belezaearte.salaobeleza.application.port.out.AppointmentsItemsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.AppointmentsItems;

import java.time.OffsetDateTime;
import java.util.List;

public interface SearchStartDatePort {
    List<AppointmentsItems> SearchStartDate (OffsetDateTime start_at);
}
