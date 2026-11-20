package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ProfessionalAvailabilityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Testes do adapter da disponibilidade (Matheus).
@ExtendWith(MockitoExtension.class)
public class ProfessionalAvailabilityAdapterTest {

    @InjectMocks
    private ProfessionalAvailabilityAdapter professionalAvailabilityAdapter;

    @Mock
    private ProfessionalAvailabilityRepository professionalAvailabilityRepository;

    private ProfessionalAvailability range(Integer id) {
        return ProfessionalAvailability.builder()
                .id(id).professionalId(1).weekday((short) 1)
                .startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(12, 0))
                .build();
    }

    @Test
    @DisplayName("The availability ID must be deleted.")
    void deleteId() {
        professionalAvailabilityAdapter.delete(1);
        verify(professionalAvailabilityRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Updating an availability that does not exist returns empty and saves nothing.")
    void updateNotFound() {
        when(professionalAvailabilityRepository.existsById(5)).thenReturn(false);

        assertTrue(professionalAvailabilityAdapter.update(range(5)).isEmpty());
        verify(professionalAvailabilityRepository, never()).saveAndFlush(any());
    }

    @Test
    @DisplayName("Updating an existing availability saves it.")
    void updateFound() {
        ProfessionalAvailability availability = range(5);
        when(professionalAvailabilityRepository.existsById(5)).thenReturn(true);
        when(professionalAvailabilityRepository.saveAndFlush(availability)).thenReturn(availability);

        assertEquals(availability, professionalAvailabilityAdapter.update(availability).orElseThrow());
    }

    @Test
    @DisplayName("Searching by professional uses the ordered query.")
    void searchProfessional() {
        when(professionalAvailabilityRepository.findByProfessionalIdOrderByWeekdayAscStartTimeAsc(1)).thenReturn(List.of(range(1)));

        assertEquals(1, professionalAvailabilityAdapter.searchProfessional(1).size());
    }
}
