package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Payments;
import com.belezaearte.salaobeleza.application.domain.dto.PaymentsDto;
import com.belezaearte.salaobeleza.application.domain.dto.PaymentsRevenueDto;
import com.belezaearte.salaobeleza.application.domain.dto.PaymentsSummaryDto;
import com.belezaearte.salaobeleza.application.port.out.paymentsPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentsServiceTest {

    @InjectMocks
    private PaymentsService paymentsService;

    @Mock
    private CreatePaymentsPort createPaymentsPort;

    @Mock
    private UpdatePaymentsPort updatePaymentsPort;

    @Mock
    private DeletePaymentsPort deletePaymentsPort;

    @Mock
    private SearchAppointmentPaymentsPort searchAppointmentPaymentsPort;

    @Mock
    private SearchStatusPaymentsPort searchStatusPaymentsPort;

    @Mock
    private SearchItemsTotalPaymentsPort searchItemsTotalPaymentsPort;

    @Mock
    private SearchAppointmentStatusPaymentsPort searchAppointmentStatusPaymentsPort;

    @Mock
    private ListPaymentsPort listPaymentsPort;

    @Mock
    private SearchPaidTotalPaymentsPort searchPaidTotalPaymentsPort;

    @Mock
    private SearchStatusTotalPaymentsPort searchStatusTotalPaymentsPort;

    @Mock
    private SearchStatusCountPaymentsPort searchStatusCountPaymentsPort;

    @Mock
    private SearchCreatedTotalPaymentsPort searchCreatedTotalPaymentsPort;

    @Mock
    private SearchIdPaymentsPort searchIdPaymentsPort;

    private void appointmentWithStatus(String status) {
        when(searchAppointmentStatusPaymentsPort.searchAppointmentStatus(1)).thenReturn(Optional.of(status));
    }

    private Payments payment(int id, String amount, String status) {
        return Payments.builder().id(id).appointmentId(1).amount(new BigDecimal(amount)).status(status).build();
    }

    @Test
    @DisplayName("The first service of an appointment creates a PENDENTE payment by PIX.")
    void firstServiceCreatesPendingPayment() {
        appointmentWithStatus("AGENDADO");
        when(searchAppointmentPaymentsPort.searchAppointment(1)).thenReturn(List.of());
        when(searchItemsTotalPaymentsPort.searchItemsTotal(1)).thenReturn(new BigDecimal("150.00"));

        paymentsService.generatePayments(1);

        ArgumentCaptor<Payments> created = ArgumentCaptor.forClass(Payments.class);
        verify(createPaymentsPort).create(created.capture());
        assertEquals(1, created.getValue().getAppointmentId());
        assertEquals(new BigDecimal("150.00"), created.getValue().getAmount());
        assertEquals("PENDENTE", created.getValue().getStatus());
        assertEquals("PIX", created.getValue().getMethod());
        assertNull(created.getValue().getPaidAt());
    }

    @Test
    @DisplayName("Adding another service updates the existing PENDENTE payment instead of creating a new one.")
    void anotherServiceUpdatesPendingPayment() {
        appointmentWithStatus("AGENDADO");
        when(searchAppointmentPaymentsPort.searchAppointment(1)).thenReturn(List.of(payment(10, "150.00", "PENDENTE")));
        when(searchItemsTotalPaymentsPort.searchItemsTotal(1)).thenReturn(new BigDecimal("210.00"));

        paymentsService.generatePayments(1);

        ArgumentCaptor<Payments> updated = ArgumentCaptor.forClass(Payments.class);
        verify(updatePaymentsPort).update(updated.capture());
        assertEquals(10, updated.getValue().getId());
        assertEquals(new BigDecimal("210.00"), updated.getValue().getAmount());
        verify(createPaymentsPort, never()).create(any());
    }

    @Test
    @DisplayName("What was already PAGO (ex: a Pix deposit) is discounted from the PENDENTE amount.")
    void paidAmountIsDiscounted() {
        appointmentWithStatus("CONFIRMADO");
        when(searchAppointmentPaymentsPort.searchAppointment(1)).thenReturn(List.of(payment(10, "100.00", "PAGO")));
        when(searchItemsTotalPaymentsPort.searchItemsTotal(1)).thenReturn(new BigDecimal("350.00"));

        paymentsService.generatePayments(1);

        ArgumentCaptor<Payments> created = ArgumentCaptor.forClass(Payments.class);
        verify(createPaymentsPort).create(created.capture());
        assertEquals(new BigDecimal("250.00"), created.getValue().getAmount());
    }

    @Test
    @DisplayName("Removing all services deletes the PENDENTE payment.")
    void noServicesDeletesPendingPayment() {
        appointmentWithStatus("AGENDADO");
        when(searchAppointmentPaymentsPort.searchAppointment(1)).thenReturn(List.of(payment(10, "150.00", "PENDENTE")));
        when(searchItemsTotalPaymentsPort.searchItemsTotal(1)).thenReturn(BigDecimal.ZERO);

        paymentsService.generatePayments(1);

        verify(deletePaymentsPort).delete(10);
        verify(createPaymentsPort, never()).create(any());
        verify(updatePaymentsPort, never()).update(any());
    }

    @Test
    @DisplayName("Nothing changes when the PENDENTE amount is already right.")
    void samePendingAmountIsNotUpdated() {
        appointmentWithStatus("AGENDADO");
        when(searchAppointmentPaymentsPort.searchAppointment(1)).thenReturn(List.of(payment(10, "150.00", "PENDENTE")));
        when(searchItemsTotalPaymentsPort.searchItemsTotal(1)).thenReturn(new BigDecimal("150.0"));

        paymentsService.generatePayments(1);

        verifyNoInteractions(createPaymentsPort, updatePaymentsPort, deletePaymentsPort);
    }

    @Test
    @DisplayName("A cancelled appointment does not generate payments.")
    void cancelledAppointmentDoesNotGeneratePayment() {
        appointmentWithStatus("CANCELADO");

        paymentsService.generatePayments(1);

        verifyNoInteractions(searchAppointmentPaymentsPort, createPaymentsPort, updatePaymentsPort, deletePaymentsPort);
    }

    @Test
    @DisplayName("Payments are found by status, ignoring upper/lower case.")
    void searchStatus() {
        when(searchStatusPaymentsPort.searchStatus("PENDENTE")).thenReturn(List.of(payment(10, "150.00", "PENDENTE")));

        List<PaymentsDto> result = paymentsService.searchStatus(" pendente ");

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getIdAppointment());
    }

    @Test
    @DisplayName("An unknown status is rejected.")
    void searchInvalidStatus() {
        assertThrows(BusinessOutboundException.class, () -> paymentsService.searchStatus("APROVADO"));
    }

    @Test
    @DisplayName("Cancelling an appointment turns only its PENDENTE payments into CANCELADO.")
    void cancelPayments() {
        Payments pending = payment(10, "250.00", "PENDENTE");
        Payments paid = payment(11, "100.00", "PAGO");
        when(searchAppointmentPaymentsPort.searchAppointment(1)).thenReturn(List.of(pending, paid));

        paymentsService.cancelPayments(1);

        assertEquals("CANCELADO", pending.getStatus());
        assertEquals("PAGO", paid.getStatus());
        verify(updatePaymentsPort).update(pending);
        verify(updatePaymentsPort, never()).update(paid);
    }

    @Test
    @DisplayName("The list without status returns every payment; an empty result is an empty list.")
    void listAllPayments() {
        when(listPaymentsPort.listPayments(null)).thenReturn(List.of());

        assertTrue(paymentsService.listPayments(" ").isEmpty());
        verify(listPaymentsPort).listPayments(null);
    }

    @Test
    @DisplayName("The list accepts the status in any case and rejects unknown ones.")
    void listPaymentsByStatus() {
        when(listPaymentsPort.listPayments("CANCELADO")).thenReturn(List.of());

        paymentsService.listPayments("cancelado");

        verify(listPaymentsPort).listPayments("CANCELADO");
        assertThrows(BusinessOutboundException.class, () -> paymentsService.listPayments("APROVADO"));
    }

    @Test
    @DisplayName("The summary compares the month with the previous one and calculates the receipt rate.")
    void summary() {
        YearMonth month = YearMonth.of(2026, 11);
        OffsetDateTime start = OffsetDateTime.parse("2026-11-01T00:00:00-03:00");
        OffsetDateTime end = OffsetDateTime.parse("2026-12-01T00:00:00-03:00");
        OffsetDateTime previousStart = OffsetDateTime.parse("2026-10-01T00:00:00-03:00");
        when(searchPaidTotalPaymentsPort.searchPaidTotal(start, end)).thenReturn(new BigDecimal("1160.00"));
        when(searchPaidTotalPaymentsPort.searchPaidTotal(previousStart, start)).thenReturn(new BigDecimal("1000.00"));
        when(searchCreatedTotalPaymentsPort.searchCreatedTotal("PAGO", start, end)).thenReturn(new BigDecimal("920.00"));
        when(searchCreatedTotalPaymentsPort.searchCreatedTotal("PENDENTE", start, end)).thenReturn(new BigDecimal("80.00"));
        when(searchStatusTotalPaymentsPort.searchStatusTotal("PENDENTE")).thenReturn(new BigDecimal("1280.00"));
        when(searchStatusCountPaymentsPort.searchStatusCount("PENDENTE")).thenReturn(4L);

        PaymentsSummaryDto result = paymentsService.searchSummary(month);

        assertEquals("2026-11", result.getMonth());
        assertEquals(new BigDecimal("1160.00"), result.getReceivedMonth());
        assertEquals(new BigDecimal("16"), result.getReceivedChangePercent());
        assertEquals(new BigDecimal("1280.00"), result.getPendingAmount());
        assertEquals(4L, result.getPendingAppointments());
        assertEquals(new BigDecimal("92"), result.getReceiptRatePercent());
    }

    @Test
    @DisplayName("Paying a PENDENTE payment saves PAGO, the chosen method and when it was paid.")
    void payPendingPayment() {
        Payments pending = payment(10, "150.00", "PENDENTE");
        pending.setMethod("PIX");
        when(searchIdPaymentsPort.searchId(10)).thenReturn(Optional.of(pending));
        when(updatePaymentsPort.update(any())).thenAnswer(invocation -> Optional.of(invocation.getArgument(0)));

        PaymentsDto result = paymentsService.payPayments(PaymentsDto.builder().id(10).method(" cartao_credito ").build());

        assertEquals("PAGO", result.getStatus());
        assertEquals("CARTAO_CREDITO", result.getMethod());
        assertNotNull(result.getPaidAt());
        assertEquals(new BigDecimal("150.00"), result.getAmount());
    }

    @Test
    @DisplayName("Only PENDENTE payments can be paid.")
    void payAlreadyPaidPayment() {
        when(searchIdPaymentsPort.searchId(10)).thenReturn(Optional.of(payment(10, "150.00", "PAGO")));

        assertThrows(BusinessOutboundException.class,
                () -> paymentsService.payPayments(PaymentsDto.builder().id(10).method("PIX").build()));
        verify(updatePaymentsPort, never()).update(any());
    }

    @Test
    @DisplayName("Paying requires a valid method and an existing payment.")
    void payInvalidData() {
        assertThrows(BusinessOutboundException.class,
                () -> paymentsService.payPayments(PaymentsDto.builder().id(10).method("CHEQUE").build()));
        assertThrows(BusinessOutboundException.class,
                () -> paymentsService.payPayments(PaymentsDto.builder().id(10).build()));
        when(searchIdPaymentsPort.searchId(99)).thenReturn(Optional.empty());
        assertThrows(BusinessOutboundException.class,
                () -> paymentsService.payPayments(PaymentsDto.builder().id(99).method("PIX").build()));
    }

    @Test
    @DisplayName("The revenue of the day is compared with the day before.")
    void revenueOfTheDay() {
        OffsetDateTime yesterday = OffsetDateTime.parse("2026-11-17T00:00:00-03:00");
        OffsetDateTime today = OffsetDateTime.parse("2026-11-18T00:00:00-03:00");
        OffsetDateTime tomorrow = OffsetDateTime.parse("2026-11-19T00:00:00-03:00");
        when(searchPaidTotalPaymentsPort.searchPaidTotal(today, tomorrow)).thenReturn(new BigDecimal("1240.00"));
        when(searchPaidTotalPaymentsPort.searchPaidTotal(yesterday, today)).thenReturn(new BigDecimal("1148.15"));

        PaymentsRevenueDto result = paymentsService.searchRevenue("day", LocalDate.of(2026, 11, 18));

        assertEquals("DAY", result.getPeriod());
        assertEquals(LocalDate.of(2026, 11, 18), result.getStart());
        assertEquals(LocalDate.of(2026, 11, 18), result.getEnd());
        assertEquals(new BigDecimal("1240.00"), result.getReceived());
        assertEquals(new BigDecimal("8"), result.getChangePercent());
    }

    @Test
    @DisplayName("The last 7 days include today and are compared with the 7 days before.")
    void revenueOfTheLastSevenDays() {
        OffsetDateTime previousStart = OffsetDateTime.parse("2026-11-05T00:00:00-03:00");
        OffsetDateTime start = OffsetDateTime.parse("2026-11-12T00:00:00-03:00");
        OffsetDateTime end = OffsetDateTime.parse("2026-11-19T00:00:00-03:00");
        when(searchPaidTotalPaymentsPort.searchPaidTotal(start, end)).thenReturn(new BigDecimal("500.00"));
        when(searchPaidTotalPaymentsPort.searchPaidTotal(previousStart, start)).thenReturn(BigDecimal.ZERO);

        PaymentsRevenueDto result = paymentsService.searchRevenue("LAST_7_DAYS", LocalDate.of(2026, 11, 18));

        assertEquals(LocalDate.of(2026, 11, 12), result.getStart());
        assertEquals(LocalDate.of(2026, 11, 18), result.getEnd());
        assertNull(result.getChangePercent());
    }

    @Test
    @DisplayName("The year goes from January 1st to December 31st; an unknown period is rejected.")
    void revenueOfTheYear() {
        when(searchPaidTotalPaymentsPort.searchPaidTotal(any(), any())).thenReturn(BigDecimal.ZERO);

        PaymentsRevenueDto result = paymentsService.searchRevenue("YEAR", LocalDate.of(2026, 11, 18));

        assertEquals(LocalDate.of(2026, 1, 1), result.getStart());
        assertEquals(LocalDate.of(2026, 12, 31), result.getEnd());
        assertThrows(BusinessOutboundException.class, () -> paymentsService.searchRevenue("SEMANA", null));
    }

    @Test
    @DisplayName("Without data, the percentages are null instead of dividing by zero.")
    void summaryWithoutData() {
        when(searchPaidTotalPaymentsPort.searchPaidTotal(any(), any())).thenReturn(BigDecimal.ZERO);
        when(searchCreatedTotalPaymentsPort.searchCreatedTotal(any(), any(), any())).thenReturn(BigDecimal.ZERO);
        when(searchStatusTotalPaymentsPort.searchStatusTotal("PENDENTE")).thenReturn(BigDecimal.ZERO);

        PaymentsSummaryDto result = paymentsService.searchSummary(YearMonth.of(2026, 11));

        assertNull(result.getReceivedChangePercent());
        assertNull(result.getReceiptRatePercent());
    }

    @Test
    @DisplayName("An appointment without payments returns not found.")
    void searchAppointmentWithoutPayments() {
        when(searchAppointmentPaymentsPort.searchAppointment(1)).thenReturn(List.of());

        assertThrows(BusinessOutboundException.class, () -> paymentsService.searchAppointment(1));
    }
}
