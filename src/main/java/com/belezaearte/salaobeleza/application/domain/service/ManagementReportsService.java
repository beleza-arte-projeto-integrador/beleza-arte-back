package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;
import com.belezaearte.salaobeleza.adapter.out.persistence.AppointmentsItems;
import com.belezaearte.salaobeleza.adapter.out.persistence.ProfessionalAvailability;
import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;
import com.belezaearte.salaobeleza.adapter.out.persistence.Services;
import com.belezaearte.salaobeleza.application.domain.dto.ManagementReportDto;
import com.belezaearte.salaobeleza.application.domain.dto.ReportProfessionalDto;
import com.belezaearte.salaobeleza.application.domain.dto.ReportServiceDto;
import com.belezaearte.salaobeleza.application.port.in.ManagementReports.SearchManagementReportUseCase;
import com.belezaearte.salaobeleza.application.port.out.appointmentsPort.SearchDateAppointmentsPort;
import com.belezaearte.salaobeleza.application.port.out.managementReportsPort.SearchReportProfessionalsPort;
import com.belezaearte.salaobeleza.application.port.out.managementReportsPort.SearchReportServicesPort;
import com.belezaearte.salaobeleza.application.port.out.professionalAvailabilityPort.SearchAllProfessionalAvailabilityPort;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

// Relatorios gerenciais: indicadores do salao em um periodo.
// Responsavel: Matheus
@Service
public class ManagementReportsService implements SearchManagementReportUseCase {

    // O salao fica no Brasil: "um dia" vai de 00:00 a 23:59 no horario de Brasilia.
    private static final ZoneId SALON_ZONE = ZoneId.of("America/Sao_Paulo");
    private static final DateTimeFormatter HOUR = DateTimeFormatter.ofPattern("HH:mm");

    // Limite do periodo, para o relatorio nao ficar pesado.
    static final int MAX_DAYS = 366;

    static final String CONCLUIDO = "CONCLUIDO";
    static final String CANCELADO = "CANCELADO";
    static final String NAO_COMPARECEU = "NAO_COMPARECEU";

    @Autowired
    private SearchDateAppointmentsPort searchDateAppointmentsPort;
    @Autowired
    private SearchReportServicesPort searchReportServicesPort;
    @Autowired
    private SearchReportProfessionalsPort searchReportProfessionalsPort;
    @Autowired
    private SearchAllProfessionalAvailabilityPort searchAllProfessionalAvailabilityPort;

    @Override
    public ManagementReportDto searchManagementReport(LocalDate startDate, LocalDate endDate, Integer idProfessional) {
        if (startDate == null || endDate == null) {
            throw new BusinessOutboundException("The start date and the end date are required.");
        }
        if (endDate.isBefore(startDate)) {
            throw new BusinessOutboundException("The end date must be on or after the start date.");
        }
        if (ChronoUnit.DAYS.between(startDate, endDate) + 1 > MAX_DAYS) {
            throw new BusinessOutboundException("The period can have at most " + MAX_DAYS + " days.");
        }

        OffsetDateTime start = startDate.atStartOfDay(SALON_ZONE).toOffsetDateTime();
        OffsetDateTime end = endDate.plusDays(1).atStartOfDay(SALON_ZONE).toOffsetDateTime();

        List<Appointments> appointments = Optional.ofNullable(searchDateAppointmentsPort.searchDate(start, end)).orElse(List.of());

        Map<Integer, String> serviceNames = new HashMap<>();
        for (Services service : Optional.ofNullable(searchReportServicesPort.searchAllServices()).orElse(List.of())) {
            serviceNames.put(service.getId(), service.getName());
        }

        Map<Integer, String> professionalNames = new HashMap<>();
        for (Professionals professional : Optional.ofNullable(searchReportProfessionalsPort.searchAllProfessionals()).orElse(List.of())) {
            professionalNames.put(professional.getId(), professional.getName());
        }

        int totalAppointments = 0;
        int completedAppointments = 0;
        int cancelledAppointments = 0;
        int noShowAppointments = 0;
        int validAppointments = 0;
        int totalServices = 0;
        long bookedMinutes = 0;
        BigDecimal expectedRevenue = BigDecimal.ZERO;
        BigDecimal completedRevenue = BigDecimal.ZERO;
        Set<Integer> clients = new HashSet<>();

        Map<Integer, ReportServiceDto> services = new HashMap<>();
        Map<Integer, ReportProfessionalDto> professionals = new HashMap<>();
        Integer[] byWeekday = {0, 0, 0, 0, 0, 0, 0};
        Map<String, Integer> byHour = new TreeMap<>();

        for (Appointments appointment : appointments) {
            // So os servicos da visita que caem no periodo (e do profissional escolhido, se houver filtro).
            List<AppointmentsItems> items = itemsInPeriod(appointment, start, end, idProfessional);

            if (items.isEmpty()) {
                continue;
            }

            String status = appointment.getStatus() == null ? "" : appointment.getStatus().toUpperCase();
            boolean cancelled = CANCELADO.equals(status);
            boolean noShow = NAO_COMPARECEU.equals(status);

            totalAppointments++;

            if (cancelled) {
                cancelledAppointments++;
            } else if (noShow) {
                noShowAppointments++;
            } else {
                validAppointments++;
                if (appointment.getClientId() != null) {
                    clients.add(appointment.getClientId());
                }
            }

            if (CONCLUIDO.equals(status)) {
                completedAppointments++;
            }

            for (AppointmentsItems item : items) {
                ReportProfessionalDto professional = professionals.computeIfAbsent(item.getProfessional_id(), id ->
                        ReportProfessionalDto.builder()
                                .idProfessional(id)
                                .name(professionalNames.getOrDefault(id, "Professional " + id))
                                .services(0)
                                .revenue(BigDecimal.ZERO)
                                .cancelledServices(0)
                                .noShowServices(0)
                                .build());

                if (cancelled) {
                    professional.setCancelledServices(professional.getCancelledServices() + 1);
                    continue;
                }
                if (noShow) {
                    professional.setNoShowServices(professional.getNoShowServices() + 1);
                    continue;
                }

                BigDecimal price = item.getPrice() == null ? BigDecimal.ZERO : item.getPrice();

                totalServices++;
                expectedRevenue = expectedRevenue.add(price);
                if (CONCLUIDO.equals(status)) {
                    completedRevenue = completedRevenue.add(price);
                }

                professional.setServices(professional.getServices() + 1);
                professional.setRevenue(professional.getRevenue().add(price));

                ReportServiceDto service = services.computeIfAbsent(item.getService_id(), id ->
                        ReportServiceDto.builder()
                                .idService(id)
                                .name(serviceNames.getOrDefault(id, "Service " + id))
                                .quantity(0)
                                .revenue(BigDecimal.ZERO)
                                .build());
                service.setQuantity(service.getQuantity() + 1);
                service.setRevenue(service.getRevenue().add(price));

                ZonedDateTime localStart = item.getStart_at().atZoneSameInstant(SALON_ZONE);
                byWeekday[localStart.getDayOfWeek().getValue() % 7]++;
                byHour.merge(localStart.toLocalTime().format(HOUR), 1, Integer::sum);

                if (item.getEnd_at() != null && item.getEnd_at().isAfter(item.getStart_at())) {
                    bookedMinutes += Duration.between(item.getStart_at(), item.getEnd_at()).toMinutes();
                }
            }
        }

        long availableMinutes = availableMinutes(startDate, endDate, idProfessional);

        List<ReportServiceDto> serviceRanking = new ArrayList<>(services.values());
        serviceRanking.sort(Comparator.comparing(ReportServiceDto::getQuantity).reversed()
                .thenComparing(ReportServiceDto::getRevenue, Comparator.reverseOrder())
                .thenComparing(ReportServiceDto::getName, Comparator.nullsLast(Comparator.naturalOrder())));

        List<ReportProfessionalDto> professionalRanking = new ArrayList<>(professionals.values());
        professionalRanking.sort(Comparator.comparing(ReportProfessionalDto::getServices).reversed()
                .thenComparing(ReportProfessionalDto::getRevenue, Comparator.reverseOrder())
                .thenComparing(ReportProfessionalDto::getName, Comparator.nullsLast(Comparator.naturalOrder())));

        return ManagementReportDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .idProfessional(idProfessional)
                .totalAppointments(totalAppointments)
                .completedAppointments(completedAppointments)
                .cancelledAppointments(cancelledAppointments)
                .noShowAppointments(noShowAppointments)
                .cancellationRate(percent(cancelledAppointments, totalAppointments))
                .noShowRate(percent(noShowAppointments, totalAppointments))
                .totalServices(totalServices)
                .expectedRevenue(money(expectedRevenue))
                .completedRevenue(money(completedRevenue))
                .averageTicket(validAppointments == 0 ? money(BigDecimal.ZERO)
                        : expectedRevenue.divide(BigDecimal.valueOf(validAppointments), 2, RoundingMode.HALF_UP))
                .uniqueClients(clients.size())
                .availableMinutes((int) availableMinutes)
                .bookedMinutes((int) bookedMinutes)
                .occupancyRate(availableMinutes == 0 ? null : percent(bookedMinutes, availableMinutes))
                .services(serviceRanking)
                .professionals(professionalRanking)
                .servicesByWeekday(Arrays.asList(byWeekday))
                .servicesByHour(byHour)
                .build();
    }

    // Servicos da visita que comecam dentro do periodo [start, end).
    private List<AppointmentsItems> itemsInPeriod(Appointments appointment, OffsetDateTime start, OffsetDateTime end, Integer idProfessional) {
        List<AppointmentsItems> result = new ArrayList<>();

        if (appointment == null || appointment.getItems() == null) {
            return result;
        }

        for (AppointmentsItems item : appointment.getItems()) {
            if (item == null || item.getStart_at() == null) {
                continue;
            }
            boolean inPeriod = !item.getStart_at().isBefore(start) && item.getStart_at().isBefore(end);
            boolean sameProfessional = idProfessional == null || idProfessional.equals(item.getProfessional_id());

            if (inPeriod && sameProfessional) {
                result.add(item);
            }
        }

        return result;
    }

    // Minutos de expediente no periodo, somando as faixas de professional_availability de cada dia.
    long availableMinutes(LocalDate startDate, LocalDate endDate, Integer idProfessional) {
        List<ProfessionalAvailability> ranges = Optional.ofNullable(searchAllProfessionalAvailabilityPort.searchAll()).orElse(List.of());

        long[] minutesByWeekday = new long[7];

        for (ProfessionalAvailability range : ranges) {
            if (range.getWeekday() == null || range.getStartTime() == null || range.getEndTime() == null) {
                continue;
            }
            if (idProfessional != null && !idProfessional.equals(range.getProfessionalId())) {
                continue;
            }
            int weekday = range.getWeekday();
            if (weekday < 0 || weekday > 6 || !range.getStartTime().isBefore(range.getEndTime())) {
                continue;
            }
            minutesByWeekday[weekday] += Duration.between(range.getStartTime(), range.getEndTime()).toMinutes();
        }

        long total = 0;
        for (LocalDate day = startDate; !day.isAfter(endDate); day = day.plusDays(1)) {
            total += minutesByWeekday[day.getDayOfWeek().getValue() % 7];
        }
        return total;
    }

    // Percentual com 1 casa decimal (ex: 1 de 8 = 12.5).
    private static BigDecimal percent(long part, long total) {
        if (total == 0) {
            return BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP);
        }
        return BigDecimal.valueOf(part * 100.0 / total).setScale(1, RoundingMode.HALF_UP);
    }

    private static BigDecimal money(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
