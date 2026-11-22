package com.belezaearte.salaobeleza.adapter.out.persistence;


import com.belezaearte.salaobeleza.adapter.out.persistence.repository.AppointmentsRepository;
import com.belezaearte.salaobeleza.application.port.in.Appointments.SearchIdClientAppointmentsUseCase;
import com.belezaearte.salaobeleza.application.port.out.appointmentsPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AppointmentsAdapter implements SearchUpdatedAppointmentsPort,SearchIdClientAppointmentsPort,SearchCreatedAppointmentsPort,SearchCancelledByAppointmentsPort,SearchCancelledAppointmentsPort,CreateAppointmentsPort,UpdateAppointmentsPort,DeleteAppointmentsPort {
    private final AppointmentsRepository appointmentsRepository;

    @Override
    @Transactional
    public List<Appointments> searchUpdated (OffsetDateTime updated){
        Appointments appointments = Appointments.builder()
                .updated(updated).build();

        Example<Appointments> example = Example.of(appointments);
        return appointmentsRepository.findAll(example);
    }
    @Override
    public List<Appointments> searchIdClient (Clients idClient){
        Appointments appointments = Appointments.builder()
                .idClients(idClient).build();

        Example<Appointments> example = Example.of(appointments);
        return appointmentsRepository.findAll(example);
    }
    @Override
    public  List<Appointments> searchCreated (OffsetDateTime created){
        Appointments appointments = Appointments.builder()
                .created(created).build();

        Example<Appointments> example = Example.of(appointments);
        return appointmentsRepository.findAll(example);
    }
    @Override
    public List<Appointments> searchCancelledBy (String cancelledBy){
        Appointments appointments = Appointments.builder()
                .cancelledBy(cancelledBy).build();

        Example<Appointments> example = Example.of(appointments);
        return appointmentsRepository.findAll(example);
    }
    @Override
    public List<Appointments> searchCancelled (OffsetDateTime cancelled){
        Appointments appointments = Appointments.builder()
                .cancelled(cancelled).build();

        Example<Appointments> example = Example.of(appointments);
        return appointmentsRepository.findAll(example);
    }
    @Override
    public Appointments create (Appointments appointments){
        return appointmentsRepository.saveAndFlush(appointments);
    }
    @Override
    public Appointments update (Appointments appointments){
        Optional<Appointments> appointmentsOptional = appointmentsRepository.findById(appointments.getId());

        if(appointmentsOptional.isPresent()){
            Appointments appointments1 = appointmentsOptional.get();

            appointments.setUpdated(appointments.getUpdated());
            appointments.setCancelled(appointments.getCancelled());
            appointments.setCancelledBy(appointments.getCancelledBy());
            //   appointments.setId(appointments.getId());
            appointments.setCreated(appointments.getCreated());
            appointments.setIdClients(appointments.getIdClients());

            return appointmentsRepository.saveAndFlush(appointments1);
        }
        return null;
    }
    @Override
    public void deleteAppointments (Integer id){
        if(appointmentsRepository.existsById(id)){
            appointmentsRepository.existsById(id);
        } else{
            throw new BusinessOutboundException("Erro: Verifique se existe o"+ id);
        }
    }

}