package com.example.demo.controller;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.example.demo.dto.MessageDto;
import com.example.demo.dto.ReceiptHandleDto;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.SqsService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final String topicArn = "arn:aws:sns:eu-central-1:967724518132:pickup"; // SNS Topic ARN

    @Autowired
    private OrderRepository orderRepository;

    public MessageController(SqsService sqsReceiverService) {
        this.sqsReceiverService = sqsReceiverService;
        this.snsClient = AmazonSNSClientBuilder.defaultClient();
    }

    @GetMapping
    public List<MessageDto> getMessages() {
        return sqsReceiverService.receiveMessages();
    }

    @PostMapping("/complete")
    public ResponseEntity<Map<String, String>> deleteMessage(@RequestBody ReceiptHandleDto receiptHandleDto) {
        sqsReceiverService.deleteMessage(receiptHandleDto.getReceiptHandle());

        String messageId = publishSNSPickup();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Message deleted and published to SNS topic successfully");
        response.put("snsMessageId", messageId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String publishSNSPickup() {
        // SNS로 메시지 전송
        String messageText = "Message deleted successfully from SQS";

        PublishRequest publishRequest = new PublishRequest()
                .withMessage(messageText)
                .withTopicArn(topicArn);

        // 메시지 발행 후 PublishResult 객체 받기
        PublishResult publishResult = snsClient.publish(publishRequest);

        // 메시지 ID 반환
        return publishResult.getMessageId();
    }
}
