package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

// O agendamento (a visita). Profissional, serviço, horário e preço ficam em cada item (items),
// porque numa mesma visita o cliente pode fazer vários serviços (veja docs/banco-de-dados.md).
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentsDto {
    private Integer id;
    private Integer idClient;
    private String status;
    private String notes;
    private String cancelledBy;
    private String cancelReason;
    private OffsetDateTime cancelledAt;
    private OffsetDateTime created;
    private OffsetDateTime updated;
    // Só nas respostas: os itens são cadastrados pelos endpoints de /appointmentsItems.
    private List<AppointmentsItemsDto> items;
}
