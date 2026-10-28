package com.belezaearte.salaobeleza.application.port.out.clientsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Clients;

import java.util.Optional;

public interface CreateClientsPort {
    Optional<Clients> create (Clients clients);
}
