package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Clients;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ClientsRepository;
import com.belezaearte.salaobeleza.application.domain.dto.ClientsDto;
import com.belezaearte.salaobeleza.application.port.in.Clients.*;
import com.belezaearte.salaobeleza.application.port.out.clientsPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClientsService implements CreateClientsUseCase, UpdateClientsUseCase, DeleteClientsUseCase, SearchNameClientsUseCase, SearchPhoneClientsUseCase, SearchEmailClientsUseCase, SearchCreatedClientsUseCase {

    private static final String NOT_FOUND = "Could not find client!, The requested client was not found in the system!";

    @Autowired
    private CreateClientsPort createClientsPort;
    @Autowired
    private UpdateClientsPort updateClientsPort;
    @Autowired
    private DeleteClientsPort deleteClientsPort;
    @Autowired
    private SearchNameClientsPort searchNameClientsPort;
    @Autowired
    private SearchPhoneClientsPort searchPhoneClientsPort;
    @Autowired
    private SearchEmailClientsPort searchEmailClientsPort;
    @Autowired
    private SearchCreatedClientsPort searchCreatedClientsPort;
    @Autowired
    private ClientsRepository clientsRepository;

    @Override
    public ClientsDto createClients(ClientsDto clientsDto) {
        validateRequiredFields(clientsDto);

        // O telefone é único: não deixa cadastrar o mesmo cliente duas vezes.
        if (clientsRepository.existsByPhone(clientsDto.getPhone())) {
            throw new BusinessOutboundException("Could not create client, A client with this phone is already registered");
        }

        Clients clientsEntity = toEntity(clientsDto);
        clientsEntity.setId(null); // o id é gerado pelo banco
        clientsEntity.setCreated(null); // a data de cadastro é gerada automaticamente

        try {
            Optional<Clients> clientsSaved = createClientsPort.create(clientsEntity);
            return toDto(clientsSaved.get());
        } catch (Exception e) {
            throw new BusinessOutboundException("Could not create client, Check if the client is already registered");
        }
    }

    @Override
    public ClientsDto updateClients(ClientsDto clientsDto) {
        if (clientsDto.getId() == null) {
            throw new BusinessOutboundException("Could not update client, The client id is required");
        }
        validateRequiredFields(clientsDto);

        Optional<Clients> existing = clientsRepository.findById(clientsDto.getId());
        if (existing.isEmpty()) {
            throw new BusinessOutboundException("Could not update client, Check if the client exists");
        }

        if (clientsRepository.existsByPhoneAndIdNot(clientsDto.getPhone(), clientsDto.getId())) {
            throw new BusinessOutboundException("Could not update client, Another client is already registered with this phone");
        }

        Clients clientsEntity = toEntity(clientsDto);
        // Mantém a data de cadastro original, mesmo que o JSON não envie esse campo.
        clientsEntity.setCreated(existing.get().getCreated());

        Optional<Clients> clientUpdated = updateClientsPort.update(clientsEntity);
        if (clientUpdated.isEmpty()) {
            throw new BusinessOutboundException("Could not update client, Check if the client exists");
        }
        return toDto(clientUpdated.get());
    }

    @Override
    public void deleteClients(Integer id) {
        if (id == null || !clientsRepository.existsById(id)) {
            throw new BusinessOutboundException("Could not delete client, Check if the client exists");
        }
        try {
            deleteClientsPort.delete(id);
        } catch (Exception e) {
            // Acontece quando o cliente já tem agendamentos ligados a ele.
            throw new BusinessOutboundException("Could not delete client, Check if the client has appointments");
        }
    }

    @Override
    public List<ClientsDto> searchName(String name) {
        return toDtoListOrNotFound(searchNameClientsPort.searchName(name));
    }

    @Override
    public List<ClientsDto> searchPhone(String phone) {
        return toDtoListOrNotFound(searchPhoneClientsPort.searchPhone(phone));
    }

    @Override
    public List<ClientsDto> searchEmail(String email) {
        return toDtoListOrNotFound(searchEmailClientsPort.searchEmail(email));
    }

    @Override
    public List<ClientsDto> searchCreatedClients(LocalDateTime created) {
        return toDtoListOrNotFound(searchCreatedClientsPort.searchCreatedClients(created));
    }

    // ---------------------------------------------------------------
    // Métodos auxiliares
    // ---------------------------------------------------------------

    private void validateRequiredFields(ClientsDto clientsDto) {
        if (clientsDto.getName() == null || clientsDto.getName().isBlank()) {
            throw new BusinessOutboundException("Invalid client, The name is required");
        }
        if (clientsDto.getPhone() == null || clientsDto.getPhone().isBlank()) {
            throw new BusinessOutboundException("Invalid client, The phone is required");
        }
    }

    private List<ClientsDto> toDtoListOrNotFound(List<Clients> clients) {
        if (clients.isEmpty()) {
            throw new BusinessOutboundException(NOT_FOUND);
        }
        return clients.stream().map(this::toDto).toList();
    }

    private Clients toEntity(ClientsDto dto) {
        return Clients.builder()
                .id(dto.getId())
                .name(dto.getName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .created(dto.getCreated())
                .build();
    }

    private ClientsDto toDto(Clients client) {
        return ClientsDto.builder()
                .id(client.getId())
                .name(client.getName())
                .phone(client.getPhone())
                .email(client.getEmail())
                .created(client.getCreated())
                .build();
    }
}
