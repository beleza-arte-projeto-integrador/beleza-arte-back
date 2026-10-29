package com.belezaearte.salaobeleza.adapter.out.persistence;

import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ProfessionalsRepository;
import com.belezaearte.salaobeleza.application.port.out.clientsPort.SearchCreatedClientsPort;
import com.belezaearte.salaobeleza.application.port.out.professionalsPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ProfessionalsAdapter implements CreateProfessionalsPort, UpdateProfessionalsPort, DeleteProfessionalsPort, SearchEmailProfessionalsPort, SearchNameProfessionalsPort, SearchPhoneProfessionalsPort, SearchSpecialtyProfessionalsPort, SearchCreatedProfessionalsPort {

    private final ProfessionalsRepository professionalsRepository;

    @Override
    public Optional<Professionals> create(Professionals professionals) {
        return Optional.of(professionalsRepository.saveAndFlush(professionals));
    }

    @Override
    public void delete(Integer id) {
        professionalsRepository.deleteById(id);
    }

    @Override
    public List<Professionals> searchEmail(String email) {
        Professionals professionals = Professionals.builder().email(email).build();
        Example<Professionals> example = Example.of(professionals);
        return professionalsRepository.findAll(example);
    }

    @Override
    public List<Professionals> searchName(String name) {
        Professionals professionals = Professionals.builder().name(name).build();
        Example<Professionals> example = Example.of(professionals);
        return professionalsRepository.findAll(example);
    }

    @Override
    public List<Professionals> searchPhone(String phone) {
        Professionals professionals = Professionals.builder().phone(phone).build();
        Example<Professionals> example = Example.of(professionals);
        return professionalsRepository.findAll(example);
    }

    @Override
    public List<Professionals> searchSpecialty(String specialty) {
        Professionals professionals = Professionals.builder().specialty(specialty).build();
        Example<Professionals> example = Example.of(professionals);
        return professionalsRepository.findAll(example);
    }

    @Override
    public Optional<Professionals> update(Professionals professionals) {
        Optional<Professionals> professionalsOptional = professionalsRepository.findById(professionals.getId());
        if (professionalsOptional.isPresent()){
            return  Optional.of(professionalsRepository.saveAndFlush(professionals));
        } else {
            return null;
        }
    }


    @Override
    public List<Professionals> professionalCreated(LocalDateTime created) {
        Professionals professionals = Professionals.builder().created(created).build();
        Example<Professionals> example = Example.of(professionals);

        List<Professionals> professionalsList = professionalsRepository.findAll(example);

        if(professionalsList.isEmpty()){
            throw new BusinessOutboundException("Erro: Informação"+ created," não encontrada");
        }
        return professionalsList;
    }
}
