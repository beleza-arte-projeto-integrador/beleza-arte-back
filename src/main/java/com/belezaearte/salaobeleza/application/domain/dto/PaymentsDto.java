package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentsDto {
    private Integer id;
    private Integer idAppointment;
    private BigDecimal amount;
    private String method;
    private String status;
    private OffsetDateTime paidAt;
    private OffsetDateTime created;
}
