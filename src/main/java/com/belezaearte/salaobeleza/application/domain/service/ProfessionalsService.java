package com.belezaearte.salaobeleza.application.domain.service;

import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;
import com.belezaearte.salaobeleza.adapter.out.persistence.repository.ProfessionalsRepository;
import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalsDto;
import com.belezaearte.salaobeleza.application.port.in.Professionals.*;
import com.belezaearte.salaobeleza.application.port.out.professionalsPort.*;
import com.belezaearte.salaobeleza.exception.BusinessOutboundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalsService implements CreateProfessionalsUseCase, UpdateProfessionalsUseCase, DeleteProfessionalsUseCase, SearchEmailProfessionalsUseCase, SearchNameProfessionalsUseCase, SearchPhoneProfessionalsUseCase, SearchSpecialtyProfessionalsUseCase, SearchCreatedProfessionalsUseCase {

    private static final String NOT_FOUND = "Could not find professional!, The requested professional was not found in the system!";

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
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public ProfessionalsDto createProfessionals(ProfessionalsDto professionalsDto) {
        Professionals professionalsEntity = toEntity(professionalsDto);
        professionalsEntity.setId(null); // o id é gerado pelo banco
        professionalsEntity.setCreated(null); // a data de cadastro é gerada automaticamente

        // Todo profissional novo começa ativo (no front, o status inicial é "Ativo").
        if (professionalsEntity.getActive() == null) {
            professionalsEntity.setActive(true);
        }

        validateRequiredFields(professionalsEntity);

        // LGPD: a senha é gravada só como hash (BCrypt), nunca a senha original.
        professionalsEntity.setPassword(hashPassword(professionalsEntity.getPassword()));

        // CPF, e-mail e usuário não podem repetir.
        if (professionalsEntity.getCpf() != null && professionalsRepository.existsByCpf(professionalsEntity.getCpf())) {
            throw new BusinessOutboundException("Could not create professional, A professional with this CPF is already registered");
        }
        if (professionalsEntity.getEmail() != null && professionalsRepository.existsByEmail(professionalsEntity.getEmail())) {
            throw new BusinessOutboundException("Could not create professional, A professional with this email is already registered");
        }
        if (professionalsEntity.getUsername() != null && professionalsRepository.existsByUsername(professionalsEntity.getUsername())) {
            throw new BusinessOutboundException("Could not create professional, This username is already in use");
        }

        try {
            Optional<Professionals> professionalsSaved = createProfessionalsPort.create(professionalsEntity);
            return toDto(professionalsSaved.get());
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
        return toDtoListOrNotFound(searchEmailProfessionalsPort.searchEmail(email));
    }

    @Override
    public List<ProfessionalsDto> searchName(String name) {
        return toDtoListOrNotFound(searchNameProfessionalsPort.searchName(name));
    }

    @Override
    public List<ProfessionalsDto> searchPhone(String phone) {
        return toDtoListOrNotFound(searchPhoneProfessionalsPort.searchPhone(phone));
    }

    @Override
    public List<ProfessionalsDto> searchSpecialty(String specialty) {
        return toDtoListOrNotFound(searchSpecialtyProfessionalsPort.searchSpecialty(specialty));
    }

    @Override
    public ProfessionalsDto updateProfessionals(ProfessionalsDto professionalsDto) {
        if (professionalsDto.getId() == null) {
            throw new BusinessOutboundException("Could not update professional, The professional id is required");
        }

        Optional<Professionals> existing = professionalsRepository.findById(professionalsDto.getId());
        if (existing.isEmpty()) {
            throw new BusinessOutboundException("Could not update professional, Check if the professional exists");
        }

        Professionals professionalsEntity = toEntity(professionalsDto);

        // Mantém o que não veio no JSON:
        // - a data de cadastro nunca muda;
        // - senha vazia = mantém a senha atual (igual à tela de perfil do front);
        // - status não informado = mantém o atual;
        // - CPF mascarado (como sai nas respostas, ex: "***.982.247-**") = mantém o CPF atual.
        professionalsEntity.setCreated(existing.get().getCreated());
        if (professionalsEntity.getPassword() == null) {
            professionalsEntity.setPassword(existing.get().getPassword());
        } else {
            // Senha nova: grava só o hash (LGPD).
            professionalsEntity.setPassword(hashPassword(professionalsEntity.getPassword()));
        }
        if (isMaskedCpf(professionalsDto.getCpf())) {
            professionalsEntity.setCpf(existing.get().getCpf());
        }
        if (professionalsEntity.getActive() == null) {
            professionalsEntity.setActive(existing.get().getActive());
        }

        validateRequiredFields(professionalsEntity);

        Integer id = professionalsEntity.getId();
        if (professionalsEntity.getCpf() != null && professionalsRepository.existsByCpfAndIdNot(professionalsEntity.getCpf(), id)) {
            throw new BusinessOutboundException("Could not update professional, Another professional is already registered with this CPF");
        }
        if (professionalsEntity.getEmail() != null && professionalsRepository.existsByEmailAndIdNot(professionalsEntity.getEmail(), id)) {
            throw new BusinessOutboundException("Could not update professional, Another professional is already registered with this email");
        }
        if (professionalsEntity.getUsername() != null && professionalsRepository.existsByUsernameAndIdNot(professionalsEntity.getUsername(), id)) {
            throw new BusinessOutboundException("Could not update professional, This username is already in use");
        }

        Optional<Professionals> professionalUpdated = updateProfessionalsPort.update(professionalsEntity);

        if (professionalUpdated == null || professionalUpdated.isEmpty()){
            throw new BusinessOutboundException("Could not update professional, Check if the professional exists");
        }

        return toDto(professionalUpdated.get());
    }

    @Override
    public List<ProfessionalsDto> professionalCreated(LocalDateTime created) {
        List<Professionals> professionals = searchCreatedProfessionalsPort.professionalCreated(created);
        return professionals.stream().map(this::toDto).toList();
    }

    // ---------------------------------------------------------------
    // Métodos auxiliares
    // ---------------------------------------------------------------

    private void validateRequiredFields(Professionals professional) {
        if (professional.getName() == null) {
            throw new BusinessOutboundException("Invalid professional, The name is required");
        }
        if (professional.getCpf() != null && professional.getCpf().length() != 11) {
            throw new BusinessOutboundException("Invalid professional, The CPF must have 11 digits");
        }
    }

    private List<ProfessionalsDto> toDtoListOrNotFound(List<Professionals> professionals) {
        if (professionals.isEmpty()) {
            throw new BusinessOutboundException(NOT_FOUND);
        }
        return professionals.stream().map(this::toDto).toList();
    }

    // JSON -> entidade (o que vai para o banco).
    private Professionals toEntity(ProfessionalsDto dto) {
        return Professionals.builder()
                .id(dto.getId())
                .name(blankToNull(dto.getName()))
                .specialty(dto.getSpecialty())
                .phone(dto.getPhone())
                .email(blankToNull(dto.getEmail()))
                .cpf(onlyDigits(dto.getCpf()))
                .experienceYears(dto.getExperienceYears())
                .username(blankToNull(dto.getUsername()))
                .password(blankToNull(dto.getPassword()))
                .active(dto.getActive())
                .build();
    }

    // Entidade -> JSON de resposta. A senha não é devolvida e o CPF sai mascarado (LGPD).
    private ProfessionalsDto toDto(Professionals professional) {
        return ProfessionalsDto.builder()
                .id(professional.getId())
                .name(professional.getName())
                .specialty(professional.getSpecialty())
                .phone(professional.getPhone())
                .email(professional.getEmail())
                .cpf(maskCpf(professional.getCpf()))
                .experienceYears(professional.getExperienceYears())
                .username(professional.getUsername())
                .active(professional.getActive())
                .created(professional.getCreated())
                .updated(professional.getUpdated())
                .build();
    }

    // O front manda o CPF com máscara ("529.982.247-25"); no banco ficam só os 11 números.
    private String onlyDigits(String value) {
        if (value == null) {
            return null;
        }
        String digits = value.replaceAll("\\D", "");
        return digits.isEmpty() ? null : digits;
    }

    private String hashPassword(String password) {
        return password == null ? null : passwordEncoder.encode(password);
    }

    // LGPD (minimização): a API não devolve o CPF inteiro, só os dígitos do meio,
    // o suficiente para a pessoa conferir. Ex: "52998224725" -> "***.982.247-**".
    private String maskCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return null;
        }
        return "***." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-**";
    }

    // O front pode mandar de volta o CPF mascarado que recebeu na busca.
    private boolean isMaskedCpf(String cpf) {
        return cpf != null && cpf.contains("*");
    }

    // Texto vazio vira null, para não gravar "" em colunas únicas (e-mail, usuário).
    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
