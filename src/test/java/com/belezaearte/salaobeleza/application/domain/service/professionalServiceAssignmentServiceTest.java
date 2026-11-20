package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.ProfessionalServiceAssignment;
import com.belezaearte.salaobeleza.adapter.out.persistence.ProfessionalServiceAssignmentAdapter;
import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;
import com.belezaearte.salaobeleza.adapter.out.persistence.Services;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ProfessionalServiceAssignmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)

public class professionalServiceAssignmentServiceTest {
    @Mock
    ProfessionalServiceAssignmentRepository professionalServiceAssignmentRepository;

    @InjectMocks
    private ProfessionalServiceAssignmentAdapter professionalServiceAssignmentAdapter;

    @Test
    void update(){
        Professionals idProfessionals = Professionals.builder()
                .id(33)
                .build();

        Services idServices = Services.builder()
                .id(20)
                .build();

        ProfessionalServiceAssignment UpdateFakeBank = ProfessionalServiceAssignment.builder()
                .id(17)

                .idProfessional(idProfessionals)
                .idService(idServices).build();


        Mockito.when(professionalServiceAssignmentRepository.findById((Mockito.any())))
                .thenReturn(Optional.of(UpdateFakeBank));

        //modifica e salva
        Mockito.when(professionalServiceAssignmentRepository.saveAndFlush(Mockito.any(ProfessionalServiceAssignment.class)))
                .thenReturn(UpdateFakeBank);

        //ACT
        Optional<ProfessionalServiceAssignment> result = professionalServiceAssignmentAdapter.update(UpdateFakeBank);

        //ASSERT
        Assertions.assertNotNull(result, "Erro o objeto não retorna porque está nulo");
        Assertions.assertEquals(UpdateFakeBank,result.get(), "O retorno deveria ser igual que foi alterado");
    }
    @Test
    void delete() {
        Integer id = 16;

        professionalServiceAssignmentAdapter.delete(id);

        Mockito.verify(professionalServiceAssignmentRepository, Mockito.times(1))
                .deleteById(id);
    }
    @Test
    void createProfessional(){
        ProfessionalServiceAssignment create = new ProfessionalServiceAssignment();

        Mockito.when(professionalServiceAssignmentRepository.saveAndFlush(create))
                .thenReturn(create);

        Optional<ProfessionalServiceAssignment> result = professionalServiceAssignmentAdapter.create(create);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(create, result.get());
    }
    @Test
    void searchIdProfessional () {
        Professionals Professional = Professionals.builder()
                .id (17)
                .build();

        ProfessionalServiceAssignment fakeBank = ProfessionalServiceAssignment.builder()
                .id(20)
                .idProfessional(Professional)
                .build();

        Mockito.when(professionalServiceAssignmentRepository.findAll(Mockito.any(Example.class)))
                .thenReturn(List.of(fakeBank));

        List<ProfessionalServiceAssignment> result = professionalServiceAssignmentAdapter.searchIdProfessional(Professional);

        Assertions.assertEquals(Professional,result.get(0).getIdProfessional());
        Assertions.assertNotNull("Erro: Resultado nulo");
    }
    @Test
    void searchIdService () {
        Services idService = Services.builder()
                .id (17)
                .build();

        ProfessionalServiceAssignment fakeBank = ProfessionalServiceAssignment.builder()
                .id(28)
                .idService(idService)
                .build();

        Mockito.when(professionalServiceAssignmentRepository.findAll(Mockito.any(Example.class)))
                .thenReturn(List.of(fakeBank));

        List<ProfessionalServiceAssignment> result = professionalServiceAssignmentAdapter.searchIdService(idService);

        Assertions.assertEquals(idService,result.get(0).getIdService());
        Assertions.assertNotNull("Erro: Resultado nulo");
    }
}
