package naimaier.finances.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import naimaier.finances.model.Categoria;
import naimaier.finances.model.Despesa;
import naimaier.finances.model.Receita;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class ResumoControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	TestEntityManager em;
	

	@Test
	void shouldReturnOkAndCorrectValues() throws Exception {
		fillDb();
		
		URI uri = new URI("/resumo/2022/8");
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.isOk())
		.andReturn();
		
		String expectedResult = "{\"totalReceitas\":10000.00,\"totalDespesas\":4100.00,\"saldo\":5900.00,"
				+ "\"gastoTotalPorCategoria\":[{\"total\":1200.00,\"categoria\":\"Alimentação\"},"
				+ "{\"total\":1900.00,\"categoria\":\"Moradia\"},{\"total\":1000.00,\"categoria\":\"Transporte\"}]}";
		
		String jsonResponse = mvcResult
				.getResponse()
				.getContentAsString(StandardCharsets.UTF_8);
		
		JSONAssert.assertEquals(expectedResult, jsonResponse, JSONCompareMode.STRICT);
	}
	
	
	@Test
	void shouldReturnOkWhenSearchingMonthWithNoResults() throws Exception {
		
		URI uri = new URI("/resumo/2022/9");
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.isOk())
		.andReturn();
		
		String jsonResponse = mvcResult
				.getResponse()
				.getContentAsString(StandardCharsets.UTF_8);
		
		String expectedResult = "{\"totalReceitas\":0,\"totalDespesas\":0,\"saldo\":0,\"gastoTotalPorCategoria\":[]}";
		
		JSONAssert.assertEquals(expectedResult, jsonResponse, JSONCompareMode.STRICT);
	}
	
	
	@Test
	void shouldReturnBadRequestWhenSearchingInvalidMonth() throws Exception {
		
		URI uri = new URI("/resumo/2022/20");
		
		mockMvc.perform(MockMvcRequestBuilders
				.get(uri))
		.andExpect(MockMvcResultMatchers
				.status()
				.isBadRequest());
	}
	
	
	void fillDb() throws Exception {
		
		persistIncome("salario", BigDecimal.valueOf(10000), LocalDate.of(2022, 8, 1));
		persistExpense("mercado", BigDecimal.valueOf(700), LocalDate.of(2022, 8, 5), Categoria.ALIMENTAÇÃO);
		persistExpense("acougue", BigDecimal.valueOf(200), LocalDate.of(2022, 8, 6), Categoria.ALIMENTAÇÃO);
		persistExpense("restaurante", BigDecimal.valueOf(300), LocalDate.of(2022, 8, 31), Categoria.ALIMENTAÇÃO);
		persistExpense("combustivel", BigDecimal.valueOf(1000), LocalDate.of(2022, 8, 10), Categoria.TRANSPORTE);
		persistExpense("luz", BigDecimal.valueOf(300), LocalDate.of(2022, 8, 5), Categoria.MORADIA);
		persistExpense("aluguel", BigDecimal.valueOf(1500), LocalDate.of(2022, 8, 5), Categoria.MORADIA);
		persistExpense("internet", BigDecimal.valueOf(100), LocalDate.of(2022, 8, 5), Categoria.MORADIA);
	}
	
	void persistIncome(String descricao, BigDecimal valor, LocalDate data) {
		Receita receita = new Receita();
		receita.setDescricao(descricao);
		receita.setValor(valor);
		receita.setData(data);
		
		em.persist(receita);
	}
	
	void persistExpense(String descricao, BigDecimal valor, LocalDate data, Categoria categoria) {
		Despesa despesa = new Despesa();
		despesa.setDescricao(descricao);
		despesa.setValor(valor);
		despesa.setData(data);
		despesa.setCategoria(categoria);
		
		em.persist(despesa);
	}

}
