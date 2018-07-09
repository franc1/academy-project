package com.logate.academy.unit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logate.academy.domains.User;
import com.logate.academy.services.UserService;
import com.logate.academy.web.controllers.UserController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerUnitTest {
	
	private static final String BASE_API = "/api/users";
	
	private MockMvc mockMvc;     // NE anotira se sa @Autowired!
	
	@Mock
	private UserService userService;

	@InjectMocks
	private UserController userController;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(userController)
				.build();
	}
	
	@Test
	public void getUserByIdTest() throws Exception
	{
		Optional<User> mockedUser = prepareMockData();
		
		// when
		given(userService.getUserById(2)).willReturn(mockedUser);
		
		// request
		MvcResult result = mockMvc.perform(get(BASE_API + "/2")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andReturn();
		
		User responseUsr = new ObjectMapper().readValue(
			result.getResponse().getContentAsString(), User.class);   //pretvara Json string u objekat
		
		// then
		assertThat(responseUsr.getFirstName()).isEqualTo("Heril");
		assertThat(responseUsr.getIsActive()).isEqualTo(true);
	}
	
	private Optional<User> prepareMockData()
	{
		User user = new User();
		user.setId(2);
		user.setFirstName("Heril");
		user.setLastName("Muratovic");
		user.setIsActive(true);
		user.setEmail("heril.muratovic@logate.com");
		
		return Optional.of(user);
	}
}
