package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ProfessionalsRepository;
import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalsDto;
import com.belezaearte.salaobeleza.application.port.out.professionalsPort.CreateProfessionalsPort;
import com.belezaearte.salaobeleza.application.port.out.professionalsPort.UpdateProfessionalsPort;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfessionalsServiceTest {

    @InjectMocks
    private ProfessionalsService professionalsService;

    @Mock
    private CreateProfessionalsPort createProfessionalsPort;

    @Mock
    private UpdateProfessionalsPort updateProfessionalsPort;

    @Mock
    private ProfessionalsRepository professionalsRepository;

    @Test
    @DisplayName("A new professional is saved with the CPF digits only, active, and the password is not returned.")
    void createProfessional() {
        when(createProfessionalsPort.create(any())).thenAnswer(invocation -> {
            Professionals p = invocation.getArgument(0);
            p.setId(1);
            return Optional.of(p);
        });

        ProfessionalsDto result = professionalsService.createProfessionals(ProfessionalsDto.builder()
                .name("Anna Souza").cpf("529.982.247-25").username("anna").password("Senha123").experienceYears(5)
                .build());

        ArgumentCaptor<Professionals> saved = ArgumentCaptor.forClass(Professionals.class);
        verify(createProfessionalsPort).create(saved.capture());
        assertEquals("52998224725", saved.getValue().getCpf());
        assertEquals(true, saved.getValue().getActive());
        assertEquals("Senha123", saved.getValue().getPassword());

        assertEquals(1, result.getId());
        assertEquals(5, result.getExperienceYears());
        assertNull(result.getPassword());
    }

    @Test
    @DisplayName("A professional with a username that is already in use is not created.")
    void createProfessionalDuplicateUsername() {
        when(professionalsRepository.existsByUsername("anna")).thenReturn(true);

        assertThrows(BusinessOutboundException.class, () -> professionalsService.createProfessionals(
                ProfessionalsDto.builder().name("Anna Souza").username("anna").build()));
        verify(createProfessionalsPort, never()).create(any());
    }

    @Test
    @DisplayName("A professional with an incomplete CPF is not created.")
    void createProfessionalInvalidCpf() {
        assertThrows(BusinessOutboundException.class, () -> professionalsService.createProfessionals(
                ProfessionalsDto.builder().name("Anna Souza").cpf("529.982").build()));
        verify(createProfessionalsPort, never()).create(any());
    }

    @Test
    @DisplayName("Updating without a password keeps the current password, status and registration date.")
    void updateKeepsPasswordWhenEmpty() {
        LocalDateTime created = LocalDateTime.of(2026, 11, 1, 10, 0);
        Professionals existing = Professionals.builder()
                .id(1).name("Anna").password("SenhaAntiga1").active(false).created(created).build();
        when(professionalsRepository.findById(1)).thenReturn(Optional.of(existing));
        when(updateProfessionalsPort.update(any())).thenAnswer(invocation -> Optional.of(invocation.getArgument(0)));

        ProfessionalsDto result = professionalsService.updateProfessionals(
                ProfessionalsDto.builder().id(1).name("Anna Souza").password("").build());

        ArgumentCaptor<Professionals> saved = ArgumentCaptor.forClass(Professionals.class);
        verify(updateProfessionalsPort).update(saved.capture());
        assertEquals("SenhaAntiga1", saved.getValue().getPassword());
        assertEquals(false, saved.getValue().getActive());
        assertEquals(created, result.getCreated());
        assertEquals("Anna Souza", result.getName());
    }

    @Test
    @DisplayName("Updating a professional that does not exist throws an error.")
    void updateNotFound() {
        when(professionalsRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(BusinessOutboundException.class, () -> professionalsService.updateProfessionals(
                ProfessionalsDto.builder().id(99).name("Ninguém").build()));
        verify(updateProfessionalsPort, never()).update(any());
    }
}
