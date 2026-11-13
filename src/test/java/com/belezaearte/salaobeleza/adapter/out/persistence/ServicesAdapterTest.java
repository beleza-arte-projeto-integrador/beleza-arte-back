package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ServicesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ServicesAdapterTest {
    @InjectMocks
    ServicesAdapter servicesAdapter;

    @Mock
    ServicesRepository servicesRepository;

    @Test
    void sharchCategories(){
        //Arrange
        String categories = "Unhas";

        Services fakeBank = Services.builder()
                .category(categories)
                .build();

        Mockito.when(servicesRepository.findAll(Mockito.any(Example.class)))
                .thenReturn(List.of(fakeBank));

        //Act
        List<Services> result = servicesAdapter.categories(categories);

        //Assert
        Assertions.assertNotNull("Erro: Categoria não encontrada");
        Assertions.assertEquals(categories, result.get(0).getCategory());

    }
    @Test
    void searchName(){
        //Arrange
        String name = "Coloração";
        Services fakeBank = Services.builder()
                .id(13)
                .name(name)
                .description("Coloração total do cabelo ou mechas")
                .price(new BigDecimal(200.00))
                .duration_minutes(60)
                .created(OffsetDateTime.parse("2026-08-05T14:40:44.440084Z"))
                .category("Cabelo").build();


        Mockito.when(servicesRepository.findAll(Mockito.any(Example.class)))
                .thenReturn(List.of(fakeBank));

        //Act
        List<Services> result = servicesAdapter.searchName(name);

        //Assert
        Assertions.assertNotNull("Erro: Resultado nulo você esqueceu algum dado.");
        Assertions.assertEquals(name, result.get(0).getName());
    }
    @Test
    void searchCreated() {
        OffsetDateTime searchCreation = OffsetDateTime.parse("2026-07-25T11:06:00.000000Z");

        Services fakeBank = Services.builder()
                .id(12)
                .name("Corte de cabelo")
                .description("Corte seus cabelos")
                .price(new BigDecimal(72.00))
                .duration_minutes(40)
                .created(OffsetDateTime.parse("2026-07-25T11:06:00.000000Z"))
                .category("Cabelo")
                .build();

        Mockito.when(servicesRepository.findAll(Mockito.any(Example.class)))
                .thenReturn(List.of(fakeBank));

        List<Services> result = servicesAdapter.searchCreated(searchCreation);

        Assertions.assertNotNull("Erro: Resultado nulo");
        Assertions.assertEquals(searchCreation, result.get(0).getCreated());

    }
    @Test
    void searchDurationMinuts(){
        Integer duration_minutes = 60;

        Services fakeBank = Services.builder()
                .id(9)
                .name("Penteados")
                .description("Escovamos, fazemos tranças e colocamos acessórios")
                .price(new BigDecimal(50.00))
                .duration_minutes(duration_minutes)
                .created(OffsetDateTime.parse("2026-10-02T09:00:00.000000Z"))
                .category("Penteados").build();

        Mockito.when(servicesRepository.findAll(Mockito.any(Example.class)))
                .thenReturn(List.of(fakeBank));

        List<Services> result =servicesAdapter.searchDurationMinuts(duration_minutes);

        Assertions.assertNotNull("Erro: Resultado nulo");
        Assertions.assertEquals(duration_minutes, result.get(0).getDuration_minutes());
    }
    @Test
    void  searchDescription(){
        String description = "Realçamos sua beleza e criamos efeitos artisticos";

        Services fakeBank = Services.builder()
                .id(38)
                .name("Maquiagem")
                .description(description)
                .price(new BigDecimal(70.00))
                .duration_minutes(90)
                .created(OffsetDateTime.parse("2026-12-20T12:30:00.000000Z"))
                .category("Estetica-Maquiagem")
                .build();

        Mockito.when(servicesRepository.findAll(Mockito.any(Example.class)))
                .thenReturn(List.of(fakeBank));

        List<Services> result = servicesAdapter.searchDescription(description);

        Assertions.assertEquals(description,result.get(0).getDescription());
        Assertions.assertNotNull("Erro: Resultado nulo");
    }
    @Test
    void searchPrice () {
        BigDecimal price = BigDecimal.valueOf(70.00);
        Services fakeBank = Services.builder()
                .id(38)
                .name("Maquiagem")
                .description("Realçamos sua beleza e criamos efeitos artisticos")
                .price(price)
                .duration_minutes(90)
                .created(OffsetDateTime.parse("2026-12-20T12:30:00.000000Z"))
                .category("Estetica-Maquiagem")
                .build();

        Mockito.when(servicesRepository.findAll(Mockito.any(Example.class)))
                .thenReturn(List.of(fakeBank));

        List<Services> result = servicesAdapter.searchPrice(price);

        Assertions.assertEquals(price,result.get(0).getPrice());
        Assertions.assertNotNull("Erro: Resultado nulo");
    }
    @Test
    void update(){
        OffsetDateTime searchCreation = OffsetDateTime.parse("2026-07-25T11:06:00.000000Z");

        Services fakeBank = Services.builder()
                .id(38)
                .name("Hidratação capilar")
                .description("Com uso de cremes hidratantes que auxiliam na fortificação dos fios e aumenta o brilho")
                .price(new BigDecimal(70.00))
                .duration_minutes(90)
                .created(searchCreation)
                .category("Hidratação de Cabelo")
                .build();

        Mockito.when(servicesRepository.findById((Mockito.any())))
                .thenReturn(Optional.of(fakeBank));

        //modifica e salva
        Mockito.when(servicesRepository.saveAndFlush(Mockito.any(Services.class)))
                .thenReturn(fakeBank);

        //ACT
        Services result = servicesAdapter.update(fakeBank);

        //ASSERT
        Assertions.assertNotNull(result, "Erro o objeto não retorna porque está nulo");
        Assertions.assertEquals(fakeBank,result, "O retorno deveria ser igual que foi alterado");
    }
    @Test
    void delete(){
        Integer id = 16;

        Mockito.when(servicesRepository.existsById(id))
                .thenReturn(true);

        servicesAdapter.delete(id);

        Mockito.verify(servicesRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(servicesRepository, Mockito.times(1)).deleteById(id);
    }
    @Test
    void create(){
        Services create = new Services();
        Mockito.when(servicesRepository.saveAndFlush(create))
                .thenReturn(create);

        Services result = servicesAdapter.create(create);

        Assertions.assertEquals(create,result);
    }
}
