package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ClientsRepository;
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
public class ClientsAdapterTest {

    @InjectMocks
    private ClientsAdapter clientsAdapter;

    @Mock
    private ClientsRepository clientsRepository;

    @Test
    @DisplayName("The client ID must be deleted.")
    void deleteId() {
        clientsAdapter.delete(1);
        verify(clientsRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Updating a client that does not exist returns empty and saves nothing.")
    void updateNotFound() {
        when(clientsRepository.existsById(99)).thenReturn(false);

        Optional<Clients> result = clientsAdapter.update(Clients.builder().id(99).name("Ana").phone("11999999999").build());

        assertTrue(result.isEmpty());
        verify(clientsRepository, never()).saveAndFlush(any());
    }
}
