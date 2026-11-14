package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.AppointmentsItemsRepository;
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
public class AppointmentsItemsAdapterTest {
        @InjectMocks
        AppointmentsItemsAdapter appointmentsItemsAdapter;

        @Mock
        AppointmentsItemsRepository appointmentsItemsRepository;

        @Test
        void searchPrice () {
            BigDecimal price = BigDecimal.valueOf(70.00);
            AppointmentsItems fakeBank = AppointmentsItems.builder()
                    .id(38)
                    .price(price)
                    .start_at(OffsetDateTime.parse("2026-12-20T12:30:00.000000Z"))
                    .end_at(OffsetDateTime.parse("2026-12-20T13:30:00.000000Z"))
                    .build();

            Mockito.when(appointmentsItemsRepository.findAll(Mockito.any(Example.class)))
                    .thenReturn(List.of(fakeBank));

            List<AppointmentsItems> result = appointmentsItemsAdapter.searchPrice(price);

            Assertions.assertEquals(price,result.get(0).getPrice());
            Assertions.assertNotNull("Erro: Resultado nulo");
        }

        @Test
        void update(){
            OffsetDateTime searchCreation = OffsetDateTime.parse("2026-07-25T11:06:00.000000Z");

            AppointmentsItems fakeBank = AppointmentsItems.builder()
                    .id(38)
                    .appointment_id(22)
                    .professional_id(12)
                    .resource_id(13)
                    .resource_id(53)
                    .price(new BigDecimal(70.00))
                    .start_at(OffsetDateTime.parse("2026-12-20T12:30:00.000000Z"))
                    .end_at(OffsetDateTime.parse("2026-12-20T13:30:00.000000Z"))
                    .build();

            Mockito.when(appointmentsItemsRepository.findById((Mockito.any())))
                    .thenReturn(Optional.of(fakeBank));

            //modifica e salva
            Mockito.when(appointmentsItemsRepository.saveAndFlush(Mockito.any(AppointmentsItems.class)))
                    .thenReturn(fakeBank);

            //ACT
            AppointmentsItems result = appointmentsItemsAdapter.update(fakeBank);

            //ASSERT
            Assertions.assertNotNull(result, "Erro o objeto não retorna porque está nulo");
            Assertions.assertEquals(fakeBank,result, "O retorno deveria ser igual que foi alterado");
        }
        @Test
        void delete(){
            Integer id = 16;

            Mockito.when(appointmentsItemsRepository.existsById(id))
                    .thenReturn(true);

            appointmentsItemsAdapter.delete(id);

            Mockito.verify(appointmentsItemsRepository, Mockito.times(1)).existsById(id);
            Mockito.verify(appointmentsItemsRepository, Mockito.times(1)).deleteById(id);
        }
        @Test
        void create(){
            AppointmentsItems create = new AppointmentsItems();
            Mockito.when(appointmentsItemsRepository.saveAndFlush(create))
                    .thenReturn(create);

            AppointmentsItems result = appointmentsItemsAdapter.create(create);

            Assertions.assertEquals(create,result);
        }
        @Test
    void SearchStartDate(){
    OffsetDateTime searchStartDate = OffsetDateTime.parse("2026-07-25T11:06:00.000000Z");

    AppointmentsItems fakeBank = AppointmentsItems.builder()
            .id(38)
            .price(BigDecimal.valueOf(92.37))
            .start_at(searchStartDate)
            .end_at(OffsetDateTime.parse("2026-07-25T13:30:00.000000Z"))
            .build();

            Mockito.when(appointmentsItemsRepository.findAll(Mockito.any(Example.class)))
            .thenReturn(List.of(fakeBank));

    List<AppointmentsItems> result = appointmentsItemsAdapter.SearchStartDate(searchStartDate);

            Assertions.assertEquals(searchStartDate,result.get(0).getStart_at());
            Assertions.assertNotNull("Erro: Resultado nulo");
}
    @Test
    void searchEndDate(){
        OffsetDateTime searchEndDate = OffsetDateTime.parse("2026-10-31T06:06:06.000000Z");

        AppointmentsItems fakeBank = AppointmentsItems.builder()
                .id(38)
                .price(BigDecimal.valueOf(92.37))
                .start_at(OffsetDateTime.parse("2026-10-31T09:09:09.000000Z"))
                .end_at(searchEndDate)
                .build();

        Mockito.when(appointmentsItemsRepository.findAll(Mockito.any(Example.class)))
                .thenReturn(List.of(fakeBank));

        List<AppointmentsItems> result = appointmentsItemsAdapter.searchEndDate(searchEndDate);

        Assertions.assertEquals(searchEndDate,result.get(0).getEnd_at());
        Assertions.assertNotNull("Erro: Resultado nulo");
    }
    }


