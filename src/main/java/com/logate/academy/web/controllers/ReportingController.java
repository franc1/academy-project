package com.logate.academy.web.controllers;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.logate.academy.services.ArticleService;
import com.logate.academy.services.CommentService;
import com.logate.academy.web.dto.ArticleDTO;
import com.logate.academy.web.dto.CommentDTO1;
import com.logate.academy.web.exceptions.ValidationException;
import com.logate.academy.web.validators.ArticleValidator;

@RestController
@RequestMapping(value = "/api")
public class ReportingController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportingController.class);
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private CommentService commentService;
	
	@RequestMapping(
			value = "/last5articles", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Article>> getLastFiveArticles(){
		List<Article> articles = articleService.findLastFive();
		return new ResponseEntity<>(articles, HttpStatus.OK); 
	}
	
	
	@RequestMapping(
			value = "/top-article", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ArticleDTO>> getTopFiveArticles(){
		List<ArticleDTO> articles = articleService.findTopFive();
		return new ResponseEntity<>(articles, HttpStatus.OK); 
	}
	
	@RequestMapping(
			value = "/top5comments", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CommentDTO1>> getTopFiveComments(){
		List<CommentDTO1> comments = commentService.findTopFiveComments();
		return new ResponseEntity<>(comments, HttpStatus.OK); 
	}
	
	@RequestMapping(
			value = "/worst5comments", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CommentDTO1>> getWorstFiveComments(){
		List<CommentDTO1> comments = commentService.findWorstFiveComments();
		return new ResponseEntity<>(comments, HttpStatus.OK); 
	}
	
}
