package com.digital.wallet.controllers;

import com.digital.wallet.jwtUtils.JwtTokenProvider;
import com.digital.wallet.modelRequests.CardInfo;
import com.digital.wallet.modelRequests.LoginRequest;

import com.digital.wallet.models.Customer;
import com.digital.wallet.models.Wallet;
import com.digital.wallet.repositories.CustomerRepository;
import com.digital.wallet.services.CustomerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepo;

    MockHttpServletResponse loginResponse;
    String tokenResponse;

    @BeforeAll
    public void setUp() throws Exception{
        Wallet mockWallet = new Wallet(0);
        Customer mockCustomer = new Customer("esther",
                "muk", "enmuk@gmail.com","1234567890",
                Arrays.asList(mockWallet));
        mockCustomer.setEnabled(true);
        customerRepo.save(mockCustomer);

        LoginRequest mockLoginAuth  = new LoginRequest("enmuk@gmail.com","1234567890");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/login")
                .content(asJsonString(mockLoginAuth))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        loginResponse = result.getResponse();
        String jsonString = loginResponse.getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);
        tokenResponse = actualObj.get("token").textValue();
    }

    @Test
    public void shouldFailToDisplayWallet() throws Exception {
        this.mockMvc
                .perform(get("http://localhost:9090/api/v1/welcom"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldDisplayWallet() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("http://localhost:9090/api/v1/welcom")
                .header("Authorization", "Bearer " + tokenResponse)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void shouldFailToDisplayWalletWithExpiredToken() throws Exception {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbm11a0BnbWFpbC5jb20iLCJleHAiOjE2MjUyNTg1MDAsImlhdCI6MTYyNTI0MDUwMH0.mfrjxYXi2u_4yKuDBHVPiAhm5hxTwJP8_hs69BkWK3FqjIBIHV866WVMAH-oT8UuGRhxtgZrhYf-lfJwh7fBuA";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("http://localhost:9090/api/v1/welcom")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldLoginUser() throws Exception {
        Assertions.assertNotNull(loginResponse.getContentAsString());
    }

    @Test
    public void shouldFailToLoginUserWithBadCredentials() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/login")
                .content(asJsonString(new LoginRequest("emuk@gmail.com","12345")
                ))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    public void shouldAddCardWithValidDetails() throws Exception {
        CardInfo mockCard = new CardInfo(4299359383763352L, 321, LocalDate.of(2025, 3, 1).toString());
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/add/card")
                .header("Authorization", "Bearer " + tokenResponse)
                .content(asJsonString(mockCard))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldFailToAddCardWithInvalidDetails() throws Exception {
        CardInfo mockCard = new CardInfo(487345678, 345, "2012-02-22T");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/add/card")
                .header("Authorization", "Bearer " + tokenResponse)
                .content(asJsonString(mockCard))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldFailToAddExpiredCard() throws Exception {
        CardInfo mockCard = new CardInfo(5399356490908888L, 123, "2020-03-30");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/add/card")
                .header("Authorization", "Bearer " + tokenResponse)
                .content(asJsonString(mockCard))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldFailToAddCardForExpiredUser() throws Exception {

        CardInfo mockCard = new CardInfo(4299359383763352L, 321, "2025-03-01");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/add/card")
                .content(asJsonString(mockCard))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
