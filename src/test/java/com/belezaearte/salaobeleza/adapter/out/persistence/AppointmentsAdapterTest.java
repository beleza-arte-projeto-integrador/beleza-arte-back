package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.AppointmentsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentsAdapterTest {

    @InjectMocks
    private AppointmentsAdapter appointmentsAdapter;

    @Mock
    private AppointmentsRepository appointmentsRepository;

    @Test
    @DisplayName("The appointment ID must be deleted.")
    void deleteId() {
        appointmentsAdapter.delete(1);
        verify(appointmentsRepository, times(1)).deleteById(1);
        verify(appointmentsRepository, times(1)).flush();
    }

    @Test
    @DisplayName("Updating an appointment that does not exist returns empty and saves nothing.")
    void updateNotFound() {
        when(appointmentsRepository.existsById(99)).thenReturn(false);

        Optional<Appointments> result = appointmentsAdapter.update(Appointments.builder().id(99).clientId(1).status("AGENDADO").build());

        assertTrue(result.isEmpty());
        verify(appointmentsRepository, never()).saveAndFlush(any());
    }
}
