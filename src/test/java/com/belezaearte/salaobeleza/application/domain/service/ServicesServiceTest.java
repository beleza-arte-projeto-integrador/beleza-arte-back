package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Services;
import com.belezaearte.salaobeleza.application.domain.dto.ServicesDto;
import com.belezaearte.salaobeleza.application.port.out.servicesPort.*;
import org.junit.jupiter.api.Assertions;
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

@ExtendWith(MockitoExtension.class)
public class ServicesServiceTest {

    @Mock
    SearchPriceServicesPort searchPriceServicesPort;
    @InjectMocks
    private ServicesService servicesService;

    @Test
    void searchPrice(){
        BigDecimal price = BigDecimal.valueOf(100.00);

        Services fakeBank = Services.builder()
                .name("Progressiva")
                .id(305)
                .description("Uso de química")
                .price(price)
                .duration_minutes(90)
                .created(OffsetDateTime.parse("2026-11-06T23:11:00.000000Z"))
                .category("Cabelo").build();

        Mockito.when(searchPriceServicesPort.searchPrice(price))
                .thenReturn(List.of(fakeBank));

        List<ServicesDto> result = servicesService.searchPrice(price);

        ServicesDto servicesDto = result.get(0);
        Assertions.assertEquals(price,servicesDto.getPrice());
    }
    @Mock
    SearchNameServicesPort searchNameServicesPort;
    @Test
    void searchName(){
        String name = "Cuidados com a pele";

        Services fakeBank = Services.builder()
                .name(name)
                .id(5)
                .description("Limpeza de pele")
                .price(BigDecimal.valueOf(120.00))
                .duration_minutes(120)
                .created(OffsetDateTime.parse("2026-11-06T23:11:00.000000Z"))
                .category("Pele").build();

        Mockito.when(searchNameServicesPort.searchName(name))
                .thenReturn(List.of(fakeBank));

        List<ServicesDto> result = servicesService.searchName(name);

        ServicesDto servicesDto = result.get(0);
        Assertions.assertEquals(name,servicesDto.getName());
    }
    @Mock
    SearchDurationServicesPort searchDurationServicesPort;
    @Test
    void searchDurationMinuts(){
        Integer duration_minutes = 60;

        Services fakeBank = Services.builder()
                .name("Cabelo")
                .id(2)
                .description("Penteado para casamento")
                .price(BigDecimal.valueOf(90.00))
                .duration_minutes(duration_minutes)
                .created(OffsetDateTime.parse("2026-11-06T23:11:00.000000Z"))
                .category("Penteado").build();

        Mockito.when(searchDurationServicesPort.searchDurationMinuts(duration_minutes))
                .thenReturn(List.of(fakeBank));

        List<ServicesDto> result = servicesService.searchDurationMinuts(duration_minutes);

        ServicesDto servicesDto = result.get(0);
        Assertions.assertEquals(duration_minutes,servicesDto.getDuration_minutes());
    }
    @Mock
    SearchDescriptionServicesPort searchDescriptionServicesPort;
    @Test
    void searchDescription(){
        String description = "Esmaltação de unhas, alongamentos e amparos.";

        Services fakeBank = Services.builder()
                .name("Unhas")
                .id(98)
                .description(description)
                .price(BigDecimal.valueOf(90.00))
                .duration_minutes(50)
                .created(OffsetDateTime.parse("2026-11-06T23:11:00.000000Z"))
                .category("Unhas").build();

        Mockito.when(searchDescriptionServicesPort.searchDescription(description))
                .thenReturn(List.of(fakeBank));

        List<ServicesDto> result = servicesService.searchDescription(description);

        ServicesDto servicesDto = result.get(0);
        Assertions.assertEquals(description,servicesDto.getDescription());
    }
    @Mock
    SearchCreatedServicesPort searchCreatedServicesPort;
    @Test
    void searchCreated(){
        OffsetDateTime searchCreation = OffsetDateTime.of(
                2026, 11, 6,
                23, 11, 0, 0,
                ZoneOffset.UTC
        );
        Services fakeBank = Services.builder()
                .name("Unhas")
                .id(98)
                .description("Esmaltação de unhas, alongamentos e amparos")
                .price(BigDecimal.valueOf(90.00))
                .duration_minutes(50)
                .created(searchCreation)
                .category("Unhas").build();

        Mockito.when(searchCreatedServicesPort.searchCreated(searchCreation))
                .thenReturn(List.of(fakeBank));

        List<ServicesDto> result = servicesService.searchCreated(searchCreation);

        ServicesDto servicesDto = result.get(0);
        Assertions.assertEquals(searchCreation,servicesDto.getCreated());
    }
    @Mock
    UpdateServicesPort updateServicesPort;
    @Test
    void update(){

        ServicesDto fakeBank = ServicesDto.builder()
                .name("Unhas")
                .id(98)
                .description("Esmaltação de unhas, alongamentos e amparos")
                .price(BigDecimal.valueOf(90.00))
                .duration_minutes(50)
                .created(OffsetDateTime.parse("2026-11-06T23:11:00.000000Z"))
                .category("Unhas").build();

        Services updating = Services.builder()
                .name("Unhas")
                .id(98)
                .description("Esmaltação de unhas, alongamentos e amparos")
                .price(BigDecimal.valueOf(90.00))
                .duration_minutes(50)
                .created(OffsetDateTime.parse("2026-11-06T23:11:00.000000Z"))
                .category("Unhas").build();

        Mockito.when(updateServicesPort.update(Mockito.any(Services.class)))
                .thenReturn(updating);

        ServicesDto result = servicesService.update(fakeBank);

        Assertions.assertEquals(fakeBank.getCreated(), result.getCreated());
    }
    @Mock
    SearchCategoryPort searchCategoryPort;
    @Test
    void categories(){
        String category = "Unhas";

        Services fakeBank = Services.builder()
                .name("Esmaltação em unhas de gel")
                .id(100)
                .description("Esmaltação de unhas, alongamentos e amparos, unhas de fibras e unhas em gel")
                .price(BigDecimal.valueOf(200.00))
                .duration_minutes(60)
                .created(OffsetDateTime.parse("2026-11-23T23:59:00.000000Z"))
                .category(category).build();

        Mockito.when(searchCategoryPort.categories(category))
                .thenReturn(List.of(fakeBank));

        List<ServicesDto> result = servicesService.categories(category);

        ServicesDto servicesDto = result.get(0);
        Assertions.assertEquals(category,servicesDto.getCategory());
    }
    @Mock
    DeleteServicesPort deleteServicesPort;
    @Test
    void delete(){
        Integer deleteId = 365;

        servicesService.delete(deleteId);

        Mockito.verify(deleteServicesPort,Mockito.times(1)).delete(deleteId);

    }
    @Mock
    CreateServicesPort createServicesPort;
    @Test
    void create(){
        OffsetDateTime searchCreation = OffsetDateTime.of(
                2026, 11, 6,
                23, 11, 0, 0,
                ZoneOffset.UTC
        );
        ServicesDto fakeBank = ServicesDto.builder()
                .id(33)
                .name("Alongamento capilar")
                .description("Mega hair ou aplique com Queratina (K-Tip), Tic-tac, Microlink")
                .price(BigDecimal.valueOf(80.00))
                .duration_minutes(60)
                .created(OffsetDateTime.parse("2026-11-23T23:59:00.000000Z"))
                .category("Cabelo").build();

        Services creating = Services.builder()
                .id(33)
                .name("Alongamento capilar")
                .description("Mega hair ou aplique com Queratina (K-Tip), Tic-tac, Microlink")
                .price(BigDecimal.valueOf(80.00))
                .duration_minutes(60)
                .created(searchCreation)
                .category("Cabelo").build();

        Mockito.when(createServicesPort.create(Mockito.any(Services.class)))
                .thenReturn(creating);

        //ACT
        ServicesDto result = servicesService.create(fakeBank);

        //ASSERT
        Assertions.assertEquals("Alongamento capilar",result.getName());
        Assertions.assertEquals("Mega hair ou aplique com Queratina (K-Tip), Tic-tac, Microlink",result.getDescription());
        Assertions.assertEquals(BigDecimal.valueOf(80.00), result.getPrice());
        Assertions.assertEquals(60,result.getDuration_minutes());
        Assertions.assertEquals(searchCreation,result.getCreated());
        Assertions.assertEquals("Cabelo", result.getCategory());
    }
}
