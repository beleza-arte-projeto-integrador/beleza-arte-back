package com.belezaearte.salaobeleza.application.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientsDto {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String address;

}
