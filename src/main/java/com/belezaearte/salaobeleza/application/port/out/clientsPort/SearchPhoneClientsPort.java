package com.belezaearte.salaobeleza.application.port.out.clientsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Clients;

import java.util.List;

public interface SearchPhoneClientsPort {
    List<Clients> searchPhone (String phone);
}
