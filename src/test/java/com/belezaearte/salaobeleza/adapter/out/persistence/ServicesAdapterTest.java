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
    void searchCreated(){

    }
}
