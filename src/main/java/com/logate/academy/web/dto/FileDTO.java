package com.logate.academy.web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FileDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull
	private String title;
	
	private String fileEncoded;
	
	private String description;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileEncoded() {
		return fileEncoded;
	}

	public void setFileEncoded(String fileEncoded) {
		this.fileEncoded = fileEncoded;
	}
}
