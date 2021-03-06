package com.eaa.login.advice;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@SuppressWarnings({ "unchecked", "rawtypes" })
@ControllerAdvice
public class EntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ErrorMessage error = new ErrorMessage("Server Error", details);
		return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@ExceptionHandler(RecordNotFoundException.class)
//	public final ResponseEntity<Object> handleUserNotFoundException(RecordNotFoundException ex, WebRequest request) {
//		List<String> details = new ArrayList<>();
//		details.add(ex.getLocalizedMessage());
//		ErrorMessage error = new ErrorMessage("Record Not Found", details);
//		return new ResponseEntity(error, HttpStatus.NOT_FOUND);
//	}
//
//	@ExceptionHandler(ValidationException.class)
//	public final ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//			HttpHeaders headers, HttpStatus status, WebRequest request) {
//		List<String> details = new ArrayList<>();
//		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
//			details.add(error.getDefaultMessage());
//		}
//		ErrorMessage error = new ErrorMessage("Validation Failed", details);
//		return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
//	}

}
