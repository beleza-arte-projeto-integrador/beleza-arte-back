package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicesDto {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration_minutes;
    private OffsetDateTime created;
    private String category;

}
