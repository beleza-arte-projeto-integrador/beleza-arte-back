package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.AppointmentsRepository;
import com.belezaearte.salaobeleza.application.port.out.appointmentsPort.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AppointmentsAdapter implements CreateAppointmentsPort, UpdateAppointmentsPort, DeleteAppointmentsPort, SearchClientAppointmentsPort, SearchProfessionalAppointmentsPort, SearchServiceAppointmentsPort, SearchStatusAppointmentsPort, SearchNotesAppointmentsPort, SearchPriceAppointmentsPort, SearchDateAppointmentsPort, SearchTimeAppointmentsPort, SearchCreatedAppointmentsPort {

    private final AppointmentsRepository appointmentsRepository;

    @Override
    public Optional<Appointments> create(Appointments appointments) {
        return Optional.of(appointmentsRepository.saveAndFlush(appointments));
    }

    @Override
    public Optional<Appointments> update(Appointments appointments) {
        if (appointments.getId() == null || !appointmentsRepository.existsById(appointments.getId())) {
            return Optional.empty();
        }
        return Optional.of(appointmentsRepository.saveAndFlush(appointments));
    }

    @Override
    public void delete(Integer id) {
        appointmentsRepository.deleteById(id);
        // Envia o delete ao banco agora, para um erro (ex: pagamento ligado) aparecer aqui.
        appointmentsRepository.flush();
    }

    @Override
    public List<Appointments> searchClient(Integer idClient) {
        return appointmentsRepository.findByClientIdOrderByIdAsc(idClient);
    }

    @Override
    public List<Appointments> searchProfessional(Integer idProfessional) {
        return appointmentsRepository.findByProfessional(idProfessional);
    }

    @Override
    public List<Appointments> searchService(Integer idService) {
        return appointmentsRepository.findByService(idService);
    }

    @Override
    public List<Appointments> searchStatus(String status) {
        return appointmentsRepository.findByStatusOrderByIdAsc(status);
    }

    // Busca por "pedaço" do texto, sem diferenciar maiúsculas de minúsculas.
    @Override
    public List<Appointments> searchNotes(String notes) {
        return appointmentsRepository.findByNotesContainingIgnoreCaseOrderByIdAsc(notes);
    }

    @Override
    public List<Appointments> searchPrice(BigDecimal price) {
        return appointmentsRepository.findByItemPrice(price);
    }

    @Override
    public List<Appointments> searchDate(OffsetDateTime start, OffsetDateTime end) {
        return appointmentsRepository.findByItemStartBetween(start, end);
    }

    @Override
    public List<Appointments> searchTime(OffsetDateTime startAt) {
        return appointmentsRepository.findByItemStart(startAt);
    }

    @Override
    public List<Appointments> searchCreated(OffsetDateTime created) {
        return appointmentsRepository.findByCreatedOrderByIdAsc(created);
    }
}
