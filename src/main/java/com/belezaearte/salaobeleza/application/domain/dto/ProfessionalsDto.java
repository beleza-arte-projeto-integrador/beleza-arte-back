package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfessionalsDto {
    private Integer id;
    private String name;
    private String specialty;
    private String phone;
    private String email;

}
