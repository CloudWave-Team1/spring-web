package com.example.demo.controller;

import com.example.demo.dto.MessageDto;
import com.example.demo.service.SqsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final SqsService sqsReceiverService;

    public MessageController(SqsService sqsReceiverService) {
        this.sqsReceiverService = sqsReceiverService;
    }

    @GetMapping
    public List<MessageDto> getMessages() {
        return sqsReceiverService.receiveMessages();
    }

    @DeleteMapping("/message/{receiptHandle}")
    public ResponseEntity<String> deleteMessage(@PathVariable String receiptHandle) {
        sqsReceiverService.deleteMessage(receiptHandle);
        return new ResponseEntity<>("Message deleted successfully", HttpStatus.OK);
    }
}
