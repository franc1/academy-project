package com.logate.academy.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.logate.academy.domains.Article;
import com.logate.academy.domains.Category;
import com.logate.academy.domains.Comment;
import com.logate.academy.domains.User;
import com.logate.academy.repository.CommentRepository;
import com.logate.academy.repository.UserRepository;
import com.logate.academy.web.dto.ArticleDTO;
import com.logate.academy.web.dto.CommentDTO;
import com.logate.academy.web.dto.CommentDTO1;

@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	Authentication auth;

	public Comment store(CommentDTO commentDTO) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> userOpt = userRepository.findByUsername(auth.getPrincipal().toString());
		User user = userOpt.get();
		Comment comment = new Comment();
		comment.setArticle(commentDTO.getArticle());
		comment.setBody(commentDTO.getBody());
		comment.setUser(user);
		comment.setPublishedAt(commentDTO.getPublishedAt());
		
		return commentRepository.save(comment);
	}

	public void delete(Integer commentId) {
		commentRepository.deleteById(commentId);
	}

	public void deleteByArticle(Article article) {
		List<Comment> comments = commentRepository.findByArticle(article);
		for(Comment comment: comments) {
			commentRepository.deleteById(comment.getId());
		}
	}

	public Comment updateComment(CommentDTO commentDTO) {
		auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> user = userRepository.findByUsername(auth.getPrincipal().toString());
		Comment dbComment = commentRepository.findByArticleAndUser(commentDTO.getArticle(), user.get()).get(0);
		if(dbComment.getId() != null) {
			dbComment.setBody(commentDTO.getBody());
			dbComment.setPublishedAt(commentDTO.getPublishedAt());
			return commentRepository.save(dbComment);
		}
		return null;
	}

	public Optional<Comment> findById(Integer commentId) {
		
		return commentRepository.findById(commentId);
	}

	public Comment updateLikesAndDislikes(Integer commentId, Integer like, Integer dislike) {
		Optional<Comment> dbComment = commentRepository.findById(commentId);
		if (!dbComment.isPresent()) {
			return null;
		}
		
		Comment comment = dbComment.get();
		if(like != null) {
			comment.setLikes(like);}
		
		if(dislike != null) {
			comment.setDislikes(dislike);}
		
		return commentRepository.save(comment);
	}


	public List<Comment> getAllComments() {
		return commentRepository.findAll();
	}

	public Comment updateLikes(Integer commentId) {
		Optional<Comment> dbComment = commentRepository.findById(commentId);
		if (!dbComment.isPresent()) {
			return null;
		}
		Comment comment = dbComment.get();
		comment.setLikes(comment.getLikes()+1);
		
		return commentRepository.save(comment);
	}

	public Comment updateDislikes(Integer commentId) {
		Optional<Comment> dbComment = commentRepository.findById(commentId);
		if (!dbComment.isPresent()) {
			return null;
		}
		Comment comment = dbComment.get();
		comment.setDislikes(comment.getDislikes()+1);
		
		return commentRepository.save(comment);
	}

	public List<CommentDTO1> findTopFiveComments() {
		
		List<Object[]> objectsList = commentRepository.findTopFive();
		List<CommentDTO1> topComments = new ArrayList<>();
		
		// processing data...
		objectsList
			.stream()
			.forEach(comments -> {
				CommentDTO1 commentDTO = new CommentDTO1(
					(Integer) comments[0],
					(Integer) comments[1],
					(String) comments[2],
					(Date) comments[3],
					(Integer) comments[4],
					(Integer) comments[5],
					(Integer) comments[6]
				);
				topComments.add(commentDTO);
			});
		
		return topComments;
		
	}

	public List<CommentDTO1> findWorstFiveComments() {
		
		List<Object[]> objectsList = commentRepository.findWorstFive();
		List<CommentDTO1> topComments = new ArrayList<>();
		
		// processing data...
		objectsList
			.stream()
			.forEach(comments -> {
				CommentDTO1 commentDTO = new CommentDTO1(
					(Integer) comments[0],
					(Integer) comments[1],
					(String) comments[2],
					(Date) comments[3],
					(Integer) comments[4],
					(Integer) comments[5],
					(Integer) comments[6]
				);
				topComments.add(commentDTO);
			});
		
		return topComments;
		
	}
	
}
