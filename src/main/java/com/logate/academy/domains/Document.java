package com.logate.academy.domains;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.springframework.context.annotation.Description;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Description(value = "Article Documentation")
@Entity
@Table(name = "documents")
@DynamicInsert
@JsonIgnoreProperties(ignoreUnknown = true) 
public class Document implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Integer id;
	
	@Column(nullable = false)
	private String title;
	
	@Column(name = "path", nullable = false)
	private String filePath;
	
	@Column
	private String description;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Document [id=" + id + ", filePath=" + filePath + ", description=" + description + "]";
	}
}
