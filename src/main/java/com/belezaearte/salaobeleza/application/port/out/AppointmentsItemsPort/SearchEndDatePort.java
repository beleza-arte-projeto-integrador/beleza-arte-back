package com.belezaearte.salaobeleza.application.port.out.AppointmentsItemsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.AppointmentsItems;

import java.time.OffsetDateTime;
import java.util.List;

public interface SearchEndDatePort {
    List<AppointmentsItems> searchEndDate (OffsetDateTime end_at);
}
