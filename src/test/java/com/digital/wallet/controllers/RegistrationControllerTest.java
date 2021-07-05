package com.digital.wallet.controllers;

import com.digital.wallet.modelRequests.ConfirmationTokenAndAddPin;
import com.digital.wallet.modelRequests.RegisterRequest;
import com.digital.wallet.models.ConfirmationToken;
import com.digital.wallet.models.Customer;
import com.digital.wallet.models.Wallet;
import com.digital.wallet.repositories.ConfirmationTokenRepository;
import com.digital.wallet.repositories.CustomerRepository;
import com.digital.wallet.services.ConfirmationTokenService;
import com.digital.wallet.services.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc()
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private CustomerRepository customerRepo;
    @Autowired ConfirmationTokenRepository confirmationTokenRepo;


    Wallet mockWallet = new Wallet(0);
    Customer mockCustomer = new Customer("Rob",
            "Quincy", "quincy@gmail.com","quincy",
            Arrays.asList(mockWallet));

    @Test
    public void shouldRegisterNewUser() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/register")
                .content(asJsonString(new RegisterRequest("Rob",
                                "Carl", "carl@gmail.com", "robocall",
                                4567)
                ))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
    @Test
    public void shouldFailToRegisterNewUserWithExistingEmail() throws Exception {
        RegisterRequest mockReq = new RegisterRequest("Rob",
                "Carl", "quincy@gmail.com", "robocall",
                4567);
        customerRepo.save(mockCustomer);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/register")
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockReq))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }
    @Test
    public void shouldFailToRegisterNewUserWithMissingEmail() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void shouldConfirmRegisteredUser() throws Exception {
        Wallet mockWallet = new Wallet(0);
        Customer mockCustomer = new Customer("Rob",
                "Cat", "cat@gmail.com","quincy",
                Arrays.asList(mockWallet));

        customerRepo.save(mockCustomer);
        ConfirmationToken mockToken = new ConfirmationToken(LocalDateTime.now(),
                LocalDateTime.now().plusHours(10), mockCustomer);

        confirmationTokenService.save(mockToken);


        ConfirmationTokenAndAddPin mockReq = new ConfirmationTokenAndAddPin();
        mockReq.setToken(mockToken.getToken());
        mockReq.setPin(4567);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/registration/confirm")
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockReq))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

    @Test
    public void shouldFailToConfirmUserWithEmptyToken() throws Exception {
        ConfirmationTokenAndAddPin mockReq = new ConfirmationTokenAndAddPin();
        mockReq.setToken(" ");
        mockReq.setPin(4567);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/registration/confirm")
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockReq))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        System.out.println(response);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

    @Test
    public void shouldFailToConfirmUserWithInvalidToken() throws Exception {

        ConfirmationToken mockToken = new ConfirmationToken(LocalDateTime.now(),
                LocalDateTime.now(), mockCustomer);
        mockToken.setExpiresAt(LocalDateTime.of(2021,07,02,12,20 ));

        ConfirmationTokenAndAddPin mockReq = new ConfirmationTokenAndAddPin();
        mockReq.setToken(mockToken.getToken());
        mockReq.setPin(4567);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/registration/confirm")
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockReq))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        System.out.println(response);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }
    @Test
    public void shouldFailToConfirmUserWithWrongPin() throws Exception {
        ConfirmationTokenAndAddPin mockReq = new ConfirmationTokenAndAddPin();
        mockReq.setToken("1456");
        mockReq.setPin(45);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/registration/confirm")
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockReq))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        System.out.println(response);

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
