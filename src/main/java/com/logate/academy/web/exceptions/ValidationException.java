package com.logate.academy.web.exceptions;

import org.springframework.context.annotation.Description;
import org.springframework.validation.Errors;

@Description(value = "Validation Exception")
public class ValidationException extends Exception {
	
	private Errors errors;
	
	public ValidationException(Errors errors) {
		this.errors = errors;
	}

	public Errors getErrors() {
		return errors;
	}
}
