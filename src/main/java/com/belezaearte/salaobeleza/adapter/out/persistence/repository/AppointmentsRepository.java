package com.belezaearte.salaobeleza.adapter.out.persistence.repository;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public interface AppointmentsRepository extends JpaRepository<Appointments, Integer> {

    // Campos da própria tabela appointments.
    List<Appointments> findByClientIdOrderByIdAsc(Integer clientId);

    List<Appointments> findByStatusOrderByIdAsc(String status);

    List<Appointments> findByNotesContainingIgnoreCaseOrderByIdAsc(String notes);

    List<Appointments> findByCreatedOrderByIdAsc(OffsetDateTime created);

    // Profissional, serviço, preço e horário ficam nos itens (appointment_items):
    // busca os agendamentos que têm pelo menos um item com o valor procurado.
    @Query("select a from Appointments a where exists (select i.id from AppointmentsItems i "
            + "where i.appointment_id = a.id and i.professional_id = :professionalId) order by a.id")
    List<Appointments> findByProfessional(@Param("professionalId") Integer professionalId);

    @Query("select a from Appointments a where exists (select i.id from AppointmentsItems i "
            + "where i.appointment_id = a.id and i.service_id = :serviceId) order by a.id")
    List<Appointments> findByService(@Param("serviceId") Integer serviceId);

    @Query("select a from Appointments a where exists (select i.id from AppointmentsItems i "
            + "where i.appointment_id = a.id and i.price = :price) order by a.id")
    List<Appointments> findByItemPrice(@Param("price") BigDecimal price);

    @Query("select a from Appointments a where exists (select i.id from AppointmentsItems i "
            + "where i.appointment_id = a.id and i.start_at = :startAt) order by a.id")
    List<Appointments> findByItemStart(@Param("startAt") OffsetDateTime startAt);

    // Agendamentos com algum item começando no intervalo [start, end).
    @Query("select a from Appointments a where exists (select i.id from AppointmentsItems i "
            + "where i.appointment_id = a.id and i.start_at >= :start and i.start_at < :end) order by a.id")
    List<Appointments> findByItemStartBetween(@Param("start") OffsetDateTime start, @Param("end") OffsetDateTime end);
}
