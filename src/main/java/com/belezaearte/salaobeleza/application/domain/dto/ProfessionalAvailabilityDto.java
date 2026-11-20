package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

// JSON de entrada e saida da disponibilidade. Exemplo:
// { "idProfessional": 1, "weekday": 1, "startTime": "09:00", "endTime": "12:00" }
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalAvailabilityDto {
    private Integer id;
    private Integer idProfessional;
    // 0 = domingo, 1 = segunda ... 6 = sabado
    private Integer weekday;
    private LocalTime startTime;
    private LocalTime endTime;
}
