package com.belezaearte.salaobeleza.application.port.in.Clients;

import com.belezaearte.salaobeleza.application.domain.dto.ClientsDto;

import java.util.List;

public interface SearchPhoneClientsUseCase {
    List<ClientsDto> searchPhone (String phone);

}
