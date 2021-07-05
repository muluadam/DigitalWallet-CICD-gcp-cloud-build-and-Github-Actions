package com.digital.wallet.controllers;

import com.digital.wallet.modelRequests.ConfirmationTokenAndAddPin;
import com.digital.wallet.modelRequests.LoginRequest;
import com.digital.wallet.modelRequests.TransferMoneyRequest;
import com.digital.wallet.models.Card;
import com.digital.wallet.models.ConfirmationToken;
import com.digital.wallet.models.Customer;
import com.digital.wallet.models.Wallet;
import com.digital.wallet.repositories.CardRepository;
import com.digital.wallet.repositories.ConfirmationTokenRepository;
import com.digital.wallet.repositories.CustomerRepository;
import com.digital.wallet.repositories.WalletRepository;
import com.digital.wallet.services.ConfirmationTokenService;
import com.digital.wallet.services.WalletService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WalletService walletService;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    ConfirmationTokenService confirmationTokenService;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private CardRepository cardRepo;

//    @Autowired
//    private WalletRepository walletRepo;

    String tokenResponse;
    Long walletId;
    Customer mockCustomer;
    Card mockInvalidCard;
    long receiverTag;
    long senderTag;
    Wallet mockWallet;

    @BeforeAll
    public void setUp() throws Exception{
        mockWallet = new Wallet(0);
        mockCustomer = new Customer("salah",
                "moh", "mohsala@gmail.com","235478095",
                Arrays.asList(mockWallet));
        mockCustomer.setEnabled(true);
        mockCustomer.setCustomerPin(7645);
        mockWallet.setWalletHolder(mockCustomer);
        customerRepo.save(mockCustomer);

        LoginRequest mockLoginAuth  = new LoginRequest("mohsala@gmail.com","235478095");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/login")
                .content(asJsonString(mockLoginAuth))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse loginResponse = result.getResponse();
        String jsonString = loginResponse.getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(jsonString);
        tokenResponse = actualObj.get("token").textValue();

        //get walletId
        walletId = mockCustomer.getWallets().get(0).getWalletId();
        senderTag = mockCustomer.getWallets().get(0).getTag();

        //mock receiver
        Wallet mockReceiverWallet = new Wallet(0);
        Customer mockReceiverCustomer = new Customer("euro",
                "fifa", "eurofifa@gmail.com","235478010",
                Arrays.asList(mockReceiverWallet));
        mockReceiverCustomer.setEnabled(true);
        mockReceiverCustomer.setCustomerPin(7609);
        mockReceiverWallet.setWalletHolder(mockReceiverCustomer);
        customerRepo.save(mockReceiverCustomer);

        // mock receiver tag
        receiverTag = mockReceiverCustomer.getWallets().get(0).getTag();

    }

    @Test
    public void shouldFailIfWalletIsNotCustomers() throws Exception {
        int walletId2 = 1;
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("http://localhost:9090/api/v1/wallet/"+walletId2+"/transactions")
                .header("Authorization", "Bearer " + tokenResponse)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldDisplayTransactions() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("http://localhost:9090/api/v1/wallet/"+walletId+"/transactions")
                .header("Authorization", "Bearer " + tokenResponse)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void shouldFailToDisplayTransactionsForUnauthorizedUser() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("http://localhost:9090/api/v1/wallet/"+walletId+"/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldFailToTopUpForUnauthorizedUser() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/wallet/"+walletId+"/topUp")
                .contentType(MediaType.APPLICATION_JSON)
                .param("amount", "2000.0")
                .param("pin", "7645")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldFailToTopUpWithInvalidAmount() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/wallet/"+walletId+"/topUp")
                .header("Authorization", "Bearer " + tokenResponse)
                .param("amount", "0.0")
                .param("pin", "7645")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldFailToTopUpWithInvalidPin() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/wallet/"+walletId+"/topUp")
                .header("Authorization", "Bearer " + tokenResponse)
                .param("amount", "3000.0")
                .param("pin", "7612")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldFailToTopUpIfWalletIsNotCustomers() throws Exception {
        int walletId2 = 1;
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/wallet/"+walletId2+"/topUp")
                .header("Authorization", "Bearer " + tokenResponse)
                .param("amount", "3000.0")
                .param("pin", "7645")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldFailToTopUpIfNoCardAdded() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/wallet/"+walletId+"/topUp")
                .header("Authorization", "Bearer " + tokenResponse)
                .param("amount", "3000.0")
                .param("pin", "7645")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldFailToTopUpWithInvalidCard() throws Exception {
        mockInvalidCard = new Card(1234150899, 289, LocalDate.of(2038, 2, 8));
        mockInvalidCard.setCardHolder(mockCustomer);
        cardRepo.save(mockInvalidCard);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/wallet/"+walletId+"/topUp")
                .header("Authorization", "Bearer " + tokenResponse)
                .param("amount", "3000.0")
                .param("pin", "7645")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldTopUpWithValidCard() throws Exception {
        cardRepo.delete(mockInvalidCard);
        Card mockCard = new Card(4299350987676775L, 321, LocalDate.of(2032, 2, 8));
        mockCard.setCardHolder(mockCustomer);
        cardRepo.save(mockCard);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/wallet/"+walletId+"/topUp")
                .header("Authorization", "Bearer " + tokenResponse)
                .param("amount", "3000.0")
                .param("pin", "7645")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void shouldFailToTransferWithInvalidPin() throws Exception {
        TransferMoneyRequest mockTransfer = new TransferMoneyRequest(200.0F, receiverTag, "transfer", 7000);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/wallet/"+walletId+"/transfer")
                .header("Authorization", "Bearer " + tokenResponse)
                .content(asJsonString(mockTransfer))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldFailToTransferWithoutToken() throws Exception {
        TransferMoneyRequest mockTransfer = new TransferMoneyRequest(200.0F, receiverTag, "transfer", 7645);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/wallet/"+walletId+"/transfer")
                .content(asJsonString(mockTransfer))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldFailToTransferWithInvalidAmount() throws Exception {
        TransferMoneyRequest mockTransfer = new TransferMoneyRequest(0.0F, receiverTag, "transfer", 7645);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/wallet/"+walletId+"/transfer")
                .header("Authorization", "Bearer " + tokenResponse)
                .content(asJsonString(mockTransfer))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldFailToTransferWithInvalidTag() throws Exception {
        long invalidTag = 67854;
        TransferMoneyRequest mockTransfer = new TransferMoneyRequest(30.0F, invalidTag, "transfer", 7645);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/wallet/"+walletId+"/transfer")
                .header("Authorization", "Bearer " + tokenResponse)
                .content(asJsonString(mockTransfer))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldFailToTransferWithPersonalTag() throws Exception {
        TransferMoneyRequest mockTransfer = new TransferMoneyRequest(30.0F, senderTag, "transfer", 7645);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/wallet/"+walletId+"/transfer")
                .header("Authorization", "Bearer " + tokenResponse)
                .content(asJsonString(mockTransfer))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldFailToTransferIfMoneyIsNotEnough() throws Exception {
        mockWallet.setAmount(30);
        customerRepo.save(mockCustomer);
        TransferMoneyRequest mockTransfer = new TransferMoneyRequest(300.0F, receiverTag, "transfer", 7645);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/wallet/"+walletId+"/transfer")
                .header("Authorization", "Bearer " + tokenResponse)
                .content(asJsonString(mockTransfer))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void shouldTransferMoneySuccessfully() throws Exception {
        mockWallet.setAmount(3000.0F);
        customerRepo.save(mockCustomer);
        TransferMoneyRequest mockTransfer = new TransferMoneyRequest(300.0F, receiverTag, "transfer", 7645);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/v1/wallet/"+walletId+"/transfer")
                .header("Authorization", "Bearer " + tokenResponse)
                .content(asJsonString(mockTransfer))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
