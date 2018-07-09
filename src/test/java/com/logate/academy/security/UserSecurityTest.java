package com.logate.academy.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.logate.academy.web.controllers.ArticleController;
import com.logate.academy.web.controllers.UserController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserSecurityTest {   // NER RADE !!!
	
	private static final String BASE_API = "/api/users";
	
	private MockMvc mockMvc;
	
	@MockBean
	private UserController userController;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(userController)
				.build();
	}
	
	
	@Test
	@WithMockUser(username = "heriiiii", roles = {"USER"})   // NE RADI???
	public void getByIdUserRegularTest() throws Exception
	{
		mockMvc.perform(get(BASE_API + "/" + "1")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void getByIdUserUnauthorizedTest() throws Exception
	{
		mockMvc.perform(get(BASE_API + "/" + "1")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void getAllUsersUnAuthorizedTest() throws Exception
	{
		mockMvc.perform(get(BASE_API)
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	@WithMockUser(username = "fran", roles = {"MANAGER"})   // NE RADI???
	public void getAllArticles() throws Exception
	{
		mockMvc.perform(get("/api/articles")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isForbidden());
	}
	
}
