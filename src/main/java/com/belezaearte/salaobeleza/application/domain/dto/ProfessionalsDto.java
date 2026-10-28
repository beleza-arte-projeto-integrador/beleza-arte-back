package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProfessionalsDto {
 public Integer id;
 public String name;
 public String specialty;
 public String phone;
 public String email;
 public LocalDateTime created;
}
