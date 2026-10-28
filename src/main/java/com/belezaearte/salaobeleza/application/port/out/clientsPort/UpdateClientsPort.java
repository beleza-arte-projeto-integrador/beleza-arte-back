package com.belezaearte.salaobeleza.application.port.out.clientsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Clients;

import java.util.Optional;

public interface UpdateClientsPort {
    Optional<Clients> update (Clients clients);
}
