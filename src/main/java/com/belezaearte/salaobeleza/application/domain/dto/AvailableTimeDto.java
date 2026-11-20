package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.OffsetDateTime;

// Um horario livre na agenda do profissional.
// startAt ja vem no horario de Brasilia, pronto para criar o item do agendamento.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTimeDto {
    private LocalTime startTime;
    private LocalTime endTime;
    private OffsetDateTime startAt;
    private OffsetDateTime endAt;
}
