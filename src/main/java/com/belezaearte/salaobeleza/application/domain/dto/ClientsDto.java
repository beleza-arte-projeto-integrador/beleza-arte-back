package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientsDto {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private LocalDate birthDate;
    private String preferences;
    private LocalDateTime created;
    private LocalDateTime updated;

}
