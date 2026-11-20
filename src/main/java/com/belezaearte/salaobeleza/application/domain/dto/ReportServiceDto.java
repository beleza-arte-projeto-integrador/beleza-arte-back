package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Linha do ranking de servicos do relatorio gerencial.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportServiceDto {
    private Integer idService;
    private String name;
    private Integer quantity;
    private BigDecimal revenue;
}
