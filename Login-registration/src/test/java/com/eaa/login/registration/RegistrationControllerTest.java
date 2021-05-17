package com.eaa.login.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegistrationControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@MockBean
	private RegistrationService registrationService;

	ObjectMapper om = new ObjectMapper();

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

	}

	@WithMockUser("EAA")
	@Test
	public void validRegisterTest() throws Exception {
		RegistrationRequest req = new RegistrationRequest();
		req.setFirstName("Elias");
		req.setLastName("Abi atallah");
		req.setEmail("eabiatallah@gmail.com");
		req.setPassword("pass");
		req.setConfirmPassword("pass");

		// This is the response from service.
		when(registrationService.register(Mockito.any(RegistrationRequest.class))).thenReturn(new RegistrationResponse("Valid", Boolean.TRUE));

		// String jsonRequest = om.writeValueAsString(req);

		MvcResult result = mockMvc.perform(post("/api/v1/registration/register").content(om.writeValueAsString(req))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();

		String resultContent = result.getResponse().getContentAsString();
		RegistrationResponse response = om.readValue(resultContent, RegistrationResponse.class);
		Assert.assertTrue(response.isStatus() == Boolean.TRUE);
		 
		assertThat(response.getMessage()).contains("token");
	}

	@WithMockUser("EAA")
	@Test
	public void invalidRegisterTest() throws Exception {

		RegistrationRequest req = new RegistrationRequest();
		req.setFirstName("Elias");
		req.setLastName("Abi atallah");
		req.setEmail("eabiatallah");
		req.setPassword("pass");
		req.setConfirmPassword("passxs");

		this.mockMvc.perform(post("/api/v1/registration/register").contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(req))).andExpect(status().is5xxServerError());

	}

	@WithMockUser("EAA")
	@Test
	public void testConfirm() throws Exception {

		// This is the response from service.
		when(registrationService.confirmToken(Mockito.anyString()))
				.thenReturn(new RegistrationResponse("Valid", Boolean.TRUE));

		String token = "validToken";

		MvcResult result = mockMvc.perform(get("/api/v1/registration/confirm/tokenValue")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(om.writeValueAsString(token)))
				.andExpect(status().isOk()).andReturn();

		String resultContent = result.getResponse().getContentAsString();
		RegistrationResponse response = om.readValue(resultContent, RegistrationResponse.class);
		Assert.assertTrue(response.isStatus() == Boolean.TRUE);

	}

	@WithMockUser("EAA")
	@Test
	public void testInvalidConfirm() throws Exception {

		// This is the response from service.
		when(registrationService.confirmToken(Mockito.anyString()))
				.thenReturn(new RegistrationResponse("Valid", Boolean.FALSE));

		String token = "invalidToken";

		MvcResult result = mockMvc.perform(get("/api/v1/registration/confirm/tokenValue")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(om.writeValueAsString(token)))
				.andExpect(status().isOk()).andReturn();

		String resultContent = result.getResponse().getContentAsString();
		RegistrationResponse response = om.readValue(resultContent, RegistrationResponse.class);
		Assert.assertTrue(response.isStatus() == Boolean.FALSE);

	}

}
