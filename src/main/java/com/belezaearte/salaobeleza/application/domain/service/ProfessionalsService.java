package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ProfessionalsRepository;
import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalsDto;
import com.belezaearte.salaobeleza.application.port.in.Professionals.*;
import com.belezaearte.salaobeleza.application.port.out.professionalsPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalsService implements CreateProfessionalsUseCase, UpdateProfessionalsUseCase, DeleteProfessionalsUseCase, SearchEmailProfessionalsUseCase, SearchNameProfessionalsUseCase, SearchPhoneProfessionalsUseCase, SearchSpecialtyProfessionalsUseCase, SearchCreatedProfessionalsUseCase {

    @Autowired
    private CreateProfessionalsPort createProfessionalsPort;
    @Autowired
    private UpdateProfessionalsPort updateProfessionalsPort;
    @Autowired
    private DeleteProfessionalsPort deleteProfessionalsPort;
    @Autowired
    private SearchEmailProfessionalsPort searchEmailProfessionalsPort;
    @Autowired
    private SearchNameProfessionalsPort searchNameProfessionalsPort;
    @Autowired
    private SearchPhoneProfessionalsPort searchPhoneProfessionalsPort;
    @Autowired
    private SearchSpecialtyProfessionalsPort searchSpecialtyProfessionalsPort;
    @Autowired
    private SearchCreatedProfessionalsPort searchCreatedProfessionalsPort;
    @Autowired
    private ProfessionalsRepository professionalsRepository;



    @Override
    public ProfessionalsDto createProfessionals(ProfessionalsDto professionalsDto) {
        Professionals professionalsEntity = Professionals.builder()
                .id(professionalsDto.getId())
                .name(professionalsDto.getName())
                .specialty(professionalsDto.getSpecialty())
                .phone(professionalsDto.getPhone())
                .email(professionalsDto.email)
                .build();
        try {
            Optional<Professionals> professionalsSaved = createProfessionalsPort.create(professionalsEntity);

            return ProfessionalsDto.builder()
                    .id(professionalsSaved.get().getId())
                    .name(professionalsSaved.get().getName())
                    .specialty(professionalsSaved.get().getSpecialty())
                    .phone(professionalsSaved.get().getPhone())
                    .email(professionalsSaved.get().getEmail())
                    .created(professionalsSaved.get().getCreated())
                    .build();
        } catch (Exception e){
            throw  new BusinessOutboundException("Could not create professional, Check if the professional is already registered");
        }
    }

    @Override
    public void deleteProfessionals(Integer id) {
        Optional<Professionals> professionalsOptional = professionalsRepository.findById(id);
        if (professionalsOptional.isPresent()){
            deleteProfessionalsPort.delete(id);
        } else {
            throw new BusinessOutboundException("Could not delete Professional, Check if the Professional exists");
        }

    }

    @Override
    public List<ProfessionalsDto> searchEmail(String email) {
        List<Professionals> professionals = searchEmailProfessionalsPort.searchEmail(email);

        if (!professionals.isEmpty()){
            return professionals.stream().map(professional -> ProfessionalsDto.builder()
                    .id(professional.getId())
                    .name(professional.getName())
                    .specialty(professional.getSpecialty())
                    .phone(professional.getPhone())
                    .email(professional.getEmail())
                    .created(professional.getCreated())
                    .build())
                    .toList();
        } else {
            throw  new BusinessOutboundException("Could not find professional!, The requested professional was not found in the system!");
        }
    }

    @Override
    public List<ProfessionalsDto> searchName(String name) {
        List<Professionals> professionals = searchNameProfessionalsPort.searchName(name);

        if (!professionals.isEmpty()){
            return professionals.stream().map(professional -> ProfessionalsDto.builder()
                    .id(professional.getId())
                    .name(professional.getName())
                    .specialty(professional.getSpecialty())
                    .phone(professional.getPhone())
                    .email(professional.getEmail())
                    .created(professional.getCreated())
                    .build())
                    .toList();
        } else {
            throw new BusinessOutboundException("Could not find professional!, The requested professional was not found in the system!");
        }
    }

    @Override
    public List<ProfessionalsDto> searchPhone(String phone) {
        List<Professionals> professionals = searchPhoneProfessionalsPort.searchPhone(phone);

        if (!professionals.isEmpty()){
            return professionals.stream().map(professional -> ProfessionalsDto.builder()
                    .id(professional.getId())
                    .name(professional.getName())
                    .specialty(professional.getSpecialty())
                    .phone(professional.getPhone())
                    .email(professional.getEmail())
                    .created(professional.getCreated())
                    .build())
                    .toList();
        }
        else{
            throw new BusinessOutboundException("Could not find professional!, The requested professional was not found in the system!");
        }
    }

    @Override
    public List<ProfessionalsDto> searchSpecialty(String specialty) {
        List<Professionals> professionals = searchSpecialtyProfessionalsPort.searchSpecialty(specialty);

        if (!professionals.isEmpty()){
            return professionals.stream().map(professional -> ProfessionalsDto.builder()
                    .id(professional.getId())
                    .name(professional.getName())
                    .specialty(professional.getSpecialty())
                    .phone(professional.getPhone())
                    .email(professional.getEmail())
                    .created(professional.getCreated())
                    .build())
                    .toList();
        }
        else{
            throw new BusinessOutboundException("Could not find professional!, The requested professional was not found in the system!");
        }
    }

    @Override
    public ProfessionalsDto updateProfessionals(ProfessionalsDto professionalsDto) {
        Professionals professionalsEntity = Professionals.builder()
                .id(professionalsDto.getId())
                .name(professionalsDto.getName())
                .specialty(professionalsDto.getSpecialty())
                .phone(professionalsDto.getPhone())
                .email(professionalsDto.email)
                .created(professionalsDto.created)
                .build();
        Optional<Professionals> professionalUpdated = updateProfessionalsPort.update(professionalsEntity);

        if (professionalUpdated == null){
            throw new BusinessOutboundException("Could not update professional, Check if the professional exists");
        }

        return ProfessionalsDto.builder()
                .id(professionalUpdated.get().getId())
                .name(professionalUpdated.get().getName())
                .specialty(professionalUpdated.get().getSpecialty())
                .phone(professionalUpdated.get().getPhone())
                .email(professionalUpdated.get().getEmail())
                .created(professionalUpdated.get().getCreated())
                .build();
    }

    @Override
    public List<ProfessionalsDto> professionalCreated(LocalDateTime created) {
        List<Professionals> professionals = searchCreatedProfessionalsPort.professionalCreated(created);
        return professionals.stream().map(professionals1 -> ProfessionalsDto.builder()
                .created(professionals1.getCreated())
                        .id(professionals1.getId())
                        .name(professionals1.getName())
                        .specialty(professionals1.getSpecialty())
                        .phone(professionals1.getPhone())
                        .email(professionals1.getEmail())
                .build()).toList();
    }
}
