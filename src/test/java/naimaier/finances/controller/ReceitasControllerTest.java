package naimaier.finances.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class ReceitasControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	String jsonElement1 = "{\"descricao\":\"teste\",\"valor\":\"80\",\"data\":\"20/08/2022\"}";
	String jsonExpectedElement1 = "{\"id\":1,\"descricao\":\"teste\",\"valor\":80.00,\"data\":\"20/08/2022\"}";
	
	String jsonElement2 = "{\"descricao\":\"teste\",\"valor\":\"80\",\"data\":\"20/09/2022\"}";
	String jsonExpectedElement2 = "{\"id\":2,\"descricao\":\"teste\",\"valor\":80.00,\"data\":\"20/09/2022\"}";
	
	String jsonElement3 = "{\"descricao\":\"teste3\",\"valor\":\"80\",\"data\":\"23/09/2022\"}";
	String jsonExpectedElement3 = "{\"id\":3,\"descricao\":\"teste3\",\"valor\":80.00,\"data\":\"23/09/2022\"}";
	
	String jsonExpectedAllElements = "[" + jsonExpectedElement1 + "," + jsonExpectedElement2 + "," + jsonExpectedElement3 + "]";
	String jsonExpectedSeptemberElements = "[" + jsonExpectedElement2 + "," + jsonExpectedElement3 + "]";
	
	
	@Test
	@Order(1)
	void shouldReturnCreatedOnValidPost() throws Exception {
		
		URI uri = new URI("/receitas");
		
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(jsonElement1)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.isCreated());
		
		
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(jsonElement2)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.isCreated());
		
		
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(jsonElement3)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.isCreated());
	}
	
	
	@Test
	@Order(2)
	void shouldReturnBadRequestOnDuplicatePost() throws Exception {
		
		URI uri = new URI("/receitas");
		
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(jsonElement1)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.isBadRequest());
	}
	
	
	@Test
	@Order(3)
	void shouldReturnBadRequestWhenPostMissingOneAttribute() throws Exception {
		
		URI uri = new URI("/receitas");
		String json = "{\"descricao\":\"teste\",\"valor\":\"80\"}";
		
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.isBadRequest());
	}
	
	
	@Test
	@Order(4)
	void shouldReturnBadRequestWhenPostWithInvalidDate() throws Exception {
		
		URI uri = new URI("/receitas");
		String json = "{\"descricao\":\"teste\",\"valor\":\"80\",\"data\":\"20/15/2022\"}";
		
		mockMvc.perform(MockMvcRequestBuilders
				.post(uri)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.isBadRequest());
	}
	
	
	@Test
	@Order(5)
	void shouldReturnOkWhenSearchingAllElements() throws Exception {
		
		URI uri = new URI("/receitas");
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.isOk())
		.andReturn();
		
		String jsonResponse = mvcResult.getResponse().getContentAsString();
		
		assertEquals(jsonExpectedAllElements, jsonResponse);
	}
	
	
	@Test
	@Order(6)
	void shouldReturnOkWhenSearchingElementsByDescription() throws Exception {
		
		URI uri = new URI("/receitas?descricao=teste3");
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.isOk())
		.andReturn();
		
		String jsonResponse = mvcResult.getResponse().getContentAsString();
		
		assertEquals("[" + jsonExpectedElement3 + "]", jsonResponse);
	}
	
	
	@Test
	@Order(7)
	void shouldReturnOkWhenSearchingInexistentElementsByDescription() throws Exception {
		
		URI uri = new URI("/receitas?descricao=notExistent");
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.isOk())
		.andReturn();
		
		String jsonResponse = mvcResult.getResponse().getContentAsString();
		
		assertEquals("[]", jsonResponse);
	}
	
	
	@Test
	@Order(8)
	void shouldReturnOkWhenSearchingOneElement() throws Exception {
		
		// Element 1
		
		URI uri1 = new URI("/receitas/1");
		
		MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders
				.get(uri1))
		.andExpect(MockMvcResultMatchers
				.status()
				.isOk())
		.andReturn();
		
		String jsonResponse1 = mvcResult1.getResponse().getContentAsString();
		
		assertEquals(jsonExpectedElement1, jsonResponse1);
		
		// Element 2
		
		URI uri2 = new URI("/receitas/2");
		
		MvcResult mvcResult2 = mockMvc.perform(MockMvcRequestBuilders
				.get(uri2))
		.andExpect(MockMvcResultMatchers
				.status()
				.isOk())
		.andReturn();
		
		String jsonResponse2 = mvcResult2.getResponse().getContentAsString();
		
		assertEquals(jsonExpectedElement2, jsonResponse2);
	}
	
	
	
	@Test
	@Order(9)
	void shouldReturnNotFoundWhenSearchingInexistestElement() throws Exception {
		
		URI uri = new URI("/receitas/4");
		
		mockMvc.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.isNotFound());
	}
	
	
	@Test
	@Order(10)
	void shouldReturnBadRequestOnSearchingByWrongMonth() throws Exception {
		
		URI uri = new URI("/receitas/2022/15");
		
		mockMvc.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.isBadRequest());
	}
	
	
	@Test
	@Order(11)
	void shouldReturnOkWhenSearchingMonthWithResults() throws Exception {
		
		URI uri = new URI("/receitas/2022/09");
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.isOk())
		.andReturn();
		
		String jsonResponse = mvcResult.getResponse().getContentAsString();
		
		assertEquals(jsonExpectedSeptemberElements, jsonResponse);
	}
	
	
	@Test
	@Order(12)
	void shouldReturnNotFoundWhenUpdatingWithInexistentId() throws Exception {
		
		URI uri = new URI("/receitas/4");
		
		mockMvc.perform(MockMvcRequestBuilders
				.put(uri)
				.content(jsonElement3)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.isNotFound());
	}
	
	
	@Test
	@Order(13)
	void shouldReturnBadRequestWhenDuplicatingElementOnUpdate() throws Exception {
		
		URI uri = new URI("/receitas/3");
		
		mockMvc.perform(MockMvcRequestBuilders
				.put(uri)
				.content(jsonElement2)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.isBadRequest());
	}
	
	
	@Test
	@Order(14)
	void shouldReturnOkOnValidUpdate() throws Exception {
		
		URI uri = new URI("/receitas/3");
		
		mockMvc.perform(MockMvcRequestBuilders
				.put(uri)
				.content(jsonElement3)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.isOk());
	}
	
	
	@Test
	@Order(15)
	void shouldReturnBadRequestWhenUpdatingWithInvalidDate() throws Exception {
		
		URI uri = new URI("/receitas/3");
		String json = "{\"descricao\":\"teste\",\"valor\":\"80\",\"data\":\"20/15/2022\"}";
		
		mockMvc.perform(MockMvcRequestBuilders
				.put(uri)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers
				.status()
				.isBadRequest());
	}
	
	
	@Test
	@Order(16)
	void shouldReturnNotFoundWhenTryingToDeleteInexistentId() throws Exception {
		
		URI uri = new URI("/receitas/4");
		
		mockMvc.perform(MockMvcRequestBuilders
				.delete(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.isNotFound());
	}
	
	
	@Test
	@Order(17)
	void shouldReturnOkOnDeleteValidId() throws Exception {
		
		URI uri = new URI("/receitas/3");
		
		mockMvc.perform(MockMvcRequestBuilders
				.delete(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.isOk());
	}
}
