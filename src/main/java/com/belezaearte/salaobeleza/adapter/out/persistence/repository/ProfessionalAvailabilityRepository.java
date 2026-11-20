package com.belezaearte.salaobeleza.adapter.out.persistence.repository;

import com.belezaearte.salaobeleza.adapter.out.persistence.ProfessionalAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessionalAvailabilityRepository extends JpaRepository<ProfessionalAvailability, Integer> {

    // Faixas de um profissional, em ordem de dia da semana e horario.
    List<ProfessionalAvailability> findByProfessionalIdOrderByWeekdayAscStartTimeAsc(Integer professionalId);

    // Faixas de um profissional em um dia da semana (usado para checar sobreposicao e horarios livres).
    List<ProfessionalAvailability> findByProfessionalIdAndWeekdayOrderByStartTimeAsc(Integer professionalId, Short weekday);
}
