package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;
import com.belezaearte.salaobeleza.adapter.out.persistence.Clients;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ClientsRepository;
import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsDto;
import com.belezaearte.salaobeleza.application.port.in.Appointments.*;
import com.belezaearte.salaobeleza.application.port.out.appointmentsPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AppointmentsService implements SearchUpdatedAppointmentsUseCase,SearchIdClientAppointmentsUseCase,SearchCreatedAppointmentsUseCase, SearchCancelledByAppointmentsUseCase, SearchCancelledAppointmentsUseCase,DeleteAppointmentsUseCase,UpdateAppointmentsUseCase,CreateAppointmentsUseCase {
    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    SearchUpdatedAppointmentsPort searchUpdatedAppointmentsPort;

    @Override
    public List<AppointmentsDto> searchUpdated (OffsetDateTime updated){
        List<Appointments> appointmentsList = searchUpdatedAppointmentsPort.searchUpdated(updated);

        return appointmentsList.stream()
                .map(appointments -> AppointmentsDto.builder()
                        .id(appointments.getId())
                        .idClients(appointments.getIdClients().getId())
                        .cancelledBy(appointments.getCancelledBy())
                        .cancelled(appointments.getCancelled())
                        .created(appointments.getCreated())
                        .updated(appointments.getUpdated()).build()).toList();
    }
    @Autowired
    SearchIdClientAppointmentsPort searchIdClientAppointmentsPort;
    @Override
    public List<AppointmentsDto> searchIdClient (Clients idClient) {
        List<Appointments> appointmentsList = searchIdClientAppointmentsPort.searchIdClient(idClient);

        return appointmentsList.stream()
                .map(appointments -> AppointmentsDto.builder()
                        .id(appointments.getId())
                        .idClients(appointments.getIdClients().getId())
                        .cancelledBy(appointments.getCancelledBy())
                        .cancelled(appointments.getCancelled())
                        .created(appointments.getCreated())
                        .updated(appointments.getUpdated()).build()).toList();
    }
    @Autowired
    private SearchCreatedAppointmentsPort searchCreatedAppointmentsPort;
    @Override
    public List<AppointmentsDto> searchCreated (OffsetDateTime created){
        List<Appointments> appointmentsList =searchCreatedAppointmentsPort.searchCreated(created);

        return appointmentsList.stream()
                .map(appointments -> AppointmentsDto.builder()
                        .id(appointments.getId())
                        .idClients(appointments.getIdClients().getId())
                        .cancelledBy(appointments.getCancelledBy())
                        .cancelled(appointments.getCancelled())
                        .created(appointments.getCreated())
                        .updated(appointments.getUpdated()).build()).toList();
    }

    @Autowired
    private SearchCancelledByAppointmentsPort searchCancelledByAppointmentsPort ;
    @Override
    public List<AppointmentsDto> searchCancelledBy (String cancelledBy){
        List<Appointments> appointmentsList = searchCancelledByAppointmentsPort.searchCancelledBy(cancelledBy);

        return appointmentsList.stream()
                .map(appointments -> AppointmentsDto.builder()
                        .id(appointments.getId())
                        .idClients(appointments.getIdClients().getId())
                        .cancelledBy(appointments.getCancelledBy())
                        .cancelled(appointments.getCancelled())
                        .created(appointments.getCreated())
                        .updated(appointments.getUpdated()).build()).toList();
    }
    @Autowired
    private SearchCancelledAppointmentsPort searchCancelledAppointmentsPort ;
    @Override
    public List<AppointmentsDto> searchCancelled (OffsetDateTime cancelled){
        List<Appointments> appointmentsList = searchCancelledAppointmentsPort.searchCancelled(cancelled);

        return appointmentsList.stream()
                .map(appointments -> AppointmentsDto.builder()
                        .id(appointments.getId())
                        .idClients(appointments.getIdClients().getId())
                        .cancelledBy(appointments.getCancelledBy())
                        .cancelled(appointments.getCancelled())
                        .created(appointments.getCreated())
                        .updated(appointments.getUpdated()).build()).toList();
    }

    @Autowired
    private DeleteAppointmentsPort deleteAppointmentsPort;

    @Override
    public void deleteAppointments(Integer id) {
        deleteAppointmentsPort.deleteAppointments(id);
    }
    @Autowired
    private UpdateAppointmentsPort updateAppointmentsPort;

    @Override
    public AppointmentsDto update(AppointmentsDto appointmentsDto, Integer idClient) {

        Clients clients = clientsRepository.findById(idClient)
                .orElseThrow(() -> new BusinessOutboundException("Cliente não encontrado"));

        Appointments appointmentsEntity = Appointments.builder()
                .id(appointmentsDto.getId())
                .idClients(clients)
                .cancelledBy(appointmentsDto.getCancelledBy())
                .cancelled(appointmentsDto.getCancelled())
                .created(appointmentsDto.getCreated())
                .updated(appointmentsDto.getUpdated())
                .build();

        Appointments appointmentsUpdate = updateAppointmentsPort.update(appointmentsEntity);

        if (appointmentsUpdate == null) {
            throw new BusinessOutboundException("Erro: O Id detectado pode não estar correto, por favor verifique");
        }

        return AppointmentsDto.builder()
                .id(appointmentsUpdate.getId())
                .idClients(appointmentsUpdate.getIdClients().getId())
                .cancelledBy(appointmentsUpdate.getCancelledBy())
                .cancelled(appointmentsUpdate.getCancelled())
                .created(appointmentsUpdate.getCreated())
                .updated(appointmentsUpdate.getUpdated())
                .build();
    }
    @Autowired
    public CreateAppointmentsPort createAppointmentsPort;

    @Override
    public AppointmentsDto create (AppointmentsDto appointmentsDto, Integer idClient){

        if(appointmentsDto.getCancelledBy() == null || appointmentsDto.getIdClients() == null || appointmentsDto.getUpdated() == null || appointmentsDto.getCancelled() == null || appointmentsDto.getCreated() ==null ){
            throw new BusinessOutboundException("Erro: Campo não preenchido, por favor verifique se preencheu os dados corretamente");
        }
        Clients clients = clientsRepository.findById(idClient)
                .orElseThrow(() -> new BusinessOutboundException("Cliente não encontrado"));


        Appointments appointmentsEntity = Appointments.builder()
                .id(appointmentsDto.getId())
                .idClients(clients)
                .cancelledBy(appointmentsDto.getCancelledBy())
                .cancelled(appointmentsDto.getCancelled())
                .created(appointmentsDto.getCreated())
                .updated(appointmentsDto.getUpdated())
                .build();

        Appointments appointmentsSaved = createAppointmentsPort.create(appointmentsEntity);

        return AppointmentsDto.builder()
                .id(appointmentsSaved.getId())
                .idClients(appointmentsSaved.getIdClients().getId())
                .cancelledBy(appointmentsSaved.getCancelledBy())
                .cancelled(appointmentsSaved.getCancelled())
                .created(appointmentsSaved.getCreated())
                .updated(appointmentsSaved.getUpdated())
                .build();
    }
}
