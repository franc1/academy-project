package com.logate.academy.integration;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logate.academy.domains.User;
import com.logate.academy.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = { "dev" })
public class UserIntegrationTest {
	
	private static final String BASE_API = "http://localhost:8080/api/users";
	private static final String AUTH_API = "http://localhost:8080/auth/login";
	
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	@PostConstruct
	public void setup() {
		testRestTemplate = new TestRestTemplate();
	}
	
	
	private User createRegularUser()
	{
		User user = new User();
		user.setFirstName("testFN001");
		user.setLastName("testLN001");
		user.setEmail("testEM001@gmail.com");
		user.setUsername("testUN001");
		user.setPassword("test001PSW");
		user.setAge(26);
		
		return user;
	}
	
	private User createUserForList()
	{
		User user = new User();
		user.setFirstName("testFN4001");
		user.setLastName("testLN0201");
		user.setEmail("testEM001223@gmail.com");
		user.setUsername("testU112N001");
		user.setPassword("test001PS11W");
		user.setAge(25);
		
		return user;
	}
	
	private User createUserForDelete()
	{
		User user = new User();
		user.setFirstName("testFN14001");
		user.setLastName("testLN10201");
		user.setEmail("testEM0011223@gmail.com");
		user.setUsername("testU1212N001");
		user.setPassword("test0013PS11W");
		user.setAge(23);
		
		return user;
	}
	
	private User createInvalidUser()
	{
		User user = new User();
		user.setId(1);
		user.setUsername("test123455");
		
		return user;
	}
	
	@Test
	public void regularUserInsertTest() throws Exception
	{
		// get table size before insert...
		int tableSizeBeforeInsert = userRepository.findAll().size();
		
		// create request body...
		User user = createRegularUser();
		
		HttpEntity<User> requestBody = new HttpEntity<>(user, createAdminHeaders());
		ResponseEntity<User> response = testRestTemplate
				.postForEntity(BASE_API, requestBody, User.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		
		int tableSizeAfterInsert = userRepository.findAll().size();
		assertThat(tableSizeAfterInsert).isGreaterThan(tableSizeBeforeInsert);
	}
	
	@Test
	public void invalidUserInsertTest() throws Exception
	{
		// get table size before insert...
		int tableSizeBeforeInsert = userRepository.findAll().size();
		
		// create request body...
		User user = createInvalidUser();
		
		HttpEntity<User> requestBody = new HttpEntity<>(user, createAdminHeaders());
		ResponseEntity<User> response = testRestTemplate
				.postForEntity(BASE_API, requestBody, User.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		
		int tableSizeAfterInsert = userRepository.findAll().size();
		assertThat(tableSizeAfterInsert).isEqualTo(tableSizeBeforeInsert);
	}
	
	@Test
	public void getAllUsersTest() throws Exception
	{
		User user = createUserForList();
		User dbUser = userRepository.saveAndFlush(user);  // odmah cuva, bez transakcije!
		
		HttpEntity<User> requestBody = new HttpEntity<>(createAdminHeaders());
		ResponseEntity<User[]> response = testRestTemplate
				.exchange(BASE_API, HttpMethod.GET, requestBody, User[].class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		List<User> users = Arrays.asList(response.getBody()).stream().map(userFromList -> {
			return (User) userFromList;
		}).collect(Collectors.toList());
		
		User lastUser = users.get(users.size() - 1);
		assertThat(lastUser.getFirstName()).isEqualTo(dbUser.getFirstName());
		assertThat(lastUser.getLastName()).isEqualTo(dbUser.getLastName());
	}
	
	@Test
	public void getUnauthorizedUsersTest() throws Exception
	{
		ResponseEntity<Void> response = testRestTemplate
				.exchange(BASE_API, HttpMethod.GET, null, Void.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}
	
	@Test
	public void deleteUser() throws Exception
	{
		User user = createUserForDelete();
		user = userRepository.saveAndFlush(user);

		int tableSizeBeforeDelete = userRepository.findAll().size();
		
		HttpEntity<Void> requestEntity = new HttpEntity<>(createAdminHeaders());
		ResponseEntity<Void> response = testRestTemplate
				.exchange(BASE_API + "/" + user.getId(), HttpMethod.DELETE, requestEntity, Void.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		int tableSizeAfterDelete = userRepository.findAll().size();
		assertThat(tableSizeBeforeDelete).isGreaterThan(tableSizeAfterDelete);
	}
	
	private HttpHeaders createAdminHeaders() throws Exception
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer ".concat(getAdminToken()));
		
		return headers;
	}
	
	private String getAdminToken() throws Exception
	{
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		Map<String, String> params = new HashMap<>();
		params.put("username", "markoa");
		params.put("password", "heril123");
		
		HttpEntity<Map<String, String>> requestBody = new HttpEntity<>(params, httpHeaders);
		
		ResponseEntity<String> response = testRestTemplate.postForEntity(
			AUTH_API, requestBody, String.class
		);
		
		Map<String, String> body = new ObjectMapper().readValue(response.getBody(), Map.class);
		return body.get("token").toString();
	} 
}
