package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Desempenho de um profissional no relatorio gerencial.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportProfessionalDto {
    private Integer idProfessional;
    private String name;
    // Atendimentos validos (agendamento nao cancelado e sem falta).
    private Integer services;
    private BigDecimal revenue;
    // Atendimentos de agendamentos cancelados ou com falta.
    private Integer cancelledServices;
    private Integer noShowServices;
}
