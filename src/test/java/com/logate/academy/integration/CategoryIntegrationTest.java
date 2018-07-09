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
import com.logate.academy.domains.Category;
import com.logate.academy.repository.CategoryRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = { "dev" })
public class CategoryIntegrationTest {
	
	private static final String BASE_API = "http://localhost:8080/api/categories";
	private static final String AUTH_API = "http://localhost:8080/auth/login";
	
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@PostConstruct
	public void setup() {
		testRestTemplate = new TestRestTemplate();
	}
	
	
	private Category createRegularCategory()
	{
		Category category = new Category();
		category.setName("Kategorija 111");
		category.setOpis("This is 111-s category");
		return category;
	}
	
	private Category createCategoryForList()
	{
		Category category = new Category();
		category.setName("Kategorija 222");
		category.setOpis("This is 222-s category");
		return category;
	}
	
	
	private Category createInvalidCategory()
	{
		Category category = new Category();
		category.setId(7);
		category.setName("Category Invalidt");
		category.setOpis("This is INVALID category");
		return category;
	}
	
	@Test
	public void regularCategoryInsertTest() throws Exception
	{
		// get table size before insert...
		int tableSizeBeforeInsert = categoryRepository.findAll().size();
		
		// create request body...
		Category category = createRegularCategory();
		
		HttpEntity<Category> requestBody = new HttpEntity<>(category, createAdminHeaders());
		ResponseEntity<Category> response = testRestTemplate
				.postForEntity(BASE_API, requestBody, Category.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		
		int tableSizeAfterInsert = categoryRepository.findAll().size();
		assertThat(tableSizeAfterInsert).isEqualTo(tableSizeBeforeInsert + 1);
	}
	
	@Test
	public void invalidCategoryInsertTest() throws Exception
	{
		// get table size before insert...
		int tableSizeBeforeInsert = categoryRepository.findAll().size();
		
		// create request body...
		Category category = createInvalidCategory();
		
		HttpEntity<Category> requestBody = new HttpEntity<>(category, createAdminHeaders());
		ResponseEntity<Category> response = testRestTemplate
				.postForEntity(BASE_API, requestBody, Category.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		
		int tableSizeAfterInsert = categoryRepository.findAll().size();
		assertThat(tableSizeAfterInsert).isEqualTo(tableSizeBeforeInsert);
	}
	
	@Test
	public void getAllCategoriesTest() throws Exception
	{
		Category category = createCategoryForList();
		Category dbCategory = categoryRepository.saveAndFlush(category);  // odmah cuva, bez transakcije!
		
		HttpEntity<Category> requestBody = new HttpEntity<>(createAdminHeaders());
		ResponseEntity<Category[]> response = testRestTemplate
				.exchange(BASE_API, HttpMethod.GET, requestBody, Category[].class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		List<Category> categories = Arrays.asList(response.getBody()).stream().map(categoryFromList -> {
			return (Category) category;
		}).collect(Collectors.toList());
		
		Category lastCategory = categories.get(categories.size() - 1);
		assertThat(lastCategory.getName()).isEqualTo(dbCategory.getName());
		assertThat(lastCategory.getOpis()).isEqualTo(dbCategory.getOpis());
	}
	
	
	@Test
	public void getUnauthorizedCategoryTest() throws Exception
	{
		ResponseEntity<Void> response = testRestTemplate
				.exchange(BASE_API, HttpMethod.GET, null, Void.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
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
