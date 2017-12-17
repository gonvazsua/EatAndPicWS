package com.plateandpic.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import javax.servlet.http.HttpServletResponse;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.plateandpic.dao.CommentDao;
//import com.plateandpic.exceptions.CommentException;
//import com.plateandpic.factory.CommentFactory;
//import com.plateandpic.models.Comment;
//import com.plateandpic.models.PlatePicture;
//import com.plateandpic.models.User;
//import com.plateandpic.response.CommentRequestResponse;
//
//@RunWith(SpringRunner.class)
//@WebMvcTest(value = CommentController.class, secure = false)
public class CommentControllerTest {
	
//	@Autowired
//	MockMvc mock;
//	
//	@MockBean
//	CommentFactory commentFactory;
//	
//	private List<CommentRequestResponse> comments;
//	private CommentRequestResponse commentRes;
//	private String commentsJson;
//	private String commentJson;
//	
//	@Before
//	public void before() throws JsonProcessingException{
//		
//		comments = new ArrayList<CommentRequestResponse>();
//		Comment comment = new Comment();
//		comment.setUser(new User());
//		commentRes = new CommentRequestResponse(comment);
//		comments.add(commentRes);
//		
//		ObjectMapper mapper = new ObjectMapper();
//		commentsJson = mapper.writeValueAsString(comments);
//		
//		commentJson = mapper.writeValueAsString(comment);
//		
//	}
//	
//	@Test
//	public void getByPlatePictureTest() throws Exception {
//		
//		Mockito.when(commentFactory.getCommentsByPlatePictureId(Mockito.anyLong(), Mockito.any())).thenReturn(comments);
//		
//		RequestBuilder rb = MockMvcRequestBuilders
//				.get("/comment/getByPlatePicture?platePictureId=1&page=1")
//				.accept(MediaType.APPLICATION_JSON);
//		
//		MvcResult res = mock.perform(rb).andReturn();
//		
//		assertThat(res.getResponse().getStatus()).isEqualTo(HttpServletResponse.SC_OK);
//		assertThat(res.getResponse().getContentAsString()).isEqualTo(commentsJson);
//		
//	}
//	
//	@Test
//	public void saveTest() throws Exception {
//		
////		Mockito.when(commentFactory.validateAndSave(Mockito.anyString(), Mockito.any(Comment.class)))
////			.thenReturn(commentRes);
////		
////		RequestBuilder rb = MockMvcRequestBuilders
////				.post("/comment/save")
////				.accept(MediaType.APPLICATION_JSON)
////				.content(commentJson)
////				.contentType(MediaType.APPLICATION_JSON);
////		
////		MvcResult res = mock.perform(rb).andReturn();
////		
////		assertThat(res.getResponse().getStatus()).isEqualTo(HttpServletResponse.SC_OK);
//		
//	}

}
