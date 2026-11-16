package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Clients;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ClientsRepository;
import com.belezaearte.salaobeleza.application.domain.dto.ClientsDataExportDto;
import com.belezaearte.salaobeleza.application.domain.dto.ClientsDto;
import com.belezaearte.salaobeleza.application.port.out.clientsPort.CreateClientsPort;
import com.belezaearte.salaobeleza.application.port.out.clientsPort.SearchNameClientsPort;
import com.belezaearte.salaobeleza.application.port.out.clientsPort.UpdateClientsPort;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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
    private UpdateClientsPort updateClientsPort;

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

    @Test
    @DisplayName("Exporting a client's data returns the client, the purpose of the data and when it was generated.")
    void exportClientData() {
        Clients client = Clients.builder().id(1).name("Ana").phone("11999999999").email("ana@email.com").build();
        when(clientsRepository.findById(1)).thenReturn(Optional.of(client));

        ClientsDataExportDto result = clientsService.exportClientsData(1);

        assertEquals("Ana", result.getClient().getName());
        assertEquals("11999999999", result.getClient().getPhone());
        assertNotNull(result.getPurpose());
        assertNotNull(result.getGeneratedAt());
    }

    @Test
    @DisplayName("Exporting the data of a client that does not exist throws an error.")
    void exportClientDataNotFound() {
        when(clientsRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(BusinessOutboundException.class, () -> clientsService.exportClientsData(99));
    }

    @Test
    @DisplayName("Anonymizing a client replaces name, phone and email and keeps id and registration date.")
    void anonymizeClient() {
        LocalDateTime created = LocalDateTime.of(2026, 11, 1, 10, 0);
        Clients client = Clients.builder().id(7).name("Ana").phone("11999999999").email("ana@email.com").created(created).build();
        when(clientsRepository.findById(7)).thenReturn(Optional.of(client));
        when(updateClientsPort.update(any())).thenAnswer(invocation -> Optional.of(invocation.getArgument(0)));

        ClientsDto result = clientsService.anonymizeClients(7);

        assertEquals(7, result.getId());
        assertEquals("Cliente anonimizado", result.getName());
        assertEquals("ANON-7", result.getPhone());
        assertNull(result.getEmail());
        assertEquals(created, result.getCreated());
    }

    @Test
    @DisplayName("Anonymizing a client that does not exist throws an error and saves nothing.")
    void anonymizeClientNotFound() {
        when(clientsRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(BusinessOutboundException.class, () -> clientsService.anonymizeClients(99));
        verify(updateClientsPort, never()).update(any());
    }
}
