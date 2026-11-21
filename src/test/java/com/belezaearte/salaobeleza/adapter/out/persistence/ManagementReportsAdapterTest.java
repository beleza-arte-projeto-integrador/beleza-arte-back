package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ProfessionalsRepository;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ServicesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Testes do adapter dos relatorios gerenciais (Matheus).
@ExtendWith(MockitoExtension.class)
public class ManagementReportsAdapterTest {

    @InjectMocks
    private ManagementReportsAdapter managementReportsAdapter;

    @Mock
    private ServicesRepository servicesRepository;

    @Mock
    private ProfessionalsRepository professionalsRepository;

    @Test
    @DisplayName("All services are searched to show their names in the report.")
    void searchAllServices() {
        List<Services> services = List.of(Services.builder().id(10).name("Corte").build());
        when(servicesRepository.findAll()).thenReturn(services);

        assertEquals(services, managementReportsAdapter.searchAllServices());
        verify(servicesRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("All professionals are searched to show their names in the report.")
    void searchAllProfessionals() {
        List<Professionals> professionals = List.of(Professionals.builder().id(1).name("Anna").build());
        when(professionalsRepository.findAll()).thenReturn(professionals);

        assertEquals(professionals, managementReportsAdapter.searchAllProfessionals());
        verify(professionalsRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Without data, the adapter returns empty lists.")
    void searchEmpty() {
        when(servicesRepository.findAll()).thenReturn(List.of());
        when(professionalsRepository.findAll()).thenReturn(List.of());

        assertTrue(managementReportsAdapter.searchAllServices().isEmpty());
        assertTrue(managementReportsAdapter.searchAllProfessionals().isEmpty());
    }
}
