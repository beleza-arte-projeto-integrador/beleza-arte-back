package com.belezaearte.salaobeleza.application.port.in.Clients;

import com.belezaearte.salaobeleza.application.domain.dto.ClientsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchCreatedClientsUseCase {
    List<ClientsDto> searchCreatedClients (LocalDateTime created);

}
