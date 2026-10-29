//Essa é uma classe de exceção personalizada para representar
//erros de negócio, especialmente ao interagir com sistemas externos.

package com.belezaearte.salaobeleza.exception;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class BusinessOutboundException extends RuntimeException {

    private final String message;
    private final String description;

    public BusinessOutboundException(String message){
        super(message);
        this.message = message;
        this.description = null;
    }

    public BusinessOutboundException(String message, String description){
        super(message);
        this.message = message;
        this.description = description;
    }
}