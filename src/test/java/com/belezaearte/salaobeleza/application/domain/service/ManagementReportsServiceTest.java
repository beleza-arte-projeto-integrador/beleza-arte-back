package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;
import com.belezaearte.salaobeleza.adapter.out.persistence.AppointmentsItems;
import com.belezaearte.salaobeleza.adapter.out.persistence.ProfessionalAvailability;
import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;
import com.belezaearte.salaobeleza.adapter.out.persistence.Services;
import com.belezaearte.salaobeleza.application.domain.dto.ManagementReportDto;
import com.belezaearte.salaobeleza.application.domain.dto.ReportProfessionalDto;
import com.belezaearte.salaobeleza.application.domain.dto.ReportServiceDto;
import com.belezaearte.salaobeleza.application.port.out.appointmentsPort.SearchDateAppointmentsPort;
import com.belezaearte.salaobeleza.application.port.out.managementReportsPort.SearchReportProfessionalsPort;
import com.belezaearte.salaobeleza.application.port.out.managementReportsPort.SearchReportServicesPort;
import com.belezaearte.salaobeleza.application.port.out.professionalAvailabilityPort.SearchAllProfessionalAvailabilityPort;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Testes dos relatorios gerenciais (Matheus).
@ExtendWith(MockitoExtension.class)
public class ManagementReportsServiceTest {

    @InjectMocks
    private ManagementReportsService managementReportsService;

    @Mock
    private SearchDateAppointmentsPort searchDateAppointmentsPort;

    @Mock
    private SearchReportServicesPort searchReportServicesPort;

    @Mock
    private SearchReportProfessionalsPort searchReportProfessionalsPort;

    @Mock
    private SearchAllProfessionalAvailabilityPort searchAllProfessionalAvailabilityPort;

    // Semana de 23/11/2026 (segunda) a 29/11/2026 (domingo).
    private static final LocalDate MONDAY = LocalDate.of(2026, 11, 23);
    private static final LocalDate SUNDAY = LocalDate.of(2026, 11, 29);
    private static final ZoneOffset BRASILIA = ZoneOffset.ofHours(-3);

    private int nextItemId = 1;

    private AppointmentsItems item(int professionalId, int serviceId, LocalDate date, String time, int minutes, String price) {
        OffsetDateTime start = date.atTime(LocalTime.parse(time)).atOffset(BRASILIA);
        return AppointmentsItems.builder()
                .id(nextItemId++).service_id(serviceId).professional_id(professionalId)
                .start_at(start).end_at(start.plusMinutes(minutes)).price(new BigDecimal(price))
                .build();
    }

    private Appointments appointment(int id, int clientId, String status, AppointmentsItems... items) {
        return Appointments.builder().id(id).clientId(clientId).status(status).items(new ArrayList<>(List.of(items))).build();
    }

    private List<Appointments> sampleWeek() {
        return List.of(
                // Cliente 7: corte (Anna) + manicure (Camila) na segunda, concluido
                appointment(1, 7, "CONCLUIDO",
                        item(1, 10, MONDAY, "09:00", 60, "50.00"),
                        item(2, 20, MONDAY, "10:00", 60, "60.00")),
                // Cliente 8: corte (Anna) na terca, agendado
                appointment(2, 8, "AGENDADO",
                        item(1, 10, MONDAY.plusDays(1), "14:00", 60, "50.00")),
                // Cliente 7 de novo: mechas (Anna) no sabado, confirmado
                appointment(3, 7, "CONFIRMADO",
                        item(1, 30, MONDAY.plusDays(5), "09:00", 180, "250.00")),
                // Cliente 9: cancelou a manicure (Camila)
                appointment(4, 9, "CANCELADO",
                        item(2, 20, MONDAY.plusDays(2), "11:00", 60, "60.00")),
                // Cliente 10: faltou no corte (Anna)
                appointment(5, 10, "NAO_COMPARECEU",
                        item(1, 10, MONDAY.plusDays(3), "16:00", 60, "50.00")));
    }

    private void namesAndNoAvailability() {
        when(searchReportServicesPort.searchAllServices()).thenReturn(List.of(
                Services.builder().id(10).name("Corte").build(),
                Services.builder().id(20).name("Manicure").build(),
                Services.builder().id(30).name("Mechas").build()));
        when(searchReportProfessionalsPort.searchAllProfessionals()).thenReturn(List.of(
                Professionals.builder().id(1).name("Anna").build(),
                Professionals.builder().id(2).name("Camila").build()));
        when(searchAllProfessionalAvailabilityPort.searchAll()).thenReturn(List.of());
    }

    @Test
    @DisplayName("The report counts appointments by status and calculates the rates.")
    void countsAndRates() {
        when(searchDateAppointmentsPort.searchDate(any(), any())).thenReturn(sampleWeek());
        namesAndNoAvailability();

        ManagementReportDto report = managementReportsService.searchManagementReport(MONDAY, SUNDAY, null);

        assertEquals(5, report.getTotalAppointments());
        assertEquals(1, report.getCompletedAppointments());
        assertEquals(1, report.getCancelledAppointments());
        assertEquals(1, report.getNoShowAppointments());
        assertEquals(new BigDecimal("20.0"), report.getCancellationRate());
        assertEquals(new BigDecimal("20.0"), report.getNoShowRate());
    }

    @Test
    @DisplayName("Revenue only counts services that were not cancelled and had no no-show.")
    void revenue() {
        when(searchDateAppointmentsPort.searchDate(any(), any())).thenReturn(sampleWeek());
        namesAndNoAvailability();

        ManagementReportDto report = managementReportsService.searchManagementReport(MONDAY, SUNDAY, null);

        assertEquals(4, report.getTotalServices());
        assertEquals(new BigDecimal("410.00"), report.getExpectedRevenue());
        assertEquals(new BigDecimal("110.00"), report.getCompletedRevenue());
        // 410 / 3 agendamentos validos
        assertEquals(new BigDecimal("136.67"), report.getAverageTicket());
        // Clientes 7 e 8 (o 7 voltou; 9 cancelou e 10 faltou)
        assertEquals(2, report.getUniqueClients());
    }

    @Test
    @DisplayName("Services are ranked by quantity and then by revenue, with their names.")
    void serviceRanking() {
        when(searchDateAppointmentsPort.searchDate(any(), any())).thenReturn(sampleWeek());
        namesAndNoAvailability();

        List<ReportServiceDto> services = managementReportsService.searchManagementReport(MONDAY, SUNDAY, null).getServices();

        assertEquals(List.of("Corte", "Mechas", "Manicure"), services.stream().map(ReportServiceDto::getName).toList());
        assertEquals(2, services.get(0).getQuantity());
        assertEquals(new BigDecimal("100.00"), services.get(0).getRevenue());
    }

    @Test
    @DisplayName("Each professional shows services, revenue, cancellations and no-shows.")
    void professionalPerformance() {
        when(searchDateAppointmentsPort.searchDate(any(), any())).thenReturn(sampleWeek());
        namesAndNoAvailability();

        List<ReportProfessionalDto> professionals = managementReportsService.searchManagementReport(MONDAY, SUNDAY, null).getProfessionals();

        ReportProfessionalDto anna = professionals.get(0);
        assertEquals("Anna", anna.getName());
        assertEquals(3, anna.getServices());
        assertEquals(new BigDecimal("350.00"), anna.getRevenue());
        assertEquals(0, anna.getCancelledServices());
        assertEquals(1, anna.getNoShowServices());

        ReportProfessionalDto camila = professionals.get(1);
        assertEquals("Camila", camila.getName());
        assertEquals(1, camila.getServices());
        assertEquals(1, camila.getCancelledServices());
    }

    @Test
    @DisplayName("Services are grouped by weekday (0 = Sunday) and by start hour.")
    void weekdayAndHour() {
        when(searchDateAppointmentsPort.searchDate(any(), any())).thenReturn(sampleWeek());
        namesAndNoAvailability();

        ManagementReportDto report = managementReportsService.searchManagementReport(MONDAY, SUNDAY, null);

        // Segunda: 2, terca: 1, sabado: 1
        assertEquals(List.of(0, 2, 1, 0, 0, 0, 1), report.getServicesByWeekday());
        assertEquals(List.of("09:00", "10:00", "14:00"), new ArrayList<>(report.getServicesByHour().keySet()));
        assertEquals(2, report.getServicesByHour().get("09:00"));
    }

    @Test
    @DisplayName("Filtering by professional keeps only the services of that professional.")
    void filterByProfessional() {
        when(searchDateAppointmentsPort.searchDate(any(), any())).thenReturn(sampleWeek());
        namesAndNoAvailability();

        ManagementReportDto report = managementReportsService.searchManagementReport(MONDAY, SUNDAY, 2);

        // Camila: manicure concluida (60) + manicure cancelada
        assertEquals(2, report.getTotalAppointments());
        assertEquals(1, report.getTotalServices());
        assertEquals(new BigDecimal("60.00"), report.getExpectedRevenue());
        assertEquals(1, report.getProfessionals().size());
        assertEquals(new BigDecimal("50.0"), report.getCancellationRate());
    }

    @Test
    @DisplayName("Services outside the period are ignored, even if the appointment is returned.")
    void ignoresServicesOutsidePeriod() {
        Appointments twoDays = appointment(1, 7, "AGENDADO",
                item(1, 10, MONDAY, "09:00", 60, "50.00"),
                item(1, 10, SUNDAY.plusDays(1), "09:00", 60, "50.00"));
        when(searchDateAppointmentsPort.searchDate(any(), any())).thenReturn(List.of(twoDays));
        namesAndNoAvailability();

        ManagementReportDto report = managementReportsService.searchManagementReport(MONDAY, SUNDAY, null);

        assertEquals(1, report.getTotalServices());
        assertEquals(new BigDecimal("50.00"), report.getExpectedRevenue());
    }

    @Test
    @DisplayName("Occupancy = booked minutes / working minutes from professional_availability.")
    void occupancy() {
        when(searchDateAppointmentsPort.searchDate(any(), any())).thenReturn(sampleWeek());
        when(searchReportServicesPort.searchAllServices()).thenReturn(List.of());
        when(searchReportProfessionalsPort.searchAllProfessionals()).thenReturn(List.of());
        // Anna trabalha de segunda a sabado, 09:00-19:00 (600 min/dia x 6 dias = 3600 min)
        List<ProfessionalAvailability> anna = new ArrayList<>();
        for (short weekday = 1; weekday <= 6; weekday++) {
            anna.add(ProfessionalAvailability.builder().id(weekday + 0).professionalId(1).weekday(weekday)
                    .startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(19, 0)).build());
        }
        when(searchAllProfessionalAvailabilityPort.searchAll()).thenReturn(anna);

        ManagementReportDto report = managementReportsService.searchManagementReport(MONDAY, SUNDAY, 1);

        // Anna: corte 60 + corte 60 + mechas 180 = 300 min (a falta nao conta)
        assertEquals(3600, report.getAvailableMinutes());
        assertEquals(300, report.getBookedMinutes());
        assertEquals(new BigDecimal("8.3"), report.getOccupancyRate());
    }

    @Test
    @DisplayName("Without working hours registered, the occupancy is empty (null).")
    void occupancyWithoutAvailability() {
        when(searchDateAppointmentsPort.searchDate(any(), any())).thenReturn(sampleWeek());
        namesAndNoAvailability();

        assertNull(managementReportsService.searchManagementReport(MONDAY, SUNDAY, null).getOccupancyRate());
    }

    @Test
    @DisplayName("A period without appointments returns zeros instead of an error.")
    void emptyPeriod() {
        when(searchDateAppointmentsPort.searchDate(any(), any())).thenReturn(List.of());
        namesAndNoAvailability();

        ManagementReportDto report = managementReportsService.searchManagementReport(MONDAY, SUNDAY, null);

        assertEquals(0, report.getTotalAppointments());
        assertEquals(new BigDecimal("0.00"), report.getExpectedRevenue());
        assertEquals(new BigDecimal("0.00"), report.getAverageTicket());
        assertEquals(new BigDecimal("0.0"), report.getCancellationRate());
        assertTrue(report.getServices().isEmpty());
    }

    @Test
    @DisplayName("Invalid periods are not accepted.")
    void invalidPeriod() {
        assertThrows(BusinessOutboundException.class, () -> managementReportsService.searchManagementReport(null, SUNDAY, null));
        assertThrows(BusinessOutboundException.class, () -> managementReportsService.searchManagementReport(SUNDAY, MONDAY, null));
        assertThrows(BusinessOutboundException.class, () -> managementReportsService.searchManagementReport(
                LocalDate.of(2025, 1, 1), LocalDate.of(2026, 12, 31), null));
        verify(searchDateAppointmentsPort, never()).searchDate(any(), any());
    }
}
