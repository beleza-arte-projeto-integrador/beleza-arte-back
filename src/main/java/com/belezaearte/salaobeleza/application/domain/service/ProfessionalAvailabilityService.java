package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;
import com.belezaearte.salaobeleza.adapter.out.persistence.AppointmentsItems;
import com.belezaearte.salaobeleza.adapter.out.persistence.ProfessionalAvailability;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ProfessionalsRepository;
import com.belezaearte.salaobeleza.application.domain.dto.AvailableTimeDto;
import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalAvailabilityDto;
import com.belezaearte.salaobeleza.application.port.in.ProfessionalAvailability.*;
import com.belezaearte.salaobeleza.application.port.out.appointmentsPort.SearchDateAppointmentsPort;
import com.belezaearte.salaobeleza.application.port.out.professionalAvailabilityPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

// Disponibilidade (horario de trabalho) dos profissionais - tabela professional_availability.
// Responsavel: Matheus
@Service
public class ProfessionalAvailabilityService implements CreateProfessionalAvailabilityUseCase, UpdateProfessionalAvailabilityUseCase, DeleteProfessionalAvailabilityUseCase, SearchProfessionalAvailabilityUseCase, SearchAvailableTimesUseCase {

    private static final String NOT_FOUND = "Could not find the availability! The requested availability was not found in the system.";

    // O salao fica no Brasil: os horarios da agenda sao no horario de Brasilia.
    private static final ZoneId SALON_ZONE = ZoneId.of("America/Sao_Paulo");

    // Valores padrao da busca de horarios livres (em minutos).
    static final int DEFAULT_DURATION = 60;
    static final int DEFAULT_INTERVAL = 60;

    @Autowired
    private CreateProfessionalAvailabilityPort createProfessionalAvailabilityPort;
    @Autowired
    private UpdateProfessionalAvailabilityPort updateProfessionalAvailabilityPort;
    @Autowired
    private DeleteProfessionalAvailabilityPort deleteProfessionalAvailabilityPort;
    @Autowired
    private SearchIdProfessionalAvailabilityPort searchIdProfessionalAvailabilityPort;
    @Autowired
    private SearchProfessionalAvailabilityPort searchProfessionalAvailabilityPort;
    @Autowired
    private SearchDateAppointmentsPort searchDateAppointmentsPort;
    @Autowired
    private ProfessionalsRepository professionalsRepository;

    @Override
    public ProfessionalAvailabilityDto createProfessionalAvailability(ProfessionalAvailabilityDto professionalAvailabilityDto) {
        validate(professionalAvailabilityDto);

        ProfessionalAvailability entity = toEntity(professionalAvailabilityDto);
        entity.setId(null);

        try {
            Optional<ProfessionalAvailability> saved = createProfessionalAvailabilityPort.create(entity);
            return toDto(saved.orElseThrow());
        } catch (BusinessOutboundException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessOutboundException("Could not create the availability. Please check whether it has already been registered.");
        }
    }

    @Override
    public ProfessionalAvailabilityDto updateProfessionalAvailability(ProfessionalAvailabilityDto professionalAvailabilityDto) {
        if (professionalAvailabilityDto == null || professionalAvailabilityDto.getId() == null) {
            throw new BusinessOutboundException("Could not update the availability. The id is required.");
        }
        if (searchIdProfessionalAvailabilityPort.searchId(professionalAvailabilityDto.getId()).isEmpty()) {
            throw new BusinessOutboundException(NOT_FOUND);
        }

        validate(professionalAvailabilityDto);

        Optional<ProfessionalAvailability> updated = updateProfessionalAvailabilityPort.update(toEntity(professionalAvailabilityDto));
        if (updated == null || updated.isEmpty()) {
            throw new BusinessOutboundException(NOT_FOUND);
        }
        return toDto(updated.get());
    }

    @Override
    public void deleteProfessionalAvailability(Integer id) {
        if (id == null || searchIdProfessionalAvailabilityPort.searchId(id).isEmpty()) {
            throw new BusinessOutboundException("Could not delete the availability. Please check whether the availability exists.");
        }
        deleteProfessionalAvailabilityPort.delete(id);
    }

    // Lista o horario de trabalho do profissional. Lista vazia = ainda nao cadastrou o horario.
    @Override
    public List<ProfessionalAvailabilityDto> searchProfessionalAvailability(Integer idProfessional) {
        if (idProfessional == null) {
            throw new BusinessOutboundException("The professional id is required.");
        }
        return searchProfessionalAvailabilityPort.searchProfessional(idProfessional).stream()
                .map(this::toDto)
                .toList();
    }

    // Horarios livres do profissional no dia:
    // 1) pega as faixas de trabalho do dia da semana;
    // 2) divide em horarios a cada "intervalMinutes", com a duracao do servico;
    // 3) tira os horarios que batem com outro servico ja agendado (agendamentos cancelados nao contam).
    @Override
    public List<AvailableTimeDto> searchAvailableTimes(Integer idProfessional, LocalDate date, Integer durationMinutes, Integer intervalMinutes) {
        if (idProfessional == null || date == null) {
            throw new BusinessOutboundException("The professional id and the date are required.");
        }

        int duration = durationMinutes == null ? DEFAULT_DURATION : durationMinutes;
        int interval = intervalMinutes == null ? DEFAULT_INTERVAL : intervalMinutes;

        if (duration < 1 || duration > 720) {
            throw new BusinessOutboundException("The duration must be between 1 and 720 minutes.");
        }
        if (interval < 5 || interval > 240) {
            throw new BusinessOutboundException("The interval must be between 5 and 240 minutes.");
        }
        if (!professionalsRepository.existsById(idProfessional)) {
            throw new BusinessOutboundException("Could not find the professional! The requested professional was not found in the system.");
        }

        short weekday = weekdayOf(date);
        List<ProfessionalAvailability> ranges = searchProfessionalAvailabilityPort.searchProfessionalWeekday(idProfessional, weekday);

        if (ranges.isEmpty()) {
            return List.of();
        }

        OffsetDateTime dayStart = date.atStartOfDay(SALON_ZONE).toOffsetDateTime();
        OffsetDateTime dayEnd = date.plusDays(1).atStartOfDay(SALON_ZONE).toOffsetDateTime();
        List<AppointmentsItems> busy = busyItems(searchDateAppointmentsPort.searchDate(dayStart, dayEnd), idProfessional);

        List<AvailableTimeDto> free = new ArrayList<>();

        for (ProfessionalAvailability range : ranges) {
            int rangeStart = range.getStartTime().toSecondOfDay() / 60;
            int rangeEnd = range.getEndTime().toSecondOfDay() / 60;

            for (int minute = rangeStart; minute + duration <= rangeEnd; minute += interval) {
                LocalTime start = LocalTime.ofSecondOfDay(minute * 60L);
                OffsetDateTime startAt = date.atTime(start).atZone(SALON_ZONE).toOffsetDateTime();
                OffsetDateTime endAt = startAt.plusMinutes(duration);

                if (!hasConflict(startAt, endAt, busy) && free.stream().noneMatch(t -> t.getStartAt().isEqual(startAt))) {
                    free.add(AvailableTimeDto.builder()
                            .startTime(start)
                            .endTime(start.plusMinutes(duration))
                            .startAt(startAt)
                            .endAt(endAt)
                            .build());
                }
            }
        }

        free.sort(Comparator.comparing(AvailableTimeDto::getStartAt));
        return free;
    }

    // ---------- Regras ----------

    private void validate(ProfessionalAvailabilityDto dto) {
        if (dto == null || dto.getIdProfessional() == null) {
            throw new BusinessOutboundException("The professional id is required.");
        }
        if (dto.getWeekday() == null || dto.getWeekday() < 0 || dto.getWeekday() > 6) {
            throw new BusinessOutboundException("The weekday must be a number from 0 (Sunday) to 6 (Saturday).");
        }
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new BusinessOutboundException("The start time and the end time are required.");
        }
        if (!dto.getStartTime().isBefore(dto.getEndTime())) {
            throw new BusinessOutboundException("The start time must be before the end time.");
        }
        if (!professionalsRepository.existsById(dto.getIdProfessional())) {
            throw new BusinessOutboundException("Could not find the professional! The requested professional was not found in the system.");
        }

        // Nao deixa cadastrar duas faixas que se sobrepoem no mesmo dia (ex: 09-12 e 11-14).
        List<ProfessionalAvailability> sameDay = searchProfessionalAvailabilityPort
                .searchProfessionalWeekday(dto.getIdProfessional(), dto.getWeekday().shortValue());

        boolean overlaps = sameDay.stream()
                .filter(existing -> dto.getId() == null || !dto.getId().equals(existing.getId()))
                .anyMatch(existing -> dto.getStartTime().isBefore(existing.getEndTime())
                        && dto.getEndTime().isAfter(existing.getStartTime()));

        if (overlaps) {
            throw new BusinessOutboundException("This time range overlaps another time range of the professional on the same day.");
        }
    }

    // Itens do profissional que ocupam a agenda (agendamentos cancelados liberam o horario).
    private List<AppointmentsItems> busyItems(List<Appointments> appointments, Integer idProfessional) {
        List<AppointmentsItems> items = new ArrayList<>();

        if (appointments == null) {
            return items;
        }

        for (Appointments appointment : appointments) {
            if ("CANCELADO".equalsIgnoreCase(appointment.getStatus()) || appointment.getItems() == null) {
                continue;
            }
            for (AppointmentsItems item : appointment.getItems()) {
                if (idProfessional.equals(item.getProfessional_id()) && item.getStart_at() != null) {
                    items.add(item);
                }
            }
        }

        return items;
    }

    // Mesma regra de sobreposicao do dicionario de dados:
    // novo.inicio < existente.fim  e  novo.fim > existente.inicio
    private boolean hasConflict(OffsetDateTime start, OffsetDateTime end, List<AppointmentsItems> busy) {
        for (AppointmentsItems item : busy) {
            OffsetDateTime itemEnd = item.getEnd_at() != null ? item.getEnd_at() : item.getStart_at();
            if (start.isBefore(itemEnd) && end.isAfter(item.getStart_at())) {
                return true;
            }
        }
        return false;
    }

    // DayOfWeek do Java: 1 = segunda ... 7 = domingo. No banco: 0 = domingo ... 6 = sabado.
    static short weekdayOf(LocalDate date) {
        return (short) (date.getDayOfWeek().getValue() % 7);
    }

    private ProfessionalAvailability toEntity(ProfessionalAvailabilityDto dto) {
        return ProfessionalAvailability.builder()
                .id(dto.getId())
                .professionalId(dto.getIdProfessional())
                .weekday(dto.getWeekday().shortValue())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();
    }

    private ProfessionalAvailabilityDto toDto(ProfessionalAvailability entity) {
        return ProfessionalAvailabilityDto.builder()
                .id(entity.getId())
                .idProfessional(entity.getProfessionalId())
                .weekday(entity.getWeekday() == null ? null : entity.getWeekday().intValue())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .build();
    }
}
