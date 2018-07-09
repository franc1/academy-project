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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logate.academy.domains.Role;
import com.logate.academy.services.RoleService;
import com.logate.academy.web.controllers.RoleController;
import com.mysql.fabric.xmlrpc.base.Array;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
//import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleControllerUnitTest {
	
	private static final String BASE_API = "/api/roles";
	
	private MockMvc mockMvc;  
	
	@Mock
	private RoleService roleService;

	@InjectMocks
	private RoleController roleController;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(roleController)
				.build();
	}
	
	@Test
	public void postRoleTest() throws Exception
	{
		Role mockedRole = prepareMockRole();
		Role responseMockedRole = prepareMockRole();
		responseMockedRole.setId(9);

		// when
		when(roleService.store(Mockito.any(Role.class))).thenReturn(responseMockedRole);  //za POST
		
		// request
		MvcResult result = mockMvc.perform(post(BASE_API)
			.content(asJsonString(mockedRole))
			.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isCreated())
			.andReturn();
		
		Role responseRole = new ObjectMapper().readValue(
			result.getResponse().getContentAsString(), Role.class);   //pretvara Json string u objekat
		
		
		// then
		assertThat(responseRole.getId()).isEqualTo(9);
		assertThat(responseRole.getName()).isEqualTo("ROLE_CEO");
		assertThat(responseRole.getDescription()).isEqualTo("CEO role...");
				
	}
	
	private Role prepareMockRole()
	{
		Role role = new Role();
		role.setName("ROLE_CEO");
		role.setDescription("CEO role...");
		return role;
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
	
	
	@Test
	public void postIvalidRoleTest() throws Exception
	{
		Role mockedRole = prepareInvalidMockRole();
		Role responseMockedRole = prepareInvalidMockRole();
		responseMockedRole.setId(9);

		// when
		when(roleService.store(Mockito.any(Role.class))).thenReturn(responseMockedRole);  //za POST
		
		// request
		mockMvc.perform(post(BASE_API)
			.content(asJsonString(mockedRole))
			.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isBadRequest());
	
				
	}
	
	private Role prepareInvalidMockRole()
	{
		Role role = new Role();
		role.setDescription("CEO role...");
		return role;
	}
	
}
