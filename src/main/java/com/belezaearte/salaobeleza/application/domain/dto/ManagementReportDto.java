package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// Relatorio gerencial de um periodo.
// "Agendamento" = visita do cliente (appointments); "atendimento" = cada servico da visita (appointment_items).
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagementReportDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer idProfessional;

    // Agendamentos (visitas) no periodo, por situacao.
    private Integer totalAppointments;
    private Integer completedAppointments;
    private Integer cancelledAppointments;
    private Integer noShowAppointments;
    // Percentuais sobre o total de agendamentos (ex: 12.5 = 12,5%).
    private BigDecimal cancellationRate;
    private BigDecimal noShowRate;

    // Atendimentos (servicos) validos: de agendamentos que nao foram cancelados nem tiveram falta.
    private Integer totalServices;
    // Soma do preco dos atendimentos validos.
    private BigDecimal expectedRevenue;
    // Soma do preco dos atendimentos de agendamentos CONCLUIDOS.
    private BigDecimal completedRevenue;
    // Faturamento previsto / agendamentos validos.
    private BigDecimal averageTicket;
    private Integer uniqueClients;

    // Ocupacao: minutos agendados / minutos de expediente (professional_availability) no periodo.
    // Fica null quando nenhum profissional do filtro tem horario de trabalho cadastrado.
    private Integer availableMinutes;
    private Integer bookedMinutes;
    private BigDecimal occupancyRate;

    private List<ReportServiceDto> services;
    private List<ReportProfessionalDto> professionals;
    // Atendimentos por dia da semana: posicao 0 = domingo ... 6 = sabado.
    private List<Integer> servicesByWeekday;
    // Atendimentos por horario de inicio (ex: "09:00" -> 4), em ordem de horario.
    private Map<String, Integer> servicesByHour;
}
