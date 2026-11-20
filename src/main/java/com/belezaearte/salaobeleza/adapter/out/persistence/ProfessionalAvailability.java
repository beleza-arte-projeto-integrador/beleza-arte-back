package com.belezaearte.salaobeleza.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

// Horario de trabalho de um profissional (tabela professional_availability).
// Cada linha e uma faixa de horario em um dia da semana. Para ter intervalo de almoco,
// o profissional cadastra duas faixas no mesmo dia (ex: 09:00-12:00 e 13:00-18:00).
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "professional_availability")
public class ProfessionalAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    // FK -> professionals.id
    @Column(name = "professional_id", nullable = false)
    private Integer professionalId;

    // 0 = domingo, 1 = segunda ... 6 = sabado (SMALLINT no banco).
    @Column(name = "weekday", nullable = false)
    private Short weekday;

    // Inicio e fim do expediente nessa faixa (TIME no banco).
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
}
