package com.example.demo.controller;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class TokenController {

    @Autowired
    private AmazonSNS amazonSNS;

    @Autowired
    private UserService userService;

    @Value("${aws.sns.topicArn}") // SNS Application ARN을 application.properties 파일에서 가져오기
    private String applicationArn;

    @PostMapping("/token")
    public ResponseEntity<User> registerToken(@RequestParam String customerId, @RequestParam String token) {
        // Create SNS Endpoint ARN
        CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest()
                .withPlatformApplicationArn(applicationArn)
                .withToken(token);

        CreatePlatformEndpointResult result = amazonSNS.createPlatformEndpoint(platformEndpointRequest);
        String endpointArn = result.getEndpointArn();

        // Save the user with the token and endpoint ARN
        User savedUser = userService.saveUser(customerId, token, endpointArn);

        return ResponseEntity.ok(savedUser);
    }
}