package com.logate.academy.web.controllers;

import org.slf4j.Logger;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.logate.academy.services.CategoryService;
import com.logate.academy.services.CommentService;

@RestController
@RequestMapping(value = "/api")
public class CategoryController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
	
	@Autowired
	private CategoryService categoryService;
	
	// izlistava sve kategorije
	@GetMapping(value = "/categories",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Category>> getAllCategories(){
		List<Category> categories = categoryService.getAllCategories();
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}
	
	// unosenje nove kategorije
	@PostMapping(value = "/categories",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Category> storeCategory(@RequestBody Category category){
		if (category.getId() != null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Category storedCategory = categoryService.store(category);
		return Optional.ofNullable(storedCategory)
				.map(categ -> new ResponseEntity<>(categ, HttpStatus.CREATED))
				.orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
	}
	
	// azuriranje kategorije
	@PutMapping(value = "/categories/{id}", 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Category> updateCategory(@RequestBody Category category,
			@PathVariable(value = "id") Integer categoryId){
		
		if (category.getId() == null || category.getId() != categoryId) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Category updatedCategory = categoryService.updateCategory(category);
		return Optional.ofNullable(updatedCategory)
				.map(categ -> new ResponseEntity<>(categ, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	
}
