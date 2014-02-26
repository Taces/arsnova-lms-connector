package de.thm.arsnova.connector.web;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import de.thm.arsnova.connector.model.IliasCategoryNode;
import de.thm.arsnova.connector.model.IliasQuestion;
import de.thm.arsnova.connector.services.UniRepService;

public class UniRepControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private UniRepController controller;

	@Mock
	private UniRepService service;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = standaloneSetup(controller).build();

		when(service.getTreeObjects(anyInt())).thenReturn(new ArrayList<IliasCategoryNode>());
		when(service.getQuestions(anyInt())).thenReturn(new ArrayList<IliasQuestion>());
	}

	@Test
	public void testShouldReturnTreeData() throws Exception {
		mockMvc.perform(get("/ilias/123").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	public void testShouldReturnQuestions() throws Exception {
		mockMvc.perform(get("/ilias/question/123").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
}