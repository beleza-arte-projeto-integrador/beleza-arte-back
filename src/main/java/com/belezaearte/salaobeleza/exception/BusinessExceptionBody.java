//Ela serve para padronizar o JSON dos erros
// estrutura o conteúdo da resposta de erro (código, mensagem, data, entre outros campos).

//Quando acontece um erro, o cliente recebe algo assim no postman:
//{ "code": "BUSINESS_ERROR",
//   "message": "Cliente não encontrado.",
//   "timestamp": "2026-07-07T18:20:00" }
package com.belezaearte.salaobeleza.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusinessExceptionBody {
    private String code;
    private String message;
    private String description;
}
