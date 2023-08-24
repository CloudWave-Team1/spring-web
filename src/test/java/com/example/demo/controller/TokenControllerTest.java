package com.example.demo.controller;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TokenControllerTest {

    @InjectMocks
    private TokenController tokenController;

    @Mock
    private AmazonSNS amazonSNS;

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(tokenController).build();
    }

    @Test
    public void registerToken_NewUser() throws Exception {
        String customerId = "testCustomerId";
        String token = "testToken";
        String endpointArn = "testEndpointArn";

        when(amazonSNS.createPlatformEndpoint(any())).thenReturn(new CreatePlatformEndpointResult().withEndpointArn(endpointArn));
        when(userService.findByCustomerId(customerId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/register/token")
                        .param("customerId", customerId)
                        .param("token", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).saveUser(customerId, token, endpointArn);
        verify(userService, never()).updateExistingUser(any(), any(), any());
    }

    @Test
    public void registerToken_ExistingUser() throws Exception {
        String customerId = "testCustomerId";
        String token = "testToken";
        String endpointArn = "testEndpointArn";

        User existingUser = new User();
        existingUser.setCustomerId(customerId);

        when(amazonSNS.createPlatformEndpoint(any())).thenReturn(new CreatePlatformEndpointResult().withEndpointArn(endpointArn));
        when(userService.findByCustomerId(customerId)).thenReturn(Optional.of(existingUser));

        mockMvc.perform(MockMvcRequestBuilders.post("/register/token")
                        .param("customerId", customerId)
                        .param("token", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, never()).saveUser(customerId, token, endpointArn);
        verify(userService, times(1)).updateExistingUser(existingUser, token, endpointArn);
    }
}
