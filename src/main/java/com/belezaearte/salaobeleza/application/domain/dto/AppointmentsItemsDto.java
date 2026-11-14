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
public class AppointmentsItemsDto {
    private Integer id;
    private Integer appointment_id;
    private Integer service_id;
    private Integer professional_id;
    private Integer resource_id;
    private OffsetDateTime start_at;
    private OffsetDateTime end_at;
    private BigDecimal price;
}
