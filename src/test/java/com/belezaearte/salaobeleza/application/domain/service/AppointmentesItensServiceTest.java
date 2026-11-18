package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.AppointmentsItems;
import com.belezaearte.salaobeleza.application.domain.dto.AppointmentsItemsDto;
import com.belezaearte.salaobeleza.application.port.in.Payments.GeneratePaymentsUseCase;
import com.belezaearte.salaobeleza.application.port.out.AppointmentsItemsPort.*;
import com.belezaearte.salaobeleza.application.port.out.servicesPort.DeleteServicesPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AppointmentesItensServiceTest {
    @Mock
    DeleteServicesPort deleteServicesPort;

    @InjectMocks
    private AppointmentsItemsService appointmentesItensService;

    @Mock
    GeneratePaymentsUseCase generatePaymentsUseCase;

    @Mock
    SearchIdAppointmentsItemsPort searchIdAppointmentsItemsPort;

    @Mock
    DeleteAppointmentsItemsPort deleteAppointmentsItemsPort;
    @Test
    public void delete (){
        Integer id = 600;

        appointmentesItensService.delete(id);

        Mockito.verify(deleteAppointmentsItemsPort,Mockito.times(1)).delete(id);

    }

    @Test
    @DisplayName("Removing a service recalculates the payment of its appointment.")
    void deleteRecalculatesPayment() {
        Mockito.when(searchIdAppointmentsItemsPort.searchId(600))
                .thenReturn(Optional.of(AppointmentsItems.builder().id(600).appointment_id(32).build()));

        appointmentesItensService.delete(600);

        Mockito.verify(deleteAppointmentsItemsPort).delete(600);
        Mockito.verify(generatePaymentsUseCase).generatePayments(32);
    }

    @Test
    @DisplayName("Moving a service to another appointment recalculates both payments.")
    void updateToAnotherAppointmentRecalculatesBoth() {
        AppointmentsItems moved = AppointmentsItems.builder().id(17).appointment_id(40).price(BigDecimal.valueOf(80.00)).build();
        Mockito.when(searchIdAppointmentsItemsPort.searchId(17))
                .thenReturn(Optional.of(AppointmentsItems.builder().id(17).appointment_id(32).build()));
        Mockito.when(updateAppointmentsItemsPort.update(Mockito.any(AppointmentsItems.class))).thenReturn(moved);

        appointmentesItensService.update(AppointmentsItemsDto.builder().id(17).appointment_id(40).price(BigDecimal.valueOf(80.00)).build());

        Mockito.verify(generatePaymentsUseCase).generatePayments(40);
        Mockito.verify(generatePaymentsUseCase).generatePayments(32);
    }

    @Mock
    UpdateAppointmentsItemsPort updateAppointmentsItemsPort;

    @Test
    public void update(){

        AppointmentsItemsDto fakeBank = AppointmentsItemsDto.builder()
                .id(17)
                .appointment_id(32)
                .service_id(17)
                .professional_id(12)
                .resource_id(48)
                .start_at(OffsetDateTime.parse("2026-10-27T08:00:00.000000Z"))
                .end_at(OffsetDateTime.parse("2026-10-27T10:00:00.000000Z"))
                .price(BigDecimal.valueOf(80.00)).build();

        AppointmentsItems updating = AppointmentsItems.builder()
                .id(17)
                .appointment_id(32)
                .service_id(17)
                .professional_id(12)
                .resource_id(48)
                .start_at(OffsetDateTime.parse("2026-10-27T08:00:00.000000Z"))
                .end_at(OffsetDateTime.parse("2026-10-27T10:00:00.000000Z"))
                .price(BigDecimal.valueOf(80.00)).build();

        Mockito.when(updateAppointmentsItemsPort.update(Mockito.any(AppointmentsItems.class)))
                .thenReturn(updating);

        AppointmentsItemsDto result = appointmentesItensService.update(fakeBank);

        Assertions.assertEquals(fakeBank.getId(), result.getId());
    }
    @Mock
    CreateAppointmentsItemsPort createAppointmentsItemsPort;

    @Test
    void create(){
        OffsetDateTime start_at = OffsetDateTime.of(
                2026, 11, 6,
                23, 11, 0, 0,
                ZoneOffset.UTC
        );

        OffsetDateTime end_at = OffsetDateTime.of(
                2026, 11, 6,
                23, 13, 0, 0,
                ZoneOffset.UTC
        );

        AppointmentsItemsDto fakeBank = AppointmentsItemsDto.builder()
                .id(17)
                .appointment_id(32)
                .service_id(17)
                .professional_id(12)
                .resource_id(48)
                .start_at(start_at)
                .end_at(end_at)
                .price(BigDecimal.valueOf(80.00)).build();

        AppointmentsItems creating = AppointmentsItems.builder()
                .id(17)
                .appointment_id(32)
                .service_id(17)
                .professional_id(12)
                .resource_id(48)
                .start_at(start_at)
                .end_at(end_at)
                .price(BigDecimal.valueOf(80.00)).build();

        Mockito.when(createAppointmentsItemsPort.create(Mockito.any(AppointmentsItems.class)))
                .thenReturn(creating);

        AppointmentsItemsDto result = appointmentesItensService.create(fakeBank);

        Assertions.assertEquals(BigDecimal.valueOf(80.00),result.getPrice());
        Assertions.assertEquals(17,result.getId());
        Assertions.assertEquals(32,result.getAppointment_id());
        Assertions.assertEquals(17,result.getService_id());
        Assertions.assertEquals(12,result.getProfessional_id());
        Assertions.assertEquals(48,result.getResource_id());
        Assertions.assertEquals(start_at,result.getStart_at());
        Assertions.assertEquals(end_at,result.getEnd_at());
        Mockito.verify(generatePaymentsUseCase).generatePayments(32);
    }
    @Mock
    SearchEndDatePort searchEndDatePort;

    @Test
    void searchEndDate(){
        OffsetDateTime end_at = OffsetDateTime.of(2026,3,23,12,11,0,0,ZoneOffset.UTC);

        AppointmentsItems fakeBank = AppointmentsItems.builder()
                .id(17)
                .appointment_id(32)
                .service_id(17)
                .professional_id(12)
                .resource_id(48)
                .start_at(OffsetDateTime.parse("2026-03-27T23:12:30.000000Z"))
                .end_at(end_at)
                .price(BigDecimal.valueOf(80.00)).build();

        Mockito.when(searchEndDatePort.searchEndDate(end_at))
                .thenReturn(List.of(fakeBank));

        List<AppointmentsItemsDto> result = appointmentesItensService.searchEndDate(end_at);

        AppointmentsItemsDto appointmentsItemsDto = result.get(0);
        Assertions.assertEquals(end_at,appointmentsItemsDto.getEnd_at());
    }
    @Mock
    SearchStartDatePort searchStartDatePort;

    @Test
    void SearchStartDate(){
        OffsetDateTime start_at = OffsetDateTime.of(2026,4,11,9,38,0,0,ZoneOffset.UTC);

        AppointmentsItems fakeBank = AppointmentsItems.builder()
                .id(17)
                .appointment_id(32)
                .service_id(17)
                .professional_id(12)
                .resource_id(48)
                .start_at(start_at)
                .end_at(OffsetDateTime.parse("2026-04-11T10:12:30.000000Z"))
                .price(BigDecimal.valueOf(38.00)).build();

        Mockito.when(searchEndDatePort.searchEndDate(start_at))
                .thenReturn(List.of(fakeBank));

        List<AppointmentsItemsDto> result = appointmentesItensService.searchEndDate(start_at);

        AppointmentsItemsDto appointmentsItemsDto = result.get(0);
        Assertions.assertEquals(start_at,appointmentsItemsDto.getStart_at());
    }
    @Mock
    SearchPricePort searchPricePort;

    @Test
    void searchPrice(){
        BigDecimal price = BigDecimal.valueOf(38.00);
        OffsetDateTime start_at = OffsetDateTime.of(2026,4,11,9,38,0,0,ZoneOffset.UTC);

        AppointmentsItems fakeBank = AppointmentsItems.builder()
                .id(17)
                .appointment_id(32)
                .service_id(17)
                .professional_id(12)
                .resource_id(48)
                .start_at(start_at)
                .end_at(OffsetDateTime.parse("2026-04-11T10:12:30.000000Z"))
                .price(price).build();

        Mockito.when(searchPricePort.searchPrice(price))
                .thenReturn(List.of(fakeBank));

        List<AppointmentsItemsDto> result = appointmentesItensService.searchPrice(price);

        AppointmentsItemsDto appointmentsItemsDto = result.get(0);
        Assertions.assertEquals(price,appointmentsItemsDto.getPrice());
    }
}
