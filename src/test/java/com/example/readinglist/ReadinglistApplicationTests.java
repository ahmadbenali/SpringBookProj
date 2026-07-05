package com.example.readinglist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReadinglistApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void testReadingListEmptyFlow() throws Exception {
		mockMvc.perform(get("/craig"))
				.andExpect(status().isOk())
				.andExpect(view().name("readinglist"))
				.andExpect(model().attribute("reader", "craig"))
				.andExpect(model().attribute("books", hasSize(0)));
	}

	@Test
	void testAddBookAndRetrieveFlow() throws Exception {
		mockMvc.perform(post("/craig")
						.param("title", "Spring Boot in Action")
						.param("author", "Craig Walls")
						.param("isbn", "9781617292545")
						.param("description", "A great guide to Spring Boot"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/craig"));

		mockMvc.perform(get("/craig"))
				.andExpect(status().isOk())
				.andExpect(view().name("readinglist"))
				.andExpect(model().attribute("reader", "craig"))
				.andExpect(model().attribute("books", hasSize(greaterThanOrEqualTo(1))))
				.andExpect(model().attribute("books", hasItem(
						allOf(
								hasProperty("title", is("Spring Boot in Action")),
								hasProperty("author", is("Craig Walls")),
								hasProperty("isbn", is("9781617292545")),
								hasProperty("description", is("A great guide to Spring Boot"))
						)
				)));
	}
}
