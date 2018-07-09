package com.logate.academy.unit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.coyote.http11.Http11AprProtocol;
import org.assertj.core.util.Arrays;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logate.academy.domains.Category;
import com.logate.academy.domains.User;
import com.logate.academy.services.CategoryService;
import com.logate.academy.services.UserService;
import com.logate.academy.web.controllers.CategoryController;
import com.logate.academy.web.controllers.UserController;
import com.mysql.fabric.xmlrpc.base.Array;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
//import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryControllerUnitTest {
	
	private static final String BASE_API = "/api/categories";
	
	private MockMvc mockMvc;  
	
	@Mock
	private CategoryService categoryService;

	@InjectMocks
	private CategoryController categoryController;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
				.build();
	}
	
	@Test
	public void getAllCategoriesTest() throws Exception
	{
		List<Category> mockedCategory = prepareMockData();
		
		// when
		given(categoryService.getAllCategories()).willReturn(mockedCategory);
		
		// request
		MvcResult result = mockMvc.perform(get(BASE_API)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andReturn();
		
		Category[] responseCat = new ObjectMapper().readValue(
			result.getResponse().getContentAsString(), Category[].class);   //pretvara Json string u objekat
		
		List<Category> respCateg = Arrays.asList(responseCat).stream()
				.map(categ -> {return (Category) categ;})
				.collect(Collectors.toList());
		
		// then
		assertThat(respCateg.get(0).getId()).isEqualTo(9);
		assertThat(respCateg.get(0).getName()).isEqualTo("Category Hello");
		
		assertThat(respCateg.get(1).getId()).isEqualTo(13);
		assertThat(respCateg.get(1).getName()).isEqualTo("Category Congratulations!");
				
	}
	
	private List<Category> prepareMockData()
	{
		List<Category> categories = new ArrayList<>();
		Category category1 = new Category();
		category1.setId(9);
		category1.setName("Category Hello");
		category1.setOpis("Our first category...");

		Category category2 = new Category();
		category2.setId(13);
		category2.setName("Category Congratulations!");
		category2.setOpis("Our second category...");
		
		categories.add(category1);
		categories.add(category2);
		
		return (categories);
	}
	
	@Test
	public void postCategoryTest() throws Exception
	{
		Category mockedCategory = prepareMockCategory();
		Category responseMockedCategory = prepareMockCategory();
		responseMockedCategory.setId(9);

		// when
		when(categoryService.store(Mockito.any(Category.class))).thenReturn(responseMockedCategory);  //za POST
		
		// request
		MvcResult result = mockMvc.perform(post(BASE_API)
			.content(asJsonString(mockedCategory))
			.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isCreated())
			.andReturn();
		
		Category responseCat = new ObjectMapper().readValue(
			result.getResponse().getContentAsString(), Category.class);   //pretvara Json string u objekat
		
		
		// then
		assertThat(responseCat.getId()).isEqualTo(9);
		assertThat(responseCat.getName()).isEqualTo("Category Hello");
		assertThat(responseCat.getOpis()).isEqualTo("Our first category...");
				
	}
	
	private Category prepareMockCategory()
	{
		Category category = new Category();
		//category.setId(9);
		category.setName("Category Hello");
		category.setOpis("Our first category...");
		return category;
	}
	
	
	// convert object to Json string...
	private static String asJsonString(final Object obj) {
		
		try {
			return new ObjectMapper().writeValueAsString(obj);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
