package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.PaymentsListView;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.PaymentsRepository;
import com.belezaearte.salaobeleza.application.port.out.paymentsPort.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class PaymentsAdapter implements CreatePaymentsPort, UpdatePaymentsPort, DeletePaymentsPort, SearchAppointmentPaymentsPort, SearchStatusPaymentsPort, SearchItemsTotalPaymentsPort, SearchAppointmentStatusPaymentsPort, ListPaymentsPort, SearchPaidTotalPaymentsPort, SearchStatusTotalPaymentsPort, SearchStatusCountPaymentsPort, SearchCreatedTotalPaymentsPort, SearchIdPaymentsPort {

    private final PaymentsRepository paymentsRepository;

    @Override
    public Optional<Payments> searchId(Integer id) {
        return id == null ? Optional.empty() : paymentsRepository.findById(id);
    }

    @Override
    public Optional<Payments> create(Payments payments) {
        return Optional.of(paymentsRepository.saveAndFlush(payments));
    }

    @Override
    public Optional<Payments> update(Payments payments) {
        if (payments.getId() == null || !paymentsRepository.existsById(payments.getId())) {
            return Optional.empty();
        }
        return Optional.of(paymentsRepository.saveAndFlush(payments));
    }

    @Override
    public void delete(Integer id) {
        paymentsRepository.deleteById(id);
        paymentsRepository.flush();
    }

    @Override
    public List<Payments> searchAppointment(Integer idAppointment) {
        return paymentsRepository.findByAppointmentIdOrderByIdAsc(idAppointment);
    }

    @Override
    public List<Payments> searchStatus(String status) {
        return paymentsRepository.findByStatusOrderByIdAsc(status);
    }

    @Override
    public BigDecimal searchItemsTotal(Integer idAppointment) {
        return paymentsRepository.sumItemsPrice(idAppointment);
    }

    @Override
    public Optional<String> searchAppointmentStatus(Integer idAppointment) {
        return paymentsRepository.findAppointmentStatus(idAppointment);
    }

    @Override
    public List<PaymentsListView> listPayments(String status) {
        return paymentsRepository.listPayments(status);
    }

    @Override
    public BigDecimal searchPaidTotal(OffsetDateTime start, OffsetDateTime end) {
        return paymentsRepository.sumPaidBetween(start, end);
    }

    @Override
    public BigDecimal searchStatusTotal(String status) {
        return paymentsRepository.sumStatus(status);
    }

    @Override
    public long searchStatusCount(String status) {
        return paymentsRepository.countAppointmentsWithStatus(status);
    }

    @Override
    public BigDecimal searchCreatedTotal(String status, OffsetDateTime start, OffsetDateTime end) {
        return paymentsRepository.sumStatusCreatedBetween(status, start, end);
    }
}
