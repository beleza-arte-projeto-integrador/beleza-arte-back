package com.belezaearte.salaobeleza.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.util.Optional;

@ControllerAdvice
@Slf4j
public class ControlExceptionHandler {
    @ExceptionHandler(value = {BusinessOutboundException.class})
    protected ResponseEntity<Object> handleConflict(BusinessOutboundException businessOutboundException, WebRequest request) {
        BusinessExceptionBody ex = BusinessExceptionBody.builder()
                .code(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()))
                .message(Optional.ofNullable(businessOutboundException.getMessage()).orElse(businessOutboundException.toString()))
                .description(businessOutboundException.getDescription())
                .build();

        log.warn(businessOutboundException.getMessage(), businessOutboundException);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .headers(this.getHeaders())
                .body(ex);
    }

/*
	// Tratar erro 4XX
	@ExceptionHandler({MethodArgumentTypeMismatchException.class})
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exMethod, WebRequest request) {
		String methodName = "default";
		String type = "default";
		String description = "default";

		if (exMethod != null) {
			methodName = exMethod.getName();
			Class<?> requiredType = exMethod.getRequiredType();
			if (requiredType != null) {
				type = requiredType.getSimpleName();
			}
			description = exMethod.getMessage();
		}

		String message = methodName + " should be " + type;

		BusinessExceptionBody ex = BusinessExceptionBody.builder()
				.message(message)
				.description(description)
				.build();

		log.warn(ex.getMessage(), exMethod);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.headers(this.getHeaders())
				.body(ex);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> validationError(MethodArgumentNotValidException exMethod) {
		BindingResult bindingResult = exMethod.getBindingResult();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		List<String> fieldErrorDtos = fieldErrors.stream()
				.map(f -> f.getField()
						.concat(":")
						.concat(Optional.ofNullable(f.getDefaultMessage()).orElse("argument not valid")))
				.map(String::new)
				.toList();

		BusinessExceptionBody ex = BusinessExceptionBody.builder()
				.message(fieldErrorDtos.toString())
				.description(exMethod.getMessage())
				.build();

		log.warn(ex.getMessage(), exMethod);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.headers(this.getHeaders())
				.body(ex);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> validationError(HttpMessageNotReadableException exMethod) {
		String message = "O corpo da requisição não pôde ser lido ou está mal formado.";
		Throwable mostSpecificCause = exMethod.getMostSpecificCause();
		BusinessExceptionBody ex = BusinessExceptionBody.builder()
				.message(message)
				.description(mostSpecificCause.getMessage())
				.build();

		log.warn(ex.getMessage(), exMethod);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.headers(this.getHeaders())
				.body(ex);
	}

	@ExceptionHandler({MissingServletRequestParameterException.class})
	public ResponseEntity<Object> handleException(MissingServletRequestParameterException e) {
		log.warn(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.headers(this.getHeaders())
				.body(this.getBody(e));
	}

	@ExceptionHandler({HttpRequestMethodNotSupportedException.class})
	public ResponseEntity<Object> handleException(HttpRequestMethodNotSupportedException e) {
		log.warn(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.headers(this.getHeaders())
				.body(this.getBody(e));
	}
*/

    // Tratar erro 5XX
/*	@ExceptionHandler({Throwable.class})
	public ResponseEntity<Object> handleException(Throwable eThrowable) {

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.headers(this.getHeaders())
				.body(eThrowable);
	}
*/
    // Métodos auxiliares
    private HttpHeaders getHeaders() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        return responseHeaders;
    }

/*
    private BusinessExceptionBody getBody(ServletException servletException) {
		return BusinessExceptionBody.builder()
				.message(Optional.ofNullable(servletException.getMessage()).orElse(servletException.toString()))
                .build();
	}
*/
}