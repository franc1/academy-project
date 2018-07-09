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
import com.logate.academy.domains.Role;
import com.logate.academy.repository.RoleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = { "dev" })
public class RoleIntegrationTest {
	
	private static final String BASE_API = "http://localhost:8080/api/roles";
	private static final String AUTH_API = "http://localhost:8080/auth/login";
	
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@PostConstruct
	public void setup() {
		testRestTemplate = new TestRestTemplate();
	}
	
	
	private Role createRegularRole()
	{
		Role role = new Role();
		role.setName("ROLE_CEO");
		role.setDescription("CEO role...");
		return role;
	}
	
	
	private Role createInvalidRole()
	{
		Role role = new Role();
		role.setId(5);
		role.setDescription("Comercialist role...");
		return role;
	}
	
	@Test
	public void regularRoleInsertTest() throws Exception
	{
		// get table size before insert...
		int tableSizeBeforeInsert = roleRepository.findAll().size();
		
		// create request body...
		Role role = createRegularRole();
		
		HttpEntity<Role> requestBody = new HttpEntity<>(role, createAdminHeaders());
		ResponseEntity<Role> response = testRestTemplate
				.postForEntity(BASE_API, requestBody, Role.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		
		int tableSizeAfterInsert = roleRepository.findAll().size();
		assertThat(tableSizeAfterInsert).isEqualTo(tableSizeBeforeInsert + 1);
	}
	
	@Test
	public void invalidUserInsertTest() throws Exception
	{
		// get table size before insert...
		int tableSizeBeforeInsert = roleRepository.findAll().size();
		
		// create request body...
		Role role = createInvalidRole();
		
		HttpEntity<Role> requestBody = new HttpEntity<>(role, createAdminHeaders());
		ResponseEntity<Role> response = testRestTemplate
				.postForEntity(BASE_API, requestBody, Role.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		
		int tableSizeAfterInsert = roleRepository.findAll().size();
		assertThat(tableSizeAfterInsert).isEqualTo(tableSizeBeforeInsert);
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
