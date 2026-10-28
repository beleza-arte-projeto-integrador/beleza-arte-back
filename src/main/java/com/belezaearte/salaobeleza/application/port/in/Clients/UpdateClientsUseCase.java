package com.belezaearte.salaobeleza.application.port.in.Clients;

import com.belezaearte.salaobeleza.application.domain.dto.ClientsDto;

public interface UpdateClientsUseCase {
    ClientsDto updateClients (ClientsDto clientsDto);
}
