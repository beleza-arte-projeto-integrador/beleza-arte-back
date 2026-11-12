package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.AppointmentsItemsRepository;
import com.belezaearte.salaobeleza.application.port.out.AppointmentsItemsPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AppointmentsItemsAdapter implements DeleteAppointmentsItemsPort,UpdateAppointmentsItemsPort,CreateAppointmentsItemsPort,SearchPricePort,SearchStartDatePort {
    private final AppointmentsItemsRepository appointmentsItemsRepository;

    @Override
    @Transactional
    public void delete(Integer id) {
        if (appointmentsItemsRepository.existsById(id)) {
            appointmentsItemsRepository.existsById(id);
        } else {
            throw new BusinessOutboundException("Erro: Id errado");
        }
    }

    @Override
    public AppointmentsItems update(AppointmentsItems appointmentsItems) {
        Optional<AppointmentsItems> appointmentsItemsOptional = appointmentsItemsRepository.findById(appointmentsItems.getId());

        if (appointmentsItemsOptional.isPresent()) {
            AppointmentsItems updateAppointmentsItems = appointmentsItemsOptional.get();

            updateAppointmentsItems.setStart_at(updateAppointmentsItems.getStart_at());
            updateAppointmentsItems.setEnd_at(updateAppointmentsItems.getStart_at());
            updateAppointmentsItems.setPrice(updateAppointmentsItems.getPrice());

            return appointmentsItemsRepository.saveAndFlush(updateAppointmentsItems);
        }
            return null;
    }
    @Override
    public AppointmentsItems create (AppointmentsItems appointmentsItems){
        return appointmentsItemsRepository.saveAndFlush(appointmentsItems);
        }
    @Override
    public List<AppointmentsItems> searchPrice (Integer price){
        AppointmentsItems appointmentsItems = AppointmentsItems.builder()
                .price(price)
                .build();
        Example<AppointmentsItems> appointmentsItemsExample = Example.of(appointmentsItems);
        return appointmentsItemsRepository.findAll(appointmentsItemsExample);
    }
    @Override
    public List<AppointmentsItems> SearchStartDate (OffsetDateTime start_at){
        AppointmentsItems appointmentsItems = AppointmentsItems.builder()
                .start_at(start_at)
                .build();
        Example<AppointmentsItems> appointmentsItemsExample = Example.of(appointmentsItems);
        return appointmentsItemsRepository.findAll(appointmentsItemsExample);
    }
}