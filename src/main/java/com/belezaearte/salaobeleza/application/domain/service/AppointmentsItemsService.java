package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.AppointmentsItems;
import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsItemsDto;
import com.belezaearte.salaobeleza.application.port.in.AppointmentsItems.*;
import com.belezaearte.salaobeleza.application.port.out.AppointmentsItemsPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AppointmentsItemsService implements SearchEndDateUseCase, SearchStartDateUseCase, SearchPriceUseCase, CreateAppointmentsItemsUseCase, DeleteAppointmentsItemsUseCase, UpdateAppointmentsItemsUseCase {

    @Autowired
    private SearchEndDatePort searchEndDatePort;

    public List<AppointmentsItemsDto> searchEndDate (OffsetDateTime end_at){
    List<AppointmentsItems> appointmentsItemsList = searchEndDatePort.searchEndDate(end_at);

    return appointmentsItemsList.stream()
            .map(appointmentsItems ->AppointmentsItemsDto.builder()
                    .id(appointmentsItems.getId())
                    .appointment_id(appointmentsItems.getAppointment_id())
                    .service_id(appointmentsItems.getService_id())
                    .professional_id(appointmentsItems.getProfessional_id())
                    .resource_id(appointmentsItems.getResource_id())
                    .start_at(appointmentsItems.getStart_at())
                    .end_at(appointmentsItems.getEnd_at())
                    .price(appointmentsItems.getPrice())
                    .build()).toList();
    }
    @Autowired
    private SearchStartDatePort searchStartDatePort;

    public  List<AppointmentsItemsDto> SearchStartDate (OffsetDateTime start_at){
        List<AppointmentsItems> appointmentsItemsList = searchStartDatePort.SearchStartDate(start_at);

        return appointmentsItemsList.stream()
                .map(appointmentsItems -> AppointmentsItemsDto
                        .builder()
                        .id(appointmentsItems.getId())
                        .appointment_id(appointmentsItems.getAppointment_id())
                        .service_id(appointmentsItems.getService_id())
                        .professional_id(appointmentsItems.getProfessional_id())
                        .resource_id(appointmentsItems.getResource_id())
                        .start_at(appointmentsItems.getStart_at())
                        .end_at(appointmentsItems.getEnd_at())
                        .price(appointmentsItems.getPrice())
                        .build()).toList();
    }
    @Autowired
    private SearchPricePort searchPricePort;

    public List<AppointmentsItemsDto> searchPrice (BigDecimal price){
        List<AppointmentsItems> appointmentsItemsList =searchPricePort.searchPrice(price);

        return appointmentsItemsList.stream()
                .map(appointmentsItems -> AppointmentsItemsDto.builder()
                        .id(appointmentsItems.getId())
                        .appointment_id(appointmentsItems.getAppointment_id())
                        .service_id(appointmentsItems.getService_id())
                        .professional_id(appointmentsItems.getProfessional_id())
                        .resource_id(appointmentsItems.getResource_id())
                        .start_at(appointmentsItems.getStart_at())
                        .end_at(appointmentsItems.getEnd_at())
                        .price(appointmentsItems.getPrice())
                        .build()).toList();
    }
    @Autowired
    private CreateAppointmentsItemsPort createAppointmentsItemsPort;

   public AppointmentsItemsDto create (AppointmentsItemsDto appointmentsItemsDto){
        if(appointmentsItemsDto.getAppointment_id() == null || appointmentsItemsDto.getProfessional_id() == null || appointmentsItemsDto.getService_id()== null || appointmentsItemsDto.getResource_id() == null ||appointmentsItemsDto.getEnd_at() == null ||appointmentsItemsDto.getPrice() == null ||appointmentsItemsDto.getStart_at() == null){
            throw new BusinessOutboundException("Erro: Campo não preenchido");
        }
        AppointmentsItems appointmentsItemsEntity = AppointmentsItems.builder()
                .id(appointmentsItemsDto.getId())
                .appointment_id(appointmentsItemsDto.getAppointment_id())
                .service_id(appointmentsItemsDto.getService_id())
                .professional_id(appointmentsItemsDto.getProfessional_id())
                .resource_id(appointmentsItemsDto.getResource_id())
                .start_at(appointmentsItemsDto.getStart_at())
                .end_at(appointmentsItemsDto.getEnd_at())
                .price(appointmentsItemsDto.getPrice())
                .build();
        AppointmentsItems appointmentsItems1 = createAppointmentsItemsPort.create(appointmentsItemsEntity);

        return AppointmentsItemsDto.builder()
                .id(appointmentsItems1.getId())
                .appointment_id(appointmentsItems1.getAppointment_id())
                .service_id(appointmentsItems1.getService_id())
                .professional_id(appointmentsItems1.getProfessional_id())
                .resource_id(appointmentsItems1.getResource_id())
                .start_at(appointmentsItems1.getStart_at())
                .end_at(appointmentsItems1.getEnd_at())
                .price(appointmentsItems1.getPrice()).build();
   }
   @Autowired
    private DeleteAppointmentsItemsPort deleteAppointmentsItemsPort;
    public void delete (Integer id){
        deleteAppointmentsItemsPort.delete(id);
    }

    @Autowired
    private UpdateAppointmentsItemsPort updateAppointmentsItemsPort;

    public AppointmentsItemsDto update (AppointmentsItemsDto appointmentsItemsDto) {
        AppointmentsItems appointmentsItemsEntity = AppointmentsItems.builder()
                .id(appointmentsItemsDto.getId())
                .appointment_id(appointmentsItemsDto.getAppointment_id())
                .service_id(appointmentsItemsDto.getService_id())
                .professional_id(appointmentsItemsDto.getProfessional_id())
                .resource_id(appointmentsItemsDto.getResource_id())
                .start_at(appointmentsItemsDto.getStart_at())
                .end_at(appointmentsItemsDto.getEnd_at())
                .price(appointmentsItemsDto.getPrice()).build();

        AppointmentsItems appointmentsItems1 = updateAppointmentsItemsPort.update(appointmentsItemsEntity);

        if(appointmentsItems1 == null){
            throw new BusinessOutboundException("Erro: Verifique se todos os itens foram preenchidos corretamente");
        } return AppointmentsItemsDto.builder()
                .id(appointmentsItemsDto.getId())
                .appointment_id(appointmentsItemsDto.getAppointment_id())
                .service_id(appointmentsItemsDto.getService_id())
                .professional_id(appointmentsItemsDto.getProfessional_id())
                .resource_id(appointmentsItemsDto.getResource_id())
                .start_at(appointmentsItemsDto.getStart_at())
                .end_at(appointmentsItemsDto.getEnd_at())
                .price(appointmentsItemsDto.getPrice()).build();
    }

}
