package com.etnetera.hr.controller;

import com.etnetera.hr.rest.Errors;
import com.etnetera.hr.rest.ValidationError;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Main REST controller.
 * 
 * @author Etnetera
 *
 */
public abstract class EtnRestController {

	/*
		every time MethodArgumentNotValidException
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Errors> handleValidationException(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();

		Errors errors = new Errors();
		List<ValidationError> errorList = result.getFieldErrors()//Get all errors associated with a field.
				.stream()
				.map(e -> new ValidationError(e.getField(), e.getCode()))
				.collect(Collectors.toList());

		errors.setErrors(errorList);

		return ResponseEntity.badRequest().body(errors);
	}

}
