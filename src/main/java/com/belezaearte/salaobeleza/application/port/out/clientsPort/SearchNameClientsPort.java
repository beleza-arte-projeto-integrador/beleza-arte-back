package com.belezaearte.salaobeleza.application.port.out.clientsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Clients;

import java.util.List;

public interface SearchNameClientsPort {
    List<Clients> searchName (String name);
}
