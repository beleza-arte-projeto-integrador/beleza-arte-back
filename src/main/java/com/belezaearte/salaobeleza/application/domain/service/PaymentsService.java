package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Payments;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.PaymentsListView;
import com.belezaearte.salaobeleza.application.domain.dto.PaymentsDto;
import com.belezaearte.salaobeleza.application.domain.dto.PaymentsListDto;
import com.belezaearte.salaobeleza.application.domain.dto.PaymentsRevenueDto;
import com.belezaearte.salaobeleza.application.domain.dto.PaymentsSummaryDto;
import com.belezaearte.salaobeleza.application.port.in.Payments.*;
import com.belezaearte.salaobeleza.application.port.out.paymentsPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PaymentsService implements GeneratePaymentsUseCase, CancelPaymentsUseCase, PayPaymentsUseCase, SearchAppointmentPaymentsUseCase, SearchStatusPaymentsUseCase, ListPaymentsUseCase, SearchSummaryPaymentsUseCase, SearchRevenuePaymentsUseCase {

    private static final String NOT_FOUND = "Could not find payment!, The requested payment was not found in the system!";
    private static final String INVALID_STATUS = "Invalid status, Use PENDENTE, PAGO, CANCELADO or ESTORNADO";

    static final String PENDENTE = "PENDENTE";
    static final String PAGO = "PAGO";
    static final String CANCELADO = "CANCELADO";
    static final String ESTORNADO = "ESTORNADO";
    private static final Set<String> STATUS = Set.of(PENDENTE, PAGO, CANCELADO, ESTORNADO);

    static final String DEFAULT_METHOD = "PIX";
    private static final Set<String> METHODS = Set.of("PIX", "CARTAO_CREDITO", "CARTAO_DEBITO", "DINHEIRO");
    private static final Set<String> PERIODS = Set.of("DAY", "LAST_7_DAYS", "MONTH", "YEAR");

    private static final ZoneId SALON_ZONE = ZoneId.of("America/Sao_Paulo");
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    @Autowired
    private CreatePaymentsPort createPaymentsPort;
    @Autowired
    private UpdatePaymentsPort updatePaymentsPort;
    @Autowired
    private DeletePaymentsPort deletePaymentsPort;
    @Autowired
    private SearchAppointmentPaymentsPort searchAppointmentPaymentsPort;
    @Autowired
    private SearchStatusPaymentsPort searchStatusPaymentsPort;
    @Autowired
    private SearchItemsTotalPaymentsPort searchItemsTotalPaymentsPort;
    @Autowired
    private SearchAppointmentStatusPaymentsPort searchAppointmentStatusPaymentsPort;
    @Autowired
    private ListPaymentsPort listPaymentsPort;
    @Autowired
    private SearchPaidTotalPaymentsPort searchPaidTotalPaymentsPort;
    @Autowired
    private SearchStatusTotalPaymentsPort searchStatusTotalPaymentsPort;
    @Autowired
    private SearchStatusCountPaymentsPort searchStatusCountPaymentsPort;
    @Autowired
    private SearchCreatedTotalPaymentsPort searchCreatedTotalPaymentsPort;
    @Autowired
    private SearchIdPaymentsPort searchIdPaymentsPort;

    @Override
    @Transactional
    public void generatePayments(Integer idAppointment) {
        if (idAppointment == null) {
            return;
        }
        boolean cancelled = searchAppointmentStatusPaymentsPort.searchAppointmentStatus(idAppointment)
                .map(AppointmentsService.CANCELADO::equals)
                .orElse(true);
        if (cancelled) {
            return;
        }

        List<Payments> payments = searchAppointmentPaymentsPort.searchAppointment(idAppointment);
        Optional<Payments> current = payments.stream()
                .filter(payment -> PENDENTE.equals(payment.getStatus()))
                .max(Comparator.comparing(Payments::getId));

        BigDecimal paid = sum(payments, PAGO, null);
        BigDecimal otherPending = sum(payments, PENDENTE, current.map(Payments::getId).orElse(null));
        BigDecimal outstanding = searchItemsTotalPaymentsPort.searchItemsTotal(idAppointment)
                .subtract(paid)
                .subtract(otherPending);

        if (outstanding.signum() > 0) {
            if (current.isPresent()) {
                Payments payment = current.get();
                if (payment.getAmount().compareTo(outstanding) != 0) {
                    payment.setAmount(outstanding);
                    updatePaymentsPort.update(payment);
                }
            } else {
                createPaymentsPort.create(Payments.builder()
                        .appointmentId(idAppointment)
                        .amount(outstanding)
                        .method(DEFAULT_METHOD)
                        .status(PENDENTE)
                        .build());
            }
        } else {
            current.ifPresent(payment -> deletePaymentsPort.delete(payment.getId()));
        }
    }

    @Override
    @Transactional
    public void cancelPayments(Integer idAppointment) {
        if (idAppointment == null) {
            return;
        }
        searchAppointmentPaymentsPort.searchAppointment(idAppointment).stream()
                .filter(payment -> PENDENTE.equals(payment.getStatus()))
                .forEach(payment -> {
                    payment.setStatus(CANCELADO);
                    updatePaymentsPort.update(payment);
                });
    }

    @Override
    public List<PaymentsDto> searchAppointment(Integer idAppointment) {
        return toDtoListOrNotFound(searchAppointmentPaymentsPort.searchAppointment(idAppointment));
    }

    @Override
    public List<PaymentsDto> searchStatus(String status) {
        String normalized = normalize(status);
        if (normalized == null || !STATUS.contains(normalized)) {
            throw new BusinessOutboundException(INVALID_STATUS);
        }
        return toDtoListOrNotFound(searchStatusPaymentsPort.searchStatus(normalized));
    }

    @Override
    public List<PaymentsListDto> listPayments(String status) {
        String normalized = normalize(status);
        if (normalized != null && !STATUS.contains(normalized)) {
            throw new BusinessOutboundException(INVALID_STATUS);
        }
        return listPaymentsPort.listPayments(normalized).stream().map(this::toListDto).toList();
    }

    @Override
    public PaymentsSummaryDto searchSummary(YearMonth month) {
        YearMonth target = month == null ? YearMonth.now(SALON_ZONE) : month;
        OffsetDateTime start = startOf(target);
        OffsetDateTime end = startOf(target.plusMonths(1));
        OffsetDateTime previousStart = startOf(target.minusMonths(1));

        BigDecimal received = searchPaidTotalPaymentsPort.searchPaidTotal(start, end);
        BigDecimal receivedPrevious = searchPaidTotalPaymentsPort.searchPaidTotal(previousStart, start);
        BigDecimal paidCreated = searchCreatedTotalPaymentsPort.searchCreatedTotal(PAGO, start, end);
        BigDecimal pendingCreated = searchCreatedTotalPaymentsPort.searchCreatedTotal(PENDENTE, start, end);

        return PaymentsSummaryDto.builder()
                .month(target.toString())
                .receivedMonth(received)
                .receivedPreviousMonth(receivedPrevious)
                .receivedChangePercent(receivedPrevious.signum() == 0 ? null
                        : percent(received.subtract(receivedPrevious), receivedPrevious))
                .pendingAmount(searchStatusTotalPaymentsPort.searchStatusTotal(PENDENTE))
                .pendingAppointments(searchStatusCountPaymentsPort.searchStatusCount(PENDENTE))
                .receiptRatePercent(paidCreated.add(pendingCreated).signum() == 0 ? null
                        : percent(paidCreated, paidCreated.add(pendingCreated)))
                .build();
    }

    @Override
    @Transactional
    public PaymentsDto payPayments(PaymentsDto paymentsDto) {
        if (paymentsDto == null || paymentsDto.getId() == null) {
            throw new BusinessOutboundException("Could not pay, The payment id is required");
        }
        String method = normalize(paymentsDto.getMethod());
        if (method == null || !METHODS.contains(method)) {
            throw new BusinessOutboundException("Invalid method, Use PIX, CARTAO_CREDITO, CARTAO_DEBITO or DINHEIRO");
        }
        Payments payment = searchIdPaymentsPort.searchId(paymentsDto.getId())
                .orElseThrow(() -> new BusinessOutboundException("Could not pay, Check if the payment exists"));
        if (!PENDENTE.equals(payment.getStatus())) {
            throw new BusinessOutboundException("Could not pay, Only PENDENTE payments can be paid and this one is " + payment.getStatus());
        }

        payment.setMethod(method);
        payment.setStatus(PAGO);
        payment.setPaidAt(OffsetDateTime.now(SALON_ZONE));

        return toDto(updatePaymentsPort.update(payment)
                .orElseThrow(() -> new BusinessOutboundException("Could not pay, Check if the payment exists")));
    }

    @Override
    public PaymentsRevenueDto searchRevenue(String period, LocalDate date) {
        String normalized = normalize(period);
        if (normalized == null || !PERIODS.contains(normalized)) {
            throw new BusinessOutboundException("Invalid period, Use DAY, LAST_7_DAYS, MONTH or YEAR");
        }
        LocalDate reference = date == null ? LocalDate.now(SALON_ZONE) : date;

        LocalDate start;
        LocalDate end;
        LocalDate previousStart;
        switch (normalized) {
            case "DAY" -> {
                start = reference;
                end = reference.plusDays(1);
                previousStart = reference.minusDays(1);
            }
            case "LAST_7_DAYS" -> {
                start = reference.minusDays(6);
                end = reference.plusDays(1);
                previousStart = start.minusDays(7);
            }
            case "MONTH" -> {
                start = reference.withDayOfMonth(1);
                end = start.plusMonths(1);
                previousStart = start.minusMonths(1);
            }
            default -> {
                start = reference.withDayOfYear(1);
                end = start.plusYears(1);
                previousStart = start.minusYears(1);
            }
        }

        BigDecimal received = searchPaidTotalPaymentsPort.searchPaidTotal(atStart(start), atStart(end));
        BigDecimal receivedPrevious = searchPaidTotalPaymentsPort.searchPaidTotal(atStart(previousStart), atStart(start));

        return PaymentsRevenueDto.builder()
                .period(normalized)
                .start(start)
                .end(end.minusDays(1))
                .received(received)
                .receivedPrevious(receivedPrevious)
                .changePercent(receivedPrevious.signum() == 0 ? null
                        : percent(received.subtract(receivedPrevious), receivedPrevious))
                .build();
    }

    private OffsetDateTime atStart(LocalDate day) {
        return day.atStartOfDay(SALON_ZONE).toOffsetDateTime();
    }

    private OffsetDateTime startOf(YearMonth month) {
        return month.atDay(1).atStartOfDay(SALON_ZONE).toOffsetDateTime();
    }

    private BigDecimal percent(BigDecimal part, BigDecimal total) {
        return part.multiply(HUNDRED).divide(total, 0, RoundingMode.HALF_UP);
    }

    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim().toUpperCase();
    }

    private BigDecimal sum(List<Payments> payments, String status, Integer exceptId) {
        return payments.stream()
                .filter(payment -> status.equals(payment.getStatus()))
                .filter(payment -> !payment.getId().equals(exceptId))
                .map(Payments::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<PaymentsDto> toDtoListOrNotFound(List<Payments> payments) {
        if (payments.isEmpty()) {
            throw new BusinessOutboundException(NOT_FOUND);
        }
        return payments.stream().map(this::toDto).toList();
    }

    private PaymentsDto toDto(Payments payment) {
        return PaymentsDto.builder()
                .id(payment.getId())
                .idAppointment(payment.getAppointmentId())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .paidAt(payment.getPaidAt())
                .created(payment.getCreated())
                .build();
    }

    private PaymentsListDto toListDto(PaymentsListView view) {
        return PaymentsListDto.builder()
                .id(view.getId())
                .idAppointment(view.getIdAppointment())
                .clientName(view.getClientName())
                .services(view.getServices())
                .appointmentDate(toSalonTime(view.getAppointmentDate()))
                .amount(view.getAmount())
                .method(view.getMethod())
                .status(view.getStatus())
                .paidAt(toSalonTime(view.getPaidAt()))
                .created(toSalonTime(view.getCreated()))
                .build();
    }

    private OffsetDateTime toSalonTime(Instant instant) {
        return instant == null ? null : instant.atZone(SALON_ZONE).toOffsetDateTime();
    }
}
