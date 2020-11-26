package br.com.gft.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.gft.exception.ExceptionDetails;
import br.com.gft.exception.ResourceNotFoundDetails;
import br.com.gft.exception.ResourceNotFoundException;
import br.com.gft.exception.ValidationExceptionDetails;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ResourceNotFoundDetails> handleResourceNotFoundException(ResourceNotFoundException rnfe) {
		return new ResponseEntity<>(ResourceNotFoundDetails.builder().timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value()).title("Resource Not Found").detail(rnfe.getMessage())
				.developerMessage(rnfe.getClass().getName()).build(), HttpStatus.NOT_FOUND);
	}
	

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

		String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));

		String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.joining(", "));

		return new ResponseEntity<>(ValidationExceptionDetails.builder().timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value()).title("Field Validation Error")
				.detail("Check the field(s) below").developerMessage(ex.getClass().getName()).fields(fields)
				.fieldsMessage(fieldsMessage).build(), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		ExceptionDetails.builder()
		.timestamp(LocalDateTime.now())
		.status(status.value())
		.title(ex.getCause().getMessage())
		.detail(ex.getMessage())
		.developerMessage(ex.getClass().getName())
		.build();

		return new ResponseEntity<>(body, headers, status);
	}

}
