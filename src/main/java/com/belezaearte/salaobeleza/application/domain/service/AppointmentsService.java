package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;
import com.belezaearte.salaobeleza.adapter.out.persistence.Clients;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.AppointmentsRepository;
import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsDto;
import com.belezaearte.salaobeleza.application.port.in.Appointments.*;
import com.belezaearte.salaobeleza.application.port.out.appointmentsPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentsService implements CreateAppointmentsUseCase, UpdateAppointmentsUseCase, DeleteAppointmentsUseCase, SearchIdClientAppointmentsUseCase, SearchCancelledByAppointmentsUseCase, SearchCancelledAppointmentsUseCase, SearchUpdatedAppointmentsUseCase, SearchCreatedAppointmentsUseCase {

    @Autowired
    private CreateAppointmentsPort createAppointmentsPort;
    @Autowired
    private DeleteAppointmentsPort deleteAppointmentsPort;
    @Autowired
    private SearchIdClientAppointmentsPort searchClientAppointmentsPort;
    @Autowired
    private SearchCreatedAppointmentsPort searchCreatedAppointmentsPort;
    @Autowired
    private SearchCancelledAppointmentsPort searchCancelledAppointmentsPort;
    @Autowired
    private SearchUpdatedAppointmentsPort searchUpdatedAppointmentsPort;
    @Autowired
    private UpdateAppointmentsPort updateAppointmentsPort;
    @Autowired
    private SearchCancelledByAppointmentsPort searchCancelledByAppointmentsPort;
    @Autowired
    private AppointmentsRepository appointmentsRepository;


    @Override
    public AppointmentsDto createAppointments(AppointmentsDto appointmentsDto, Integer idClient) {
        Clients clients = Clients.builder().id(idClient).build();

        Appointments appointmentsEntity = Appointments.builder()
                .idClient(clients.getId())
                .cancelledBy(appointmentsDto.getCancelledBy())
                .cancelled(appointmentsDto.getCancelled())
                .created(appointmentsDto.getCreated())
                .updated(appointmentsDto.getUpdated())
                .build();

        try {
            Optional<Appointments> appointmentsSaved = createAppointmentsPort.create(appointmentsEntity);

            return AppointmentsDto.builder()
                    .id(appointmentsSaved.get().getId())
                    .idClient(appointmentsSaved.get().getId())
                    .cancelledBy(appointmentsSaved.get().getCancelledBy())
                    .cancelled(appointmentsSaved.get().getCancelled())
                    .created(appointmentsSaved.get().getCreated())
                    .updated(appointmentsSaved.get().getUpdated())
                    .build();
        }
        catch (Exception e){
            throw new BusinessOutboundException("Could not create the Appointment. Please check whether it has already been registered.");
        }
    }

    @Override
    public void deleteAppointments(Integer id) {
        Optional<Appointments> appointmentsOptional = appointmentsRepository.findById(id);
        if (appointmentsOptional.isPresent()){
            deleteAppointmentsPort.delete(id);
        }
        else {
            throw new BusinessOutboundException("Could not delete the Appointment. Please check whether the Appointment exists.");
        }
    }

    @Override
    public List<AppointmentsDto> searchIdClient(Clients idClient) {
        List<Appointments> appointments = searchClientAppointmentsPort.searchIdClient(idClient);
        if (!appointments.isEmpty()){
            return appointments.stream().map(appointment -> AppointmentsDto.builder()
                            .id(appointment.getId())
                            .idClient(appointment.getId())
                            .cancelledBy(appointment.getCancelledBy())
                            .cancelled(appointment.getCancelled())
                            .created(appointment.getCreated())
                            .updated(appointment.getUpdated())
                            .build())
                    .toList();
        }
        else {
            throw new BusinessOutboundException("Could not search for the client's ID. Please check whether the ID exists.");
        }
    }

    @Override
    public List<AppointmentsDto> searchCancelledBy(String cancelledBy) {
        List<Appointments> appointments = searchCancelledByAppointmentsPort.searchCancelledBy(cancelledBy);
        if(!appointments.isEmpty()){
            return appointments.stream().map(appointment -> AppointmentsDto.builder()
                            .id(appointment.getId())
                            .idClient(appointment.getId())
                            .cancelledBy(appointment.getCancelledBy())
                            .cancelled(appointment.getCancelled())
                            .created(appointment.getCreated())
                            .updated(appointment.getUpdated())
                            .build())
                    .toList();
        }
        else {
            throw new BusinessOutboundException("Could not find the Appointment! The requested Appointment was not found in the system.");
        }
    }
    @Override
    public AppointmentsDto updateAppointments(AppointmentsDto appointmentsDto, Integer idClient) {
        Clients clients = Clients.builder().id(idClient).build();

        Appointments appointmentsEntity = Appointments.builder()
                .id(appointmentsDto.getId())
                .idClient(clients.getId())
                .cancelledBy(appointmentsDto.getCancelledBy())
                .cancelled(appointmentsDto.getCancelled())
                .created(appointmentsDto.getCreated())
                .updated(appointmentsDto.getUpdated())
                .build();

        Optional<Appointments> updateAppointments = updateAppointmentsPort.update(appointmentsEntity);

        if (updateAppointments == null){
            throw  new BusinessOutboundException("Could not update the Appointment. Please check whether the Appointment exists.");
        }
        return AppointmentsDto.builder()
                .id(updateAppointments.get().getId())
                .idClient(updateAppointments.get().getId())
                .cancelledBy(updateAppointments.get().getCancelledBy())
                .cancelled(updateAppointments.get().getCancelled())
                .created(updateAppointments.get().getCreated())
                .updated(updateAppointments.get().getUpdated())
                .build();

    }

    @Override
    public List<AppointmentsDto> searchCancelledAppointments(OffsetDateTime cancelled) {
        List<Appointments> appointmentsList = searchCancelledAppointmentsPort.searchCancelled(cancelled);

        return appointmentsList.stream().map(appointments -> AppointmentsDto.builder()
                        .id(appointments.getId())
                        .idClient(appointments.getId())
                        .cancelledBy(appointments.getCancelledBy())
                        .cancelled(appointments.getCancelled())
                        .created(appointments.getCreated())
                        .updated(appointments.getUpdated())
                        .build())
                .toList();
    }

    @Override
    public List<AppointmentsDto> searchCreatedAppointments(OffsetDateTime created) {
        List<Appointments> appointmentsList = searchCreatedAppointmentsPort.searchCreatedAppointments(created);

        return appointmentsList.stream().map(appointments -> AppointmentsDto.builder()
                        .id(appointments.getId())
                        .idClient(appointments.getId())
                        .cancelledBy(appointments.getCancelledBy())
                        .cancelled(appointments.getCancelled())
                        .created(appointments.getCreated())
                        .updated(appointments.getUpdated())
                        .build())
                .toList();
    }

    @Override
    public List<AppointmentsDto> searchUpdatedAppointments(OffsetDateTime updated) {
        List<Appointments> appointmentsList = searchUpdatedAppointmentsPort.searchUpdated(updated);

        return appointmentsList.stream().map(appointments -> AppointmentsDto.builder()
                        .id(appointments.getId())
                        .idClient(appointments.getId())
                        .cancelledBy(appointments.getCancelledBy())
                        .cancelled(appointments.getCancelled())
                        .created(appointments.getCreated())
                        .updated(appointments.getUpdated())
                        .build())
                .toList();
    }
}
