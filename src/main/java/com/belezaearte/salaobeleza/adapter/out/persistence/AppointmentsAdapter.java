package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.AppointmentsRepository;
import com.belezaearte.salaobeleza.application.port.out.appointmentsPort.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AppointmentsAdapter implements CreateAppointmentsPort, DeleteAppointmentsPort,SearchCreatedAppointmentsPort, SearchCancelledAppointmentsPort, SearchCancelledByAppointmentsPort, SearchIdClientAppointmentsPort, SearchUpdatedAppointmentsPort, UpdateAppointmentsPort {

    private final AppointmentsRepository appointmentsRepository;

    @Override
    public Optional<Appointments> create(Appointments appointments) {
        return Optional.of(appointmentsRepository.saveAndFlush(appointments));
    }

    @Override
    public void delete(Integer id) {
        appointmentsRepository.deleteById(id);
    }

    @Override
    public List<Appointments> searchCancelled(OffsetDateTime cancelled) {
        Appointments appointments = Appointments.builder().cancelled(cancelled).build();

        Example<Appointments> example = Example.of(appointments);
        return appointmentsRepository.findAll(example);
    }

    @Override
    public List<Appointments> searchCancelledBy(String cancelledBy) {
        Appointments appointments = Appointments.builder().cancelledBy(cancelledBy).build();
        Example<Appointments> example = Example.of(appointments);
        return appointmentsRepository.findAll(example);
    }

    @Override
    public List<Appointments> searchCreatedAppointments(OffsetDateTime created) {
        Appointments appointments = Appointments.builder().created(created).build();

        Example<Appointments> example = Example.of(appointments);
        return appointmentsRepository.findAll(example);
    }

    @Override
    public List<Appointments> searchIdClient(Clients idClient) {
        Appointments appointments = Appointments.builder().idClient(idClient.getId()).build();
        Example<Appointments> example = Example.of(appointments);
        return appointmentsRepository.findAll(example);
    }

    @Override
    public List<Appointments> searchUpdated(OffsetDateTime updated) {
        Appointments appointments = Appointments.builder().updated(updated).build();

        Example<Appointments> example = Example.of(appointments);
        return appointmentsRepository.findAll(example);
    }

    @Override
    public Optional<Appointments> update(Appointments appointments) {
        Optional<Appointments> appointmentsOptional = appointmentsRepository.findById(appointments.getId());
        if (appointmentsOptional.isPresent()){
            return Optional.of(appointmentsRepository.saveAndFlush(appointments));
        }
        else {
            return Optional.empty();
        }

    }
}
