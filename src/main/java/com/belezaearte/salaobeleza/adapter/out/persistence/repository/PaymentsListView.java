package com.belezaearte.salaobeleza.adapter.out.persistence.repository;

import java.math.BigDecimal;
import java.time.Instant;

public interface PaymentsListView {
    Integer getId();
    Integer getIdAppointment();
    String getClientName();
    String getServices();
    Instant getAppointmentDate();
    BigDecimal getAmount();
    String getMethod();
    String getStatus();
    Instant getPaidAt();
    Instant getCreated();
}
