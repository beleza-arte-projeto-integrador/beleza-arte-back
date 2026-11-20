package com.belezaearte.salaobeleza.application.port.in.ProfessionalAvailability;

import com.belezaearte.salaobeleza.application.domain.dto.AvailableTimeDto;

import java.time.LocalDate;
import java.util.List;

public interface SearchAvailableTimesUseCase {
    // Horarios livres do profissional no dia: dentro do expediente e sem conflito com outro agendamento.
    List<AvailableTimeDto> searchAvailableTimes (Integer idProfessional, LocalDate date, Integer durationMinutes, Integer intervalMinutes);
}
