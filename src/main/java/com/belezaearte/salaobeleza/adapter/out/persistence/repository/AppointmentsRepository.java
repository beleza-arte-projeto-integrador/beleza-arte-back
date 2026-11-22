package com.belezaearte.salaobeleza.adapter.out.persistence.repository;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentsRepository extends JpaRepository<Appointments, Integer> {
    List<Appointments> findByCancelledByIsNotNull();
}
