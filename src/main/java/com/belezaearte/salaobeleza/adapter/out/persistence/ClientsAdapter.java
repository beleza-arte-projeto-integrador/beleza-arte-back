package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ClientsRepository;
import com.belezaearte.salaobeleza.application.port.out.clientsPort.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ClientsAdapter implements CreateClientsPort, UpdateClientsPort, DeleteClientsPort, SearchNameClientsPort, SearchPhoneClientsPort, SearchEmailClientsPort, SearchCreatedClientsPort {

    private final ClientsRepository clientsRepository;

    // Busca por "pedaço" do texto, sem diferenciar maiúsculas de minúsculas.
    // Ex: buscar "ana" encontra "Ana Paula" e "Mariana".
    private static final ExampleMatcher CONTAINS_IGNORE_CASE = ExampleMatcher.matching()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreCase();

    @Override
    public Optional<Clients> create(Clients clients) {
        return Optional.of(clientsRepository.saveAndFlush(clients));
    }

    @Override
    public Optional<Clients> update(Clients clients) {
        if (clients.getId() == null || !clientsRepository.existsById(clients.getId())) {
            return Optional.empty();
        }
        return Optional.of(clientsRepository.saveAndFlush(clients));
    }

    @Override
    public void delete(Integer id) {
        clientsRepository.deleteById(id);
    }

    @Override
    public List<Clients> searchName(String name) {
        Clients clients = Clients.builder().name(name).build();
        return clientsRepository.findAll(Example.of(clients, CONTAINS_IGNORE_CASE));
    }

    @Override
    public List<Clients> searchPhone(String phone) {
        Clients clients = Clients.builder().phone(phone).build();
        return clientsRepository.findAll(Example.of(clients));
    }

    @Override
    public List<Clients> searchEmail(String email) {
        Clients clients = Clients.builder().email(email).build();
        return clientsRepository.findAll(Example.of(clients, ExampleMatcher.matching().withIgnoreCase()));
    }

    @Override
    public List<Clients> searchCreatedClients(LocalDateTime created) {
        Clients clients = Clients.builder().created(created).build();
        return clientsRepository.findAll(Example.of(clients));
    }
}
