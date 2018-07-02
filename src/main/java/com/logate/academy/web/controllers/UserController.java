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

import com.logate.academy.services.UserService;
import com.logate.academy.web.exceptions.ValidationException;
import com.logate.academy.web.validators.UserValidator;

@RestController
@RequestMapping(value = "/api")
public class UserController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserValidator userValidator;
	
	@GetMapping(value = "/users",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<User>> getAllUsers(){
		List<User> users = userService.getAllUsers();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/users-page", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<User>> getAllUsers(Pageable pageable){
		Page<User> users = userService.getAllUsers(pageable);     // vraca stranicu(page) sa userima
		return new ResponseEntity<>(users.getContent(), HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/users/{id}", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getUserById(@PathVariable(value = "id") Integer userId){
		Optional<User> user = userService.getUserById(userId);
		if (user.isPresent()) {         // provjerava da li postoji User...
			return new ResponseEntity<>(user.get(), HttpStatus.OK);    // user.get() daje Usera
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	// moraju se update-ovati svi podaci za jednog usera (pathVariable ovdje ne igra nikakvu ulogu)
	@PutMapping(value = "/users/{id}", 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> updateUser(@PathVariable(value = "id") Integer id,
										   @RequestBody User user){
		User updatedUser = userService.updateUser(user);
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}
	
	
	//update-uju se samo firstName i lastName
	@RequestMapping(value = "/users-update/{id}", 
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> shortUpdateUser(@PathVariable(value = "id") Integer id,
										   @RequestBody User user){
		if (user.getId() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		User updatedUser = userService.updateUser(user, id);
		return Optional.ofNullable(updatedUser)
				.map(usr -> new ResponseEntity<>(usr, HttpStatus.OK))   // user je ustvari updateUser
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	
	@PostMapping(value = "/users",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> storeUser(@RequestBody User user) throws ValidationException{
		if (user.getId() != null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Errors potentialErrors = new BeanPropertyBindingResult(user, "user");
		ValidationUtils.invokeValidator(userValidator, user, potentialErrors);
		
		if (potentialErrors.hasErrors()) {
			throw new ValidationException(potentialErrors);
		}
		
		User storedUser = userService.store(user);
		return Optional.ofNullable(storedUser)
				.map(usr -> new ResponseEntity<>(usr, HttpStatus.CREATED))
				.orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
	}
	
	@RequestMapping(value = "/users/{id}", 
			method = RequestMethod.DELETE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") Integer id){
		userService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	// 1. nacin requesta sa parametrima...
	@RequestMapping(value = "/users-params", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> mapQueryParams(
			@RequestParam(value = "firstName") String firstName, 
			@RequestParam(value = "lastName") String lastName){
		LOGGER.info("Request param 1: {} *** Request Param 2: {}", firstName, lastName);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	// 2. nacin requesta sa parametrima (kad ima vise parametara)
	@RequestMapping(value = "/users-map-params", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> mapQueryParams(@RequestParam Map<String, Object> params){
		LOGGER.info("Request params: {}", params);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	// 3. nacin requesta sa parametrima, parametri se mapiraju sa napravljenom klasom Filter
	@RequestMapping(value = "/users-model-attrs", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> mapModelAttributes(@ModelAttribute Filter filter){
		LOGGER.info("Request params: {}", filter);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/users/entire/{id}", 
			method = RequestMethod.PUT,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> updateEntireObject(@PathVariable(value = "id") Integer id, 
			@RequestBody User user){
		user = userService.updateEntire(user, id);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

}
