package com.plateandpic.controller;

import static org.assertj.core.api.Assertions.assertThat;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plateandpic.dao.CityDao;
import com.plateandpic.models.City;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CityController.class, secure = false)
public class CityControllerTest {
	
	@Autowired
	MockMvc mock;
	
	@MockBean
	CityDao cityDao;
	
	private City city;
	private String cityJson;
	
	@Before
	public void before() throws JsonProcessingException{
		
		city = new City();
		city.setCityId(1);
		city.setName("Madrid");
		city.setProvince(null);
		
		ObjectMapper mapper = new ObjectMapper();
		cityJson = mapper.writeValueAsString(city);
		
	}
	
	@Test
	public void getCityByNameTest() throws Exception{
		
		Mockito.when(cityDao.findByNameIgnoreCase(Mockito.anyString())).thenReturn(city);
		
		RequestBuilder rb = MockMvcRequestBuilders
				.get("/city/getCityByName?name=Madrid")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult res = mock.perform(rb).andReturn();
		
		assertThat(res.getResponse().getStatus()).isEqualTo(HttpServletResponse.SC_OK);
		assertThat(res.getResponse().getContentAsString()).isEqualTo(cityJson);
		
	}

}
