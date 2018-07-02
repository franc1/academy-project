package com.logate.academy.web.dto;

import java.io.Serializable;
import java.util.Date;

import com.logate.academy.domains.Article;

public class CommentDTO1 implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private Integer articleId;
	private String body;
	private Date publishedAt;
	private Integer likes;
	private Integer dislikes;
	private Integer userId;
	
	public CommentDTO1() {}

	public CommentDTO1(Integer id, Integer articleId, String body, Date publishedAt, Integer likes, Integer dislikes,
			Integer userId) {
		this.id = id;
		this.articleId = articleId;
		this.body = body;
		this.publishedAt = publishedAt;
		this.likes = likes;
		this.dislikes = dislikes;
		this.userId = userId;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
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

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Integer getDislikes() {
		return dislikes;
	}

	public void setDislikes(Integer dislikes) {
		this.dislikes = dislikes;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "CommentDTO1 [id=" + id + ", articleId=" + articleId + ", body=" + body + ", publishedAt=" + publishedAt
				+ ", likes=" + likes + ", dislikes=" + dislikes + ", userId=" + userId + "]";
	}

	
}
