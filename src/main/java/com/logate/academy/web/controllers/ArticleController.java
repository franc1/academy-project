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
import com.logate.academy.web.exceptions.ValidationException;
import com.logate.academy.web.validators.ArticleValidator;

@RestController
@RequestMapping(value = "/api")
public class ArticleController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private ArticleValidator articleValidator;
	
	@PostMapping(value = "/articles",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Article> storeArticle(@RequestBody Article article) throws ValidationException{
		if (article.getId() != null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Errors potentialErrors = new BeanPropertyBindingResult(article, "article");
		ValidationUtils.invokeValidator(articleValidator, article, potentialErrors);
		
		if (potentialErrors.hasErrors()) {
			throw new ValidationException(potentialErrors);
		}
		
		Article storedArticle = articleService.store(article);
		return Optional.ofNullable(storedArticle)
				.map(artic -> new ResponseEntity<>(artic, HttpStatus.CREATED))
				.orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
	}
	
	
	@RequestMapping(value = "/articles/{id}", 
			method = RequestMethod.DELETE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteArticle(@PathVariable(value = "id") Integer articleId){
		Optional<Article> article = articleService.findById(articleId);
		if (article.isPresent()) {        
			articleService.delete(articleId);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping(value = "/articles",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Article>> getAllArticles(){
		List<Article> articles = articleService.getAllArticles();
		return new ResponseEntity<>(articles, HttpStatus.OK);
	}
	
	
	@RequestMapping(
			value = "/articles/{id}", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Article> getArticleById(@PathVariable(value = "id") Integer articleId){
		Optional<Article> article = articleService.findById(articleId);
		if (article.isPresent()) {        
			return new ResponseEntity<>(article.get(), HttpStatus.OK); 
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	// moraju se update-ovati svi podaci za jednog usera (pathVariable ovdje ne igra nikakvu ulogu)
	@PutMapping(value = "/articles/{id}", 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Article> updateArticle(@PathVariable(value = "id") Integer articleId,
										   @RequestBody Article article){
		if (article.getId() == null || article.getId() != articleId) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Article updatedArticle = articleService.updateArticle(article);
		//return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
		return Optional.ofNullable(updatedArticle)
				.map(artic -> new ResponseEntity<>(artic, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));	
	}
	
}
