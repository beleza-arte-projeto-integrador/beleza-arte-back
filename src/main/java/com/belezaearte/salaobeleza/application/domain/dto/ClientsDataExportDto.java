package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Resposta da exportação de dados do cliente (LGPD, art. 18).
// Diz quais dados o salão guarda, para que servem e quando a cópia foi gerada.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientsDataExportDto {
    private ClientsDto client;
    private String purpose;
    private LocalDateTime generatedAt;
}
