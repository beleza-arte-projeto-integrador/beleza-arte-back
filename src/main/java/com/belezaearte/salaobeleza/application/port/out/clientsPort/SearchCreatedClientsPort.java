package com.belezaearte.salaobeleza.application.port.out.clientsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Clients;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchCreatedClientsPort {
    List<Clients> searchCreatedClients (LocalDateTime created);
}
