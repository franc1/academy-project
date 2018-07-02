package com.logate.academy.security.exceptions;

import org.springframework.context.annotation.Description;
import org.springframework.security.core.AuthenticationException;

@Description(value = "Custom User Not Found Exception.")
public class UserNotActivatedException extends AuthenticationException {

	public UserNotActivatedException(final String message) {
		super(message);
	}

}
