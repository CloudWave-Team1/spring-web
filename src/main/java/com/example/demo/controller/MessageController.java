package com.example.demo.controller;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.example.demo.dto.MessageDto;
import com.example.demo.dto.ReceiptHandleDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.SqsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final SqsService sqsReceiverService;
    private final AmazonSNS snsClient;

    @Value("${aws.sns.topicArn}")
    private String topicArn;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public MessageController(SqsService sqsReceiverService) {
        this.sqsReceiverService = sqsReceiverService;
        this.snsClient = AmazonSNSClientBuilder.defaultClient();
    }


    @GetMapping
    public List<MessageDto> getMessages() {
        return sqsReceiverService.receiveMessages();
    }

    @PostMapping("/completeAndNotify")
    public ResponseEntity<Map<String, String>> completeAndNotify(@RequestParam String customerId, @RequestBody ReceiptHandleDto receiptHandleDto) {
        // 1. 사용자에게 알림
        String endpointArn = userRepository.findEndpointArnByCustomerId(customerId);

        if (endpointArn == null || endpointArn.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Endpoint ARN not found for the user."));
        }

        String messageText = "Your custom message text here";
        PublishRequest publishRequest = new PublishRequest()
                .withMessage(messageText)
                .withTargetArn(endpointArn);

        PublishResult publishResult;
        try {
            publishResult = snsClient.publish(publishRequest);
        } catch(Exception e) {
            // SNS 전송 중 오류 발생 시
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to send notification."));
        }

        // 2. 메시지 삭제
        sqsReceiverService.deleteMessage(receiptHandleDto.getReceiptHandle());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Notification sent successfully and message deleted");
        response.put("snsMessageId", publishResult.getMessageId());

        return ResponseEntity.ok().body(response);
    }


//    @PostMapping("/complete")
//    public ResponseEntity<Map<String, String>> deleteMessage(@RequestBody ReceiptHandleDto receiptHandleDto) {
//        sqsReceiverService.deleteMessage(receiptHandleDto.getReceiptHandle());
//
//        String messageId = publishSNSPickup();
//
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "Message deleted and published to SNS topic successfully");
//        response.put("snsMessageId", messageId);
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    private String publishSNSPickup() {
//        // SNS로 메시지 전송
//        String messageText = "Message deleted successfully from SQS";
//
//        PublishRequest publishRequest = new PublishRequest()
//                .withMessage(messageText)
//                .withTopicArn(topicArn);
//
//        // 메시지 발행 후 PublishResult 객체 받기
//        PublishResult publishResult = snsClient.publish(publishRequest);
//
//        // 메시지 ID 반환
//        return publishResult.getMessageId();
//    }


}
