package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AppointmentsDto {
    private Integer id;
    private Integer idClient;
    private Integer idProfessional;
    private Integer idService;
    private BigDecimal price;
    private String status;
    private String notes;
}
