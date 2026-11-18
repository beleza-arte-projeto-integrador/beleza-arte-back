package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentsRevenueDto {
    private String period;
    private LocalDate start;
    private LocalDate end;
    private BigDecimal received;
    private BigDecimal receivedPrevious;
    private BigDecimal changePercent;
}
