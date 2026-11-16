package com.belezaearte.salaobeleza.application.port.in.Clients;

import com.belezaearte.salaobeleza.application.domain.dto.ClientsDto;

// LGPD (art. 18, IV e VI): o cliente pode pedir a eliminação dos dados pessoais.
public interface AnonymizeClientsUseCase {
    ClientsDto anonymizeClients (Integer id);
}
