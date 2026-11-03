package com.belezaearte.salaobeleza.adapter.in.web.controller;

import com.belezaearte.salaobeleza.application.domain.dto.ProfessionalsDto;
import com.belezaearte.salaobeleza.application.port.in.Professionals.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ProfessionalsController {

    @Autowired
    private CreateProfessionalsUseCase createProfessionalsUseCase;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/createProfessionals",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ProfessionalsDto createProfessionals(@RequestBody ProfessionalsDto professionalsDto){
        return createProfessionalsUseCase.createProfessionals(professionalsDto);
    }

    @Autowired
    private DeleteProfessionalsUseCase deleteProfessionalsUseCase;

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/deleteProfessionals"
    )
    public ResponseEntity<Void> deleteProfessionals(@RequestParam Integer id){
        deleteProfessionalsUseCase.deleteProfessionals(id);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    private UpdateProfessionalsUseCase updateProfessionalsUseCase;

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/updateProfessionals",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ProfessionalsDto updateProfessionals(@RequestBody ProfessionalsDto professionalsDto){
        return updateProfessionalsUseCase.updateProfessionals(professionalsDto);
    }

    @Autowired
    private SearchEmailProfessionalsUseCase searchEmailProfessionalsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchEmail",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ProfessionalsDto>> searchEmail (@RequestParam String email) {
        List<ProfessionalsDto> professionalsDto = searchEmailProfessionalsUseCase.searchEmail(email);
        return ResponseEntity.ok(professionalsDto);
    }

    @Autowired
    private SearchNameProfessionalsUseCase searchNameProfessionalsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchName",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ProfessionalsDto>> searchName (String name) {
        List<ProfessionalsDto> professionalsDto = searchNameProfessionalsUseCase.searchName(name);
        return ResponseEntity.ok(professionalsDto);
    }

    @Autowired
    private SearchPhoneProfessionalsUseCase searchPhoneProfessionalsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchPhone",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ProfessionalsDto>> searchPhone (String phone) {
        List<ProfessionalsDto> professionalsDto = searchPhoneProfessionalsUseCase.searchPhone(phone);
        return ResponseEntity.ok(professionalsDto);
    }


    @Autowired
    private SearchSpecialtyProfessionalsUseCase searchSpecialtyProfessionalsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchSpecialty",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ProfessionalsDto>> searchSpecialty (String specialty) {
        List<ProfessionalsDto> professionalsDto = searchSpecialtyProfessionalsUseCase.searchSpecialty(specialty);
        return ResponseEntity.ok(professionalsDto);
    }

    @Autowired
    private SearchCreatedProfessionalsUseCase searchCreatedProfessionalsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchCreated",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ProfessionalsDto>> professionalCreated (
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime created){
        List<ProfessionalsDto> created1 = searchCreatedProfessionalsUseCase.professionalCreated(created);

        return ResponseEntity.ok(created1);
    }


}
