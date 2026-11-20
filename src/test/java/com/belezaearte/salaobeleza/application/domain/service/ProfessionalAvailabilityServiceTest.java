package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;
import com.belezaearte.salaobeleza.adapter.out.persistence.AppointmentsItems;
import com.belezaearte.salaobeleza.adapter.out.persistence.ProfessionalAvailability;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ProfessionalsRepository;
import com.belezaearte.salaobeleza.application.domain.dto.AvailableTimeDto;
import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalAvailabilityDto;
import com.belezaearte.salaobeleza.application.port.out.appointmentsPort.SearchDateAppointmentsPort;
import com.belezaearte.salaobeleza.application.port.out.professionalAvailabilityPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Testes da disponibilidade dos profissionais (Matheus).
@ExtendWith(MockitoExtension.class)
public class ProfessionalAvailabilityServiceTest {

    @InjectMocks
    private ProfessionalAvailabilityService professionalAvailabilityService;

    @Mock
    private CreateProfessionalAvailabilityPort createProfessionalAvailabilityPort;

    @Mock
    private UpdateProfessionalAvailabilityPort updateProfessionalAvailabilityPort;

    @Mock
    private DeleteProfessionalAvailabilityPort deleteProfessionalAvailabilityPort;

    @Mock
    private SearchIdProfessionalAvailabilityPort searchIdProfessionalAvailabilityPort;

    @Mock
    private SearchProfessionalAvailabilityPort searchProfessionalAvailabilityPort;

    @Mock
    private SearchDateAppointmentsPort searchDateAppointmentsPort;

    @Mock
    private ProfessionalsRepository professionalsRepository;

    // 24/11/2026 e uma terca-feira (weekday 2).
    private static final LocalDate TUESDAY = LocalDate.of(2026, 11, 24);
    private static final ZoneOffset BRASILIA = ZoneOffset.ofHours(-3);

    private ProfessionalAvailabilityDto dto(Integer id, int weekday, String start, String end) {
        return ProfessionalAvailabilityDto.builder()
                .id(id).idProfessional(1).weekday(weekday)
                .startTime(LocalTime.parse(start)).endTime(LocalTime.parse(end))
                .build();
    }

    private ProfessionalAvailability range(Integer id, int weekday, String start, String end) {
        return ProfessionalAvailability.builder()
                .id(id).professionalId(1).weekday((short) weekday)
                .startTime(LocalTime.parse(start)).endTime(LocalTime.parse(end))
                .build();
    }

    private Appointments appointment(String status, int professionalId, String startTime, int minutes) {
        OffsetDateTime start = TUESDAY.atTime(LocalTime.parse(startTime)).atOffset(BRASILIA);
        AppointmentsItems item = AppointmentsItems.builder()
                .id(10).appointment_id(5).service_id(3).professional_id(professionalId)
                .start_at(start).end_at(start.plusMinutes(minutes)).price(new BigDecimal("80.00"))
                .build();
        return Appointments.builder().id(5).clientId(7).status(status).items(List.of(item)).build();
    }

    private List<String> startTimes(List<AvailableTimeDto> times) {
        return times.stream().map(t -> t.getStartTime().toString()).toList();
    }

    @Test
    @DisplayName("A valid time range is created and returned with the generated id.")
    void createAvailability() {
        when(professionalsRepository.existsById(1)).thenReturn(true);
        when(searchProfessionalAvailabilityPort.searchProfessionalWeekday(1, (short) 1)).thenReturn(List.of());
        when(createProfessionalAvailabilityPort.create(any())).thenAnswer(invocation -> {
            ProfessionalAvailability saved = invocation.getArgument(0);
            saved.setId(20);
            return Optional.of(saved);
        });

        ProfessionalAvailabilityDto result = professionalAvailabilityService.createProfessionalAvailability(dto(null, 1, "09:00", "12:00"));

        assertEquals(20, result.getId());
        assertEquals(1, result.getIdProfessional());
        assertEquals(1, result.getWeekday());
        assertEquals(LocalTime.of(9, 0), result.getStartTime());
        assertEquals(LocalTime.of(12, 0), result.getEndTime());
    }

    @Test
    @DisplayName("A time range whose start is not before the end is not created.")
    void createWithInvalidTimes() {
        assertThrows(BusinessOutboundException.class, () ->
                professionalAvailabilityService.createProfessionalAvailability(dto(null, 1, "18:00", "09:00")));
        assertThrows(BusinessOutboundException.class, () ->
                professionalAvailabilityService.createProfessionalAvailability(dto(null, 1, "09:00", "09:00")));
        verify(createProfessionalAvailabilityPort, never()).create(any());
    }

    @Test
    @DisplayName("A weekday outside 0..6 is not accepted.")
    void createWithInvalidWeekday() {
        assertThrows(BusinessOutboundException.class, () ->
                professionalAvailabilityService.createProfessionalAvailability(dto(null, 7, "09:00", "12:00")));
        assertThrows(BusinessOutboundException.class, () ->
                professionalAvailabilityService.createProfessionalAvailability(dto(null, -1, "09:00", "12:00")));
        verify(createProfessionalAvailabilityPort, never()).create(any());
    }

    @Test
    @DisplayName("A time range for a professional that does not exist is not created.")
    void createProfessionalNotFound() {
        when(professionalsRepository.existsById(1)).thenReturn(false);

        assertThrows(BusinessOutboundException.class, () ->
                professionalAvailabilityService.createProfessionalAvailability(dto(null, 1, "09:00", "12:00")));
        verify(createProfessionalAvailabilityPort, never()).create(any());
    }

    @Test
    @DisplayName("A time range that overlaps another one on the same day is not created.")
    void createOverlapping() {
        when(professionalsRepository.existsById(1)).thenReturn(true);
        when(searchProfessionalAvailabilityPort.searchProfessionalWeekday(1, (short) 1))
                .thenReturn(List.of(range(3, 1, "09:00", "12:00")));

        assertThrows(BusinessOutboundException.class, () ->
                professionalAvailabilityService.createProfessionalAvailability(dto(null, 1, "11:00", "14:00")));
        verify(createProfessionalAvailabilityPort, never()).create(any());
    }

    @Test
    @DisplayName("A lunch break works: 13:00-18:00 can be added after 09:00-12:00.")
    void createSecondRangeSameDay() {
        when(professionalsRepository.existsById(1)).thenReturn(true);
        when(searchProfessionalAvailabilityPort.searchProfessionalWeekday(1, (short) 1))
                .thenReturn(List.of(range(3, 1, "09:00", "12:00")));
        when(createProfessionalAvailabilityPort.create(any())).thenAnswer(invocation -> Optional.of(invocation.getArgument(0)));

        ProfessionalAvailabilityDto result = professionalAvailabilityService.createProfessionalAvailability(dto(null, 1, "13:00", "18:00"));

        assertEquals(LocalTime.of(13, 0), result.getStartTime());
    }

    @Test
    @DisplayName("Updating a time range does not count as overlapping with itself.")
    void updateAvailability() {
        when(searchIdProfessionalAvailabilityPort.searchId(3)).thenReturn(Optional.of(range(3, 1, "09:00", "12:00")));
        when(professionalsRepository.existsById(1)).thenReturn(true);
        when(searchProfessionalAvailabilityPort.searchProfessionalWeekday(1, (short) 1))
                .thenReturn(List.of(range(3, 1, "09:00", "12:00")));
        when(updateProfessionalAvailabilityPort.update(any())).thenAnswer(invocation -> Optional.of(invocation.getArgument(0)));

        ProfessionalAvailabilityDto result = professionalAvailabilityService.updateProfessionalAvailability(dto(3, 1, "08:00", "12:00"));

        assertEquals(3, result.getId());
        assertEquals(LocalTime.of(8, 0), result.getStartTime());
    }

    @Test
    @DisplayName("Updating a time range that does not exist is not allowed.")
    void updateNotFound() {
        when(searchIdProfessionalAvailabilityPort.searchId(99)).thenReturn(Optional.empty());

        assertThrows(BusinessOutboundException.class, () ->
                professionalAvailabilityService.updateProfessionalAvailability(dto(99, 1, "09:00", "12:00")));
        verify(updateProfessionalAvailabilityPort, never()).update(any());
    }

    @Test
    @DisplayName("An existing time range is deleted; a missing one is not.")
    void deleteAvailability() {
        when(searchIdProfessionalAvailabilityPort.searchId(3)).thenReturn(Optional.of(range(3, 1, "09:00", "12:00")));
        when(searchIdProfessionalAvailabilityPort.searchId(99)).thenReturn(Optional.empty());

        professionalAvailabilityService.deleteProfessionalAvailability(3);

        verify(deleteProfessionalAvailabilityPort, times(1)).delete(3);
        assertThrows(BusinessOutboundException.class, () -> professionalAvailabilityService.deleteProfessionalAvailability(99));
        verify(deleteProfessionalAvailabilityPort, never()).delete(99);
    }

    @Test
    @DisplayName("Listing the availability of a professional without ranges returns an empty list.")
    void searchEmpty() {
        when(searchProfessionalAvailabilityPort.searchProfessional(1)).thenReturn(List.of());

        assertTrue(professionalAvailabilityService.searchProfessionalAvailability(1).isEmpty());
    }

    @Test
    @DisplayName("Free times follow the working hours of the weekday, including the lunch break.")
    void availableTimesFollowWorkingHours() {
        when(professionalsRepository.existsById(1)).thenReturn(true);
        when(searchProfessionalAvailabilityPort.searchProfessionalWeekday(1, (short) 2)).thenReturn(List.of(
                range(1, 2, "09:00", "12:00"),
                range(2, 2, "13:00", "15:00")));
        when(searchDateAppointmentsPort.searchDate(any(), any())).thenReturn(List.of());

        List<AvailableTimeDto> result = professionalAvailabilityService.searchAvailableTimes(1, TUESDAY, 60, 60);

        assertEquals(List.of("09:00", "10:00", "11:00", "13:00", "14:00"), startTimes(result));
        assertEquals(TUESDAY.atTime(9, 0).atOffset(BRASILIA), result.get(0).getStartAt());
        assertEquals(LocalTime.of(10, 0), result.get(0).getEndTime());
    }

    @Test
    @DisplayName("Times already booked are removed, but cancelled appointments free the time.")
    void availableTimesWithoutConflicts() {
        when(professionalsRepository.existsById(1)).thenReturn(true);
        when(searchProfessionalAvailabilityPort.searchProfessionalWeekday(1, (short) 2))
                .thenReturn(List.of(range(1, 2, "09:00", "13:00")));
        when(searchDateAppointmentsPort.searchDate(any(), any())).thenReturn(List.of(
                appointment("AGENDADO", 1, "10:00", 90),   // ocupa 10:00-11:30
                appointment("CANCELADO", 1, "12:00", 60),  // cancelado: nao ocupa
                appointment("AGENDADO", 2, "09:00", 60))); // outro profissional

        List<AvailableTimeDto> result = professionalAvailabilityService.searchAvailableTimes(1, TUESDAY, 60, 60);

        assertEquals(List.of("09:00", "12:00"), startTimes(result));
    }

    @Test
    @DisplayName("The service duration must fit inside the working hours.")
    void availableTimesRespectDuration() {
        when(professionalsRepository.existsById(1)).thenReturn(true);
        when(searchProfessionalAvailabilityPort.searchProfessionalWeekday(1, (short) 2))
                .thenReturn(List.of(range(1, 2, "09:00", "12:00")));
        when(searchDateAppointmentsPort.searchDate(any(), any())).thenReturn(List.of());

        List<AvailableTimeDto> result = professionalAvailabilityService.searchAvailableTimes(1, TUESDAY, 90, 30);

        assertEquals(List.of("09:00", "09:30", "10:00", "10:30"), startTimes(result));
    }

    @Test
    @DisplayName("A day the professional does not work has no free times.")
    void availableTimesDayOff() {
        when(professionalsRepository.existsById(1)).thenReturn(true);
        when(searchProfessionalAvailabilityPort.searchProfessionalWeekday(1, (short) 2)).thenReturn(List.of());

        assertTrue(professionalAvailabilityService.searchAvailableTimes(1, TUESDAY, null, null).isEmpty());
        verify(searchDateAppointmentsPort, never()).searchDate(any(), any());
    }

    @Test
    @DisplayName("Invalid duration or a professional that does not exist are not accepted.")
    void availableTimesInvalid() {
        assertThrows(BusinessOutboundException.class, () -> professionalAvailabilityService.searchAvailableTimes(1, TUESDAY, 0, 60));
        assertThrows(BusinessOutboundException.class, () -> professionalAvailabilityService.searchAvailableTimes(1, null, 60, 60));

        when(professionalsRepository.existsById(99)).thenReturn(false);
        assertThrows(BusinessOutboundException.class, () -> professionalAvailabilityService.searchAvailableTimes(99, TUESDAY, 60, 60));
    }

    @Test
    @DisplayName("Weekday follows the database: 0 = Sunday ... 6 = Saturday.")
    void weekdayOf() {
        assertEquals(0, ProfessionalAvailabilityService.weekdayOf(LocalDate.of(2026, 11, 22)));
        assertEquals(2, ProfessionalAvailabilityService.weekdayOf(TUESDAY));
        assertEquals(6, ProfessionalAvailabilityService.weekdayOf(LocalDate.of(2026, 11, 28)));
    }
}
