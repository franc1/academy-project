package com.logate.academy.web.dto;

import java.io.Serializable;
import java.util.Date;

public class ArticleDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String title;
	private Integer employeeId;
	private Date publishedAt;
	private Integer documentId;
	private Integer categoryId;
	
	public ArticleDTO() {}

	public ArticleDTO(Integer id, String title, Integer employeeId, Date publishedAt, Integer documentId,
			Integer categoryId) {
		this.id = id;
		this.title = title;
		this.employeeId = employeeId;
		this.publishedAt = publishedAt;
		this.documentId = documentId;
		this.categoryId = categoryId;
	}

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

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Date getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(Date publishedAt) {
		this.publishedAt = publishedAt;
	}

	public Integer getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return "ArticleDTO [id=" + id + ", title=" + title + ", employeeId=" + employeeId + ", publishedAt="
				+ publishedAt + ", documentId=" + documentId + ", categoryId=" + categoryId + "]";
	}
	
	
}
