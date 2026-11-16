package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;
import com.belezaearte.salaobeleza.adapter.out.persistence.AppointmentsItems;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.AppointmentsRepository;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ClientsRepository;
import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsDto;
import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsItemsDto;
import com.belezaearte.salaobeleza.application.port.in.Appointments.*;
import com.belezaearte.salaobeleza.application.port.out.appointmentsPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class AppointmentsService implements CreateAppointmentsUseCase, UpdateAppointmentsUseCase, DeleteAppointmentsUseCase, SearchClientAppointmentsUseCase, SearchProfessionalAppointmentsUseCase, SearchServiceAppointmentsUseCase, SearchStatusAppointmentsUseCase, SearchNotesAppointmentsUseCase, SearchPriceAppointmentsUseCase, SearchDateAppointmentsUseCase, SearchTimeAppointmentsUseCase, SearchCreatedAppointmentsUseCase {

    private static final String NOT_FOUND = "Could not find appointment!, The requested appointment was not found in the system!";

    // Status permitidos no banco (docs/banco-de-dados.md, tabela appointments).
    static final String AGENDADO = "AGENDADO";
    static final String CONFIRMADO = "CONFIRMADO";
    static final String CONCLUIDO = "CONCLUIDO";
    static final String CANCELADO = "CANCELADO";
    static final String NAO_COMPARECEU = "NAO_COMPARECEU";

    // Ciclo de vida do status, igual ao do dicionário de dados:
    // AGENDADO -> CONFIRMADO -> CONCLUIDO; AGENDADO ou CONFIRMADO -> CANCELADO; CONFIRMADO -> NAO_COMPARECEU.
    // CONCLUIDO, CANCELADO e NAO_COMPARECEU são finais.
    private static final Map<String, Set<String>> NEXT_STATUS = Map.of(
            AGENDADO, Set.of(CONFIRMADO, CANCELADO),
            CONFIRMADO, Set.of(CONCLUIDO, CANCELADO, NAO_COMPARECEU),
            CONCLUIDO, Set.of(),
            CANCELADO, Set.of(),
            NAO_COMPARECEU, Set.of());

    // Quem pode cancelar (coluna cancelled_by).
    private static final Set<String> CANCELLED_BY = Set.of("CLIENTE", "SALAO");

    // O salão fica no Brasil: "um dia" da agenda vai de 00:00 a 23:59 no horário de Brasília.
    private static final ZoneId SALON_ZONE = ZoneId.of("America/Sao_Paulo");

    @Autowired
    private CreateAppointmentsPort createAppointmentsPort;
    @Autowired
    private UpdateAppointmentsPort updateAppointmentsPort;
    @Autowired
    private DeleteAppointmentsPort deleteAppointmentsPort;
    @Autowired
    private SearchClientAppointmentsPort searchClientAppointmentsPort;
    @Autowired
    private SearchProfessionalAppointmentsPort searchProfessionalAppointmentsPort;
    @Autowired
    private SearchServiceAppointmentsPort searchServiceAppointmentsPort;
    @Autowired
    private SearchStatusAppointmentsPort searchStatusAppointmentsPort;
    @Autowired
    private SearchNotesAppointmentsPort searchNotesAppointmentsPort;
    @Autowired
    private SearchPriceAppointmentsPort searchPriceAppointmentsPort;
    @Autowired
    private SearchDateAppointmentsPort searchDateAppointmentsPort;
    @Autowired
    private SearchTimeAppointmentsPort searchTimeAppointmentsPort;
    @Autowired
    private SearchCreatedAppointmentsPort searchCreatedAppointmentsPort;
    @Autowired
    private AppointmentsRepository appointmentsRepository;
    @Autowired
    private ClientsRepository clientsRepository;

    // Cria a visita. Todo agendamento novo começa como AGENDADO.
    // Os serviços da visita são cadastrados depois, em /appointmentsItems, com o id deste agendamento.
    @Override
    public AppointmentsDto createAppointments(AppointmentsDto appointmentsDto) {
        Integer idClient = appointmentsDto.getIdClient();
        if (idClient == null) {
            throw new BusinessOutboundException("Invalid appointment, The client is required");
        }
        if (!clientsRepository.existsById(idClient)) {
            throw new BusinessOutboundException("Could not create appointment, Check if the client exists");
        }

        Appointments appointmentsEntity = Appointments.builder()
                .clientId(idClient)
                .status(AGENDADO)
                .notes(blankToNull(appointmentsDto.getNotes()))
                .build();

        try {
            Optional<Appointments> appointmentsSaved = createAppointmentsPort.create(appointmentsEntity);
            return toDto(appointmentsSaved.get());
        } catch (Exception e) {
            throw new BusinessOutboundException("Could not create appointment, Check the appointment data");
        }
    }

    // Atualiza as observações e o status. O cliente e a data de criação não mudam.
    @Override
    public AppointmentsDto updateAppointments(AppointmentsDto appointmentsDto) {
        if (appointmentsDto.getId() == null) {
            throw new BusinessOutboundException("Could not update appointment, The appointment id is required");
        }

        Optional<Appointments> existing = appointmentsRepository.findById(appointmentsDto.getId());
        if (existing.isEmpty()) {
            throw new BusinessOutboundException("Could not update appointment, Check if the appointment exists");
        }
        Appointments appointmentsEntity = existing.get();

        if (appointmentsDto.getIdClient() != null && !appointmentsDto.getIdClient().equals(appointmentsEntity.getClientId())) {
            throw new BusinessOutboundException("Could not update appointment, The client of an appointment cannot be changed");
        }

        // Observações não informadas (null) = mantém as atuais; texto vazio apaga.
        if (appointmentsDto.getNotes() != null) {
            appointmentsEntity.setNotes(blankToNull(appointmentsDto.getNotes()));
        }

        // Status não informado = mantém o atual.
        String newStatus = normalize(appointmentsDto.getStatus());
        if (newStatus != null && !newStatus.equals(appointmentsEntity.getStatus())) {
            changeStatus(appointmentsEntity, newStatus, appointmentsDto);
        }

        Optional<Appointments> appointmentUpdated = updateAppointmentsPort.update(appointmentsEntity);
        if (appointmentUpdated.isEmpty()) {
            throw new BusinessOutboundException("Could not update appointment, Check if the appointment exists");
        }
        return toDto(appointmentUpdated.get());
    }

    // Os itens do agendamento são apagados junto, pelo próprio banco (ON DELETE CASCADE).
    // Para guardar o histórico, prefira cancelar (status CANCELADO) em vez de apagar.
    @Override
    public void deleteAppointments(Integer id) {
        if (id == null || !appointmentsRepository.existsById(id)) {
            throw new BusinessOutboundException("Could not delete appointment, Check if the appointment exists");
        }
        try {
            deleteAppointmentsPort.delete(id);
        } catch (Exception e) {
            // Acontece quando o agendamento já tem pagamentos ligados a ele.
            throw new BusinessOutboundException("Could not delete appointment, Check if the appointment has payments");
        }
    }

    @Override
    public List<AppointmentsDto> searchClient(Integer idClient) {
        return toDtoListOrNotFound(searchClientAppointmentsPort.searchClient(idClient));
    }

    @Override
    public List<AppointmentsDto> searchProfessional(Integer idProfessional) {
        return toDtoListOrNotFound(searchProfessionalAppointmentsPort.searchProfessional(idProfessional));
    }

    @Override
    public List<AppointmentsDto> searchService(Integer idService) {
        return toDtoListOrNotFound(searchServiceAppointmentsPort.searchService(idService));
    }

    @Override
    public List<AppointmentsDto> searchStatus(String status) {
        String normalized = normalize(status);
        if (normalized == null || !NEXT_STATUS.containsKey(normalized)) {
            throw new BusinessOutboundException("Invalid status, Use AGENDADO, CONFIRMADO, CONCLUIDO, CANCELADO or NAO_COMPARECEU");
        }
        return toDtoListOrNotFound(searchStatusAppointmentsPort.searchStatus(normalized));
    }

    @Override
    public List<AppointmentsDto> searchNotes(String notes) {
        return toDtoListOrNotFound(searchNotesAppointmentsPort.searchNotes(notes));
    }

    @Override
    public List<AppointmentsDto> searchPrice(BigDecimal price) {
        return toDtoListOrNotFound(searchPriceAppointmentsPort.searchPrice(price));
    }

    // A agenda de um dia: agendamentos com algum serviço começando nessa data.
    @Override
    public List<AppointmentsDto> searchDate(LocalDate date) {
        OffsetDateTime start = date.atStartOfDay(SALON_ZONE).toOffsetDateTime();
        OffsetDateTime end = date.plusDays(1).atStartOfDay(SALON_ZONE).toOffsetDateTime();
        return toDtoListOrNotFound(searchDateAppointmentsPort.searchDate(start, end));
    }

    // Agendamentos com algum serviço começando exatamente nesse dia e horário.
    @Override
    public List<AppointmentsDto> searchTime(OffsetDateTime startAt) {
        return toDtoListOrNotFound(searchTimeAppointmentsPort.searchTime(startAt));
    }

    @Override
    public List<AppointmentsDto> searchCreated(OffsetDateTime created) {
        return toDtoListOrNotFound(searchCreatedAppointmentsPort.searchCreated(created));
    }

    // ---------------------------------------------------------------
    // Métodos auxiliares
    // ---------------------------------------------------------------

    private void changeStatus(Appointments appointment, String newStatus, AppointmentsDto appointmentsDto) {
        if (!NEXT_STATUS.containsKey(newStatus)) {
            throw new BusinessOutboundException("Invalid status, Use AGENDADO, CONFIRMADO, CONCLUIDO, CANCELADO or NAO_COMPARECEU");
        }
        String currentStatus = appointment.getStatus();
        if (!NEXT_STATUS.getOrDefault(currentStatus, Set.of()).contains(newStatus)) {
            throw new BusinessOutboundException("Could not update appointment, The status cannot change from " + currentStatus + " to " + newStatus);
        }

        if (CANCELADO.equals(newStatus)) {
            String cancelledBy = normalize(appointmentsDto.getCancelledBy());
            if (cancelledBy == null || !CANCELLED_BY.contains(cancelledBy)) {
                throw new BusinessOutboundException("Could not cancel appointment, Inform who cancelled it: CLIENTE or SALAO");
            }
            appointment.setCancelledBy(cancelledBy);
            appointment.setCancelReason(blankToNull(appointmentsDto.getCancelReason()));
            appointment.setCancelledAt(OffsetDateTime.now());
        }

        appointment.setStatus(newStatus);
    }

    private List<AppointmentsDto> toDtoListOrNotFound(List<Appointments> appointments) {
        if (appointments.isEmpty()) {
            throw new BusinessOutboundException(NOT_FOUND);
        }
        return appointments.stream().map(this::toDto).toList();
    }

    private AppointmentsDto toDto(Appointments appointment) {
        List<AppointmentsItems> items = appointment.getItems() == null ? List.of() : appointment.getItems();
        return AppointmentsDto.builder()
                .id(appointment.getId())
                .idClient(appointment.getClientId())
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .cancelledBy(appointment.getCancelledBy())
                .cancelReason(appointment.getCancelReason())
                .cancelledAt(appointment.getCancelledAt())
                .created(appointment.getCreated())
                .updated(appointment.getUpdated())
                .items(items.stream().map(this::toItemDto).toList())
                .build();
    }

    private AppointmentsItemsDto toItemDto(AppointmentsItems item) {
        return AppointmentsItemsDto.builder()
                .id(item.getId())
                .appointment_id(item.getAppointment_id())
                .service_id(item.getService_id())
                .professional_id(item.getProfessional_id())
                .resource_id(item.getResource_id())
                .start_at(item.getStart_at())
                .end_at(item.getEnd_at())
                .price(item.getPrice())
                .build();
    }

    // "confirmado " vira "CONFIRMADO"; texto vazio vira null.
    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim().toUpperCase();
    }

    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
