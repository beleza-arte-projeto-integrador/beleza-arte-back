package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Clients;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ClientsRepository;
import com.belezaearte.salaobeleza.application.domain.dto.ClientsDto;
import com.belezaearte.salaobeleza.application.port.out.clientsPort.CreateClientsPort;
import com.belezaearte.salaobeleza.application.port.out.clientsPort.SearchNameClientsPort;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientsServiceTest {

    @InjectMocks
    private ClientsService clientsService;

    @Mock
    private CreateClientsPort createClientsPort;

    @Mock
    private SearchNameClientsPort searchNameClientsPort;

    @Mock
    private ClientsRepository clientsRepository;

    @Test
    @DisplayName("A new client is created and returned with its id.")
    void createClient() {
        when(clientsRepository.existsByPhone("11999999999")).thenReturn(false);
        when(createClientsPort.create(any())).thenAnswer(invocation -> {
            Clients c = invocation.getArgument(0);
            c.setId(1);
            return Optional.of(c);
        });

        ClientsDto result = clientsService.createClients(
                ClientsDto.builder().name("Ana").phone("11999999999").email("ana@email.com").build());

        assertEquals(1, result.getId());
        assertEquals("Ana", result.getName());
    }

    @Test
    @DisplayName("A client with a phone that is already registered is not created.")
    void createClientDuplicatePhone() {
        when(clientsRepository.existsByPhone("11999999999")).thenReturn(true);

        assertThrows(BusinessOutboundException.class, () -> clientsService.createClients(
                ClientsDto.builder().name("Ana").phone("11999999999").build()));
        verify(createClientsPort, never()).create(any());
    }

    @Test
    @DisplayName("A client without a name is not created.")
    void createClientWithoutName() {
        assertThrows(BusinessOutboundException.class, () -> clientsService.createClients(
                ClientsDto.builder().phone("11999999999").build()));
    }

    @Test
    @DisplayName("Searching a name that does not exist throws a not found error.")
    void searchNameNotFound() {
        when(searchNameClientsPort.searchName("Ninguém")).thenReturn(List.of());

        assertThrows(BusinessOutboundException.class, () -> clientsService.searchName("Ninguém"));
    }
}
