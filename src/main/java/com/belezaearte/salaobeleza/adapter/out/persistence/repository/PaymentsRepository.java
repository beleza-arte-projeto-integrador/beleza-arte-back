package com.belezaearte.salaobeleza.adapter.out.persistence.repository;

import com.belezaearte.salaobeleza.adapter.out.persistence.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentsRepository extends JpaRepository<Payments, Integer> {

    List<Payments> findByAppointmentIdOrderByIdAsc(Integer appointmentId);

    List<Payments> findByStatusOrderByIdAsc(String status);

    @Query("select coalesce(sum(i.price), 0) from AppointmentsItems i where i.appointment_id = :appointmentId")
    BigDecimal sumItemsPrice(@Param("appointmentId") Integer appointmentId);

    @Query("select a.status from Appointments a where a.id = :appointmentId")
    Optional<String> findAppointmentStatus(@Param("appointmentId") Integer appointmentId);

    @Query(nativeQuery = true, value = """
            select p.id as id,
                   p.appointment_id as idAppointment,
                   c.name as clientName,
                   (select string_agg(s.name, ' + ' order by i.start_at)
                      from appointment_items i join services s on s.id = i.service_id
                     where i.appointment_id = p.appointment_id) as services,
                   (select min(i.start_at) from appointment_items i
                     where i.appointment_id = p.appointment_id) as appointmentDate,
                   p.amount as amount,
                   p.method as method,
                   p.status as status,
                   p.paid_at as paidAt,
                   p.created_at as created
              from payments p
              join appointments a on a.id = p.appointment_id
              join clients c on c.id = a.client_id
             where cast(:status as varchar) is null or p.status = cast(:status as varchar)
             order by coalesce(p.paid_at, p.created_at) desc, p.id desc
            """)
    List<PaymentsListView> listPayments(@Param("status") String status);

    @Query("select coalesce(sum(p.amount), 0) from Payments p where p.status = 'PAGO' and p.paidAt >= :start and p.paidAt < :end")
    BigDecimal sumPaidBetween(@Param("start") OffsetDateTime start, @Param("end") OffsetDateTime end);

    @Query("select coalesce(sum(p.amount), 0) from Payments p where p.status = :status")
    BigDecimal sumStatus(@Param("status") String status);

    @Query("select count(distinct p.appointmentId) from Payments p where p.status = :status")
    long countAppointmentsWithStatus(@Param("status") String status);

    @Query("select coalesce(sum(p.amount), 0) from Payments p where p.status = :status and p.created >= :start and p.created < :end")
    BigDecimal sumStatusCreatedBetween(@Param("status") String status, @Param("start") OffsetDateTime start, @Param("end") OffsetDateTime end);
}
