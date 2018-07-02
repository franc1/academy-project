package com.logate.academy.web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.context.annotation.Description;

@Description(value = "Login Data Transfer Object")
public class LoginDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotNull
	@NotEmpty
	@NotBlank
	@Size(min = 5, max = 32)
	private String username;
	
	@NotNull
	@NotEmpty
	@NotBlank
	@Size(min = 8)
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginDTO [username=" + username + ", password=" + password + "]";
	}
}
