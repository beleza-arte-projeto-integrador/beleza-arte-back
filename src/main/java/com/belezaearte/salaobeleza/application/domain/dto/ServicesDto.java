package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ServicesDto {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration_minutes;
    private LocalDateTime created;

}
