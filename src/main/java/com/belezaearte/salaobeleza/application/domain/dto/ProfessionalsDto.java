package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalsDto {
 public Integer id;
 public String name;
 public String specialty;
 public String phone;
 public String email;
 public String cpf;
 public Integer experienceYears;
 public String username;
 // Só é usada no cadastro e na edição; não volta nas respostas da API.
 public String password;
 public Boolean active;
 public LocalDateTime created;
 public LocalDateTime updated;
}
