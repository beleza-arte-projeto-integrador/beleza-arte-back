package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentsSummaryDto {
    private String month;
    private BigDecimal receivedMonth;
    private BigDecimal receivedPreviousMonth;
    private BigDecimal receivedChangePercent;
    private BigDecimal pendingAmount;
    private Long pendingAppointments;
    private BigDecimal receiptRatePercent;
}
