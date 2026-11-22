package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentsDto {
    private Integer id;
    private Integer idClients;
    private String cancelledBy;
    private OffsetDateTime cancelled;
    private OffsetDateTime created;
    private OffsetDateTime updated;
}
