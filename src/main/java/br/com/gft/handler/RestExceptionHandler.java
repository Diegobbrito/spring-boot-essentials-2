package br.com.gft.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.gft.exception.ResourceNotFoundDetails;
import br.com.gft.exception.ResourceNotFoundException;

@ControllerAdvice
public class RestExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ResourceNotFoundDetails> handleResourceNotFoundException(ResourceNotFoundException rnfe){
		return new ResponseEntity<>(
				ResourceNotFoundDetails.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value())
				.title("Resource Not Found")
				.detail(rnfe.getMessage())
				.developerMessage(rnfe.getClass().getName())
				.build(), HttpStatus.NOT_FOUND);
	}
}
