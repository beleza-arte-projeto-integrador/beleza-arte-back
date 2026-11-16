package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Appointments;
import com.belezaearte.salaobeleza.adapter.out.persistence.AppointmentsItems;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.AppointmentsRepository;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ClientsRepository;
import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsDto;
import com.belezaearte.salaobeleza.application.port.out.appointmentsPort.*;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentsServiceTest {

    @InjectMocks
    private AppointmentsService appointmentsService;

    @Mock
    private CreateAppointmentsPort createAppointmentsPort;

    @Mock
    private UpdateAppointmentsPort updateAppointmentsPort;

    @Mock
    private DeleteAppointmentsPort deleteAppointmentsPort;

    @Mock
    private SearchStatusAppointmentsPort searchStatusAppointmentsPort;

    @Mock
    private SearchDateAppointmentsPort searchDateAppointmentsPort;

    @Mock
    private SearchClientAppointmentsPort searchClientAppointmentsPort;

    @Mock
    private AppointmentsRepository appointmentsRepository;

    @Mock
    private ClientsRepository clientsRepository;

    private Appointments appointment(String status) {
        return Appointments.builder().id(1).clientId(5).status(status).notes("Primeira visita").build();
    }

    private void updateReturnsWhatIsSaved() {
        when(updateAppointmentsPort.update(any())).thenAnswer(invocation -> Optional.of(invocation.getArgument(0)));
    }

    @Test
    @DisplayName("A new appointment is created as AGENDADO, even if another status is sent.")
    void createAppointment() {
        when(clientsRepository.existsById(5)).thenReturn(true);
        when(createAppointmentsPort.create(any())).thenAnswer(invocation -> {
            Appointments a = invocation.getArgument(0);
            a.setId(1);
            return Optional.of(a);
        });

        AppointmentsDto result = appointmentsService.createAppointments(
                AppointmentsDto.builder().idClient(5).status("CONCLUIDO").notes("  Primeira visita ").build());

        assertEquals(1, result.getId());
        assertEquals(5, result.getIdClient());
        assertEquals("AGENDADO", result.getStatus());
        assertEquals("Primeira visita", result.getNotes());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    @DisplayName("An appointment without a client is not created.")
    void createAppointmentWithoutClient() {
        assertThrows(BusinessOutboundException.class, () -> appointmentsService.createAppointments(
                AppointmentsDto.builder().build()));
        verify(createAppointmentsPort, never()).create(any());
    }

    @Test
    @DisplayName("An appointment for a client that does not exist is not created.")
    void createAppointmentClientNotFound() {
        when(clientsRepository.existsById(99)).thenReturn(false);

        assertThrows(BusinessOutboundException.class, () -> appointmentsService.createAppointments(
                AppointmentsDto.builder().idClient(99).build()));
        verify(createAppointmentsPort, never()).create(any());
    }

    @Test
    @DisplayName("An AGENDADO appointment can be confirmed, and notes not sent are kept.")
    void confirmAppointment() {
        when(appointmentsRepository.findById(1)).thenReturn(Optional.of(appointment("AGENDADO")));
        updateReturnsWhatIsSaved();

        AppointmentsDto result = appointmentsService.updateAppointments(
                AppointmentsDto.builder().id(1).status("confirmado").build());

        assertEquals("CONFIRMADO", result.getStatus());
        assertEquals("Primeira visita", result.getNotes());
    }

    @Test
    @DisplayName("Cancelling saves who cancelled, the reason and when.")
    void cancelAppointment() {
        when(appointmentsRepository.findById(1)).thenReturn(Optional.of(appointment("CONFIRMADO")));
        updateReturnsWhatIsSaved();

        AppointmentsDto result = appointmentsService.updateAppointments(AppointmentsDto.builder()
                .id(1).status("CANCELADO").cancelledBy("salao").cancelReason("Profissional doente").build());

        assertEquals("CANCELADO", result.getStatus());
        assertEquals("SALAO", result.getCancelledBy());
        assertEquals("Profissional doente", result.getCancelReason());
        assertNotNull(result.getCancelledAt());
    }

    @Test
    @DisplayName("Cancelling without saying who cancelled is not allowed.")
    void cancelWithoutCancelledBy() {
        when(appointmentsRepository.findById(1)).thenReturn(Optional.of(appointment("AGENDADO")));

        assertThrows(BusinessOutboundException.class, () -> appointmentsService.updateAppointments(
                AppointmentsDto.builder().id(1).status("CANCELADO").build()));
        verify(updateAppointmentsPort, never()).update(any());
    }

    @Test
    @DisplayName("An AGENDADO appointment cannot go straight to CONCLUIDO.")
    void invalidStatusTransition() {
        when(appointmentsRepository.findById(1)).thenReturn(Optional.of(appointment("AGENDADO")));

        assertThrows(BusinessOutboundException.class, () -> appointmentsService.updateAppointments(
                AppointmentsDto.builder().id(1).status("CONCLUIDO").build()));
        verify(updateAppointmentsPort, never()).update(any());
    }

    @Test
    @DisplayName("A cancelled appointment cannot change status again.")
    void finalStatusCannotChange() {
        when(appointmentsRepository.findById(1)).thenReturn(Optional.of(appointment("CANCELADO")));

        assertThrows(BusinessOutboundException.class, () -> appointmentsService.updateAppointments(
                AppointmentsDto.builder().id(1).status("CONFIRMADO").build()));
        verify(updateAppointmentsPort, never()).update(any());
    }

    @Test
    @DisplayName("Only a CONFIRMADO appointment can be marked as NAO_COMPARECEU.")
    void noShowOnlyAfterConfirmed() {
        when(appointmentsRepository.findById(1)).thenReturn(Optional.of(appointment("AGENDADO")));

        assertThrows(BusinessOutboundException.class, () -> appointmentsService.updateAppointments(
                AppointmentsDto.builder().id(1).status("NAO_COMPARECEU").build()));
    }

    @Test
    @DisplayName("The client of an appointment cannot be changed.")
    void clientCannotChange() {
        when(appointmentsRepository.findById(1)).thenReturn(Optional.of(appointment("AGENDADO")));

        assertThrows(BusinessOutboundException.class, () -> appointmentsService.updateAppointments(
                AppointmentsDto.builder().id(1).idClient(6).build()));
        verify(updateAppointmentsPort, never()).update(any());
    }

    @Test
    @DisplayName("Updating an appointment that does not exist throws an error.")
    void updateNotFound() {
        when(appointmentsRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(BusinessOutboundException.class, () -> appointmentsService.updateAppointments(
                AppointmentsDto.builder().id(99).status("CONFIRMADO").build()));
        verify(updateAppointmentsPort, never()).update(any());
    }

    @Test
    @DisplayName("Deleting an appointment that does not exist throws an error.")
    void deleteNotFound() {
        when(appointmentsRepository.existsById(99)).thenReturn(false);

        assertThrows(BusinessOutboundException.class, () -> appointmentsService.deleteAppointments(99));
        verify(deleteAppointmentsPort, never()).delete(any());
    }

    @Test
    @DisplayName("An appointment linked to a payment is not deleted.")
    void deleteWithPayments() {
        when(appointmentsRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("foreign key")).when(deleteAppointmentsPort).delete(1);

        assertThrows(BusinessOutboundException.class, () -> appointmentsService.deleteAppointments(1));
    }

    @Test
    @DisplayName("Searching by status ignores upper/lower case and rejects unknown status.")
    void searchStatus() {
        when(searchStatusAppointmentsPort.searchStatus("CONFIRMADO")).thenReturn(List.of(appointment("CONFIRMADO")));

        assertEquals(1, appointmentsService.searchStatus("confirmado").size());
        assertThrows(BusinessOutboundException.class, () -> appointmentsService.searchStatus("PAGO"));
    }

    @Test
    @DisplayName("Searching by date looks at the whole day in Brasilia time and returns the items.")
    void searchDate() {
        AppointmentsItems item = AppointmentsItems.builder().id(10).appointment_id(1).service_id(2).professional_id(3)
                .start_at(OffsetDateTime.parse("2026-11-20T14:00:00-03:00"))
                .end_at(OffsetDateTime.parse("2026-11-20T14:45:00-03:00"))
                .price(new BigDecimal("80.00")).build();
        Appointments withItem = appointment("AGENDADO");
        withItem.setItems(List.of(item));
        when(searchDateAppointmentsPort.searchDate(any(), any())).thenReturn(List.of(withItem));

        List<AppointmentsDto> result = appointmentsService.searchDate(LocalDate.of(2026, 11, 20));

        ArgumentCaptor<OffsetDateTime> start = ArgumentCaptor.forClass(OffsetDateTime.class);
        ArgumentCaptor<OffsetDateTime> end = ArgumentCaptor.forClass(OffsetDateTime.class);
        verify(searchDateAppointmentsPort).searchDate(start.capture(), end.capture());
        assertEquals(OffsetDateTime.parse("2026-11-20T00:00:00-03:00"), start.getValue());
        assertEquals(OffsetDateTime.parse("2026-11-21T00:00:00-03:00"), end.getValue());

        assertEquals(1, result.get(0).getItems().size());
        assertEquals(3, result.get(0).getItems().get(0).getProfessional_id());
    }

    @Test
    @DisplayName("Searching the appointments of a client that has none throws a not found error.")
    void searchClientNotFound() {
        when(searchClientAppointmentsPort.searchClient(5)).thenReturn(List.of());

        assertThrows(BusinessOutboundException.class, () -> appointmentsService.searchClient(5));
    }
}
