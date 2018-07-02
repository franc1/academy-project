package com.logate.academy.security.jwt;

import java.io.Serializable;

import org.springframework.context.annotation.Description;

@Description(value = "JWT Data Transfer Object.")
public class JWTToken implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String token;
	
	public JWTToken(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return this.token;
	}

	@Override
	public String toString() {
		return "JWTToken [token=" + token + "]";
	}
}
