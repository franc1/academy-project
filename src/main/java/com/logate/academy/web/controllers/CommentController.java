package com.logate.academy.web.controllers;

import org.slf4j.Logger;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.logate.academy.domains.*;
import com.logate.academy.filters.Filter;

import java.util.*;

import com.logate.academy.services.CommentService;
import com.logate.academy.web.dto.CommentDTO;
import com.logate.academy.web.exceptions.ValidationException;
import com.logate.academy.web.validators.CommentValidator;

@RestController
@RequestMapping(value = "/api")
public class CommentController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private CommentValidator commentValidator;
	
	// izlistava sve komentare
	/*@GetMapping(value = "/comments",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Comment>> getAllComments(){
		List<Comment> comments = commentService.getAllComments();
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}*/
	
	// novi komentar
	@PostMapping(value = "/comments",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("@authComponent.hasLess10(#commentDTO)")
	public ResponseEntity<Comment> storeComment(@RequestBody CommentDTO commentDTO) throws ValidationException{
		
		Errors potentialErrors = new BeanPropertyBindingResult(commentDTO, "comment");
		ValidationUtils.invokeValidator(commentValidator, commentDTO, potentialErrors);
		
		if (potentialErrors.hasErrors()) {
			throw new ValidationException(potentialErrors);
		}
		
		Comment storedComment = commentService.store(commentDTO);
		return Optional.ofNullable(storedComment)
				.map(comm -> new ResponseEntity<>(comm, HttpStatus.CREATED))
				.orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
	}
	
	
	// brsianje komentara po id-u
	@RequestMapping(value = "/comments1/{id}", 
			method = RequestMethod.DELETE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("@authComponent.canDelete(#commentId)")
	public ResponseEntity<Void> deleteComment(@PathVariable(value = "id") Integer commentId){
		Optional<Comment> comment = commentService.findById(commentId);
		if (comment.isPresent()) {        
			commentService.delete(commentId);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	// brisanje komentara za odredjeni clanak
	@RequestMapping(value = "/comments", 
			method = RequestMethod.DELETE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteCommentsByArticle(@RequestBody Article article){
		commentService.deleteByArticle(article);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	// azuriranje komentara za odredjeni clanak
	@PutMapping(value = "/comments", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("@authComponent.hasPermission(#commentDTO)")
	public ResponseEntity<Comment> updateComment(@RequestBody CommentDTO commentDTO){
		
		Comment updatedComment = commentService.updateComment(commentDTO);
		return Optional.ofNullable(updatedComment)
				.map(comm -> new ResponseEntity<>(comm, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	
	// azuriranje like-ova i dislike-ova za odredjeni clanak
	/*@PutMapping(value = "/comments/{id}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Comment> updateLikesAndDislikes(@PathVariable(value = "id") Integer commentId,
			@RequestParam(value = "like", required = false) Integer like,
			@RequestParam(value = "dislike", required = false) Integer dislike){
		
		Comment updatedComment = commentService.updateLikesAndDislikes(commentId, like, dislike);
		return Optional.ofNullable(updatedComment)
				.map(comm -> new ResponseEntity<>(comm, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}*/
	
	
	// like a comment
	@PutMapping(value = "/comments-like/{id}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Comment> updateLikes(@PathVariable(value = "id") Integer commentId){
		
		Comment updatedComment = commentService.updateLikes(commentId);
		return Optional.ofNullable(updatedComment)
				.map(comm -> new ResponseEntity<>(comm, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	// like a comment
	@PutMapping(value = "/comments-dislike/{id}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Comment> updateDislikes(@PathVariable(value = "id") Integer commentId){
		
		Comment updatedComment = commentService.updateDislikes(commentId);
		return Optional.ofNullable(updatedComment)
				.map(comm -> new ResponseEntity<>(comm, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	
}
