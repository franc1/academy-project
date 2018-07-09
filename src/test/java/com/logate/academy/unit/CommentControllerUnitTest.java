package com.logate.academy.unit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
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
import com.logate.academy.domains.Comment;
import com.logate.academy.domains.User;
import com.logate.academy.services.CommentService;
import com.logate.academy.services.UserService;
import com.logate.academy.web.controllers.CommentController;
import com.logate.academy.web.controllers.UserController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class CommentControllerUnitTest {
	
	private static final String BASE_API = "/api/comments";
	
	private MockMvc mockMvc; 
	
	@Mock
	private CommentService commentService;

	@InjectMocks
	private CommentController commentController;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(commentController)
				.build();
	}
	
	@Test
	public void deleteCommentByIdTest() throws Exception
	{
		Optional<Comment> mockedComment = prepareMockData();
		
		// when
		given(commentService.findById(3)).willReturn(mockedComment);
		
		// request
		mockMvc.perform(delete(BASE_API + "1/3")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk());
	}
	
	private Optional<Comment> prepareMockData()
	{
		Comment comment = new Comment();
		comment.setId(3);
		comment.setBody("Comment nr.1");
		comment.setPublishedAt(new Date());
		comment.setLikes(7);
		comment.setDislikes(1);
		
		return Optional.of(comment);
	}
	
	
	@Test
	public void updateLikesByIdTest() throws Exception
	{
		Comment mockedComment = prepareMockData1();
		
		// when
		given(commentService.updateLikes(7)).willReturn(mockedComment);
		
		// request
		MvcResult result = mockMvc.perform(put(BASE_API + "-like/7")
				.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andReturn();
		
		Comment responseComm = new ObjectMapper().readValue(
			result.getResponse().getContentAsString(), Comment.class);   //pretvara Json string u objekat
		
		// then
		assertThat(responseComm.getBody()).isEqualTo("Comment nr.2");
		assertThat(responseComm.getLikes()).isEqualTo(5);
	}
	
	private Comment prepareMockData1()
	{
		Comment comment = new Comment();
		comment.setId(7);
		comment.setBody("Comment nr.2");
		comment.setPublishedAt(new Date());
		comment.setLikes(5);
		comment.setDislikes(0);
		
		return comment;
	}
}
