package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.AppointmentsItemsRepository;
import com.belezaearte.salaobeleza.application.port.out.AppointmentsItemsPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AppointmentsItemsAdapter implements DeleteAppointmentsItemsPort,UpdateAppointmentsItemsPort,CreateAppointmentsItemsPort,SearchPricePort,SearchStartDatePort,SearchEndDatePort {
    private final AppointmentsItemsRepository appointmentsItemsRepository;

    @Override
    @Transactional
    public void delete(Integer id) {
        if (appointmentsItemsRepository.existsById(id)) {
            appointmentsItemsRepository.deleteById(id);
        } else {
            throw new BusinessOutboundException("Erro: Id errado");
        }
    }

    @Override
    public AppointmentsItems update(AppointmentsItems appointmentsItems) {
        Optional<AppointmentsItems> appointmentsItemsOptional = appointmentsItemsRepository.findById(appointmentsItems.getId());

        if (appointmentsItemsOptional.isPresent()) {
            AppointmentsItems updateAppointmentsItems = appointmentsItemsOptional.get();

            updateAppointmentsItems.setResource_id(appointmentsItems.getResource_id());
            updateAppointmentsItems.setProfessional_id(appointmentsItems.getProfessional_id());
            updateAppointmentsItems.setService_id(appointmentsItems.getService_id());
            updateAppointmentsItems.setAppointment_id(appointmentsItems.getAppointment_id());
            updateAppointmentsItems.setId(appointmentsItems.getId());

            updateAppointmentsItems.setStart_at(appointmentsItems.getStart_at());
            updateAppointmentsItems.setEnd_at(appointmentsItems.getEnd_at());
            updateAppointmentsItems.setPrice(appointmentsItems.getPrice());

            return appointmentsItemsRepository.saveAndFlush(updateAppointmentsItems);
        }
            return null;
    }
    @Override
    public AppointmentsItems create (AppointmentsItems appointmentsItems){
        return appointmentsItemsRepository.saveAndFlush(appointmentsItems);
        }
    @Override
    public List<AppointmentsItems> searchPrice (BigDecimal price){
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
    @Override
    public List<AppointmentsItems> searchEndDate (OffsetDateTime end_at){
        AppointmentsItems appointmentsItems = AppointmentsItems.builder()
                .end_at(end_at)
                .build();
        Example<AppointmentsItems> appointmentsItemsExample = Example.of(appointmentsItems);
        return appointmentsItemsRepository.findAll(appointmentsItemsExample);
    }
}