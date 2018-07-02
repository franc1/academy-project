package com.logate.academy.web.dto;

import java.io.Serializable;
import java.util.Date;

import com.logate.academy.domains.Article;

public class CommentDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Article article;
	private String body;
	private Date publishedAt;
	
	public CommentDTO() {}

	public CommentDTO(Article article, String body, Date publishedAt) {
		this.article = article;
		this.body = body;
		this.publishedAt = publishedAt;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(Date publishedAt) {
		this.publishedAt = publishedAt;
	}

	@Override
	public String toString() {
		return "CommentDTO [article=" + article + ", body=" + body + ", publishedAt=" + publishedAt + "]";
	}

	
}
