package com.example.demo.controller;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
    @Tag(name = "TokenController", description = "사용자가 로그인 요청 시 FCM Token -> SNS Endpoint ARN 발급 후 사용자 정보 저장")
    public ResponseEntity<User> registerToken(@RequestParam String customerId, @RequestParam String token) {
        // Create SNS Endpoint ARN
        CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest()
                .withPlatformApplicationArn(applicationArn)
                .withToken(token);

        CreatePlatformEndpointResult result = amazonSNS.createPlatformEndpoint(platformEndpointRequest);
        String endpointArn = result.getEndpointArn();

        // Check if user already exists
        Optional<User> existingUser = userService.findByCustomerId(customerId);

        if (existingUser.isPresent()) {
            // If user exists, update the token and endpoint ARN
            userService.updateExistingUser(existingUser.get(), token, endpointArn);
        } else {
            // If user does not exist, save a new user
            userService.saveUser(customerId, token, endpointArn);
        }

        return ResponseEntity.ok(existingUser.orElseGet(() -> new User(null, customerId, token, endpointArn)));
    }

}