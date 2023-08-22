package com.example.demo.controller;

import com.example.demo.dto.MessageDto;
import com.example.demo.dto.ReceiptHandleDto;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.SqsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final SqsService sqsReceiverService;

    @Autowired
    private OrderRepository orderRepository;

    public MessageController(SqsService sqsReceiverService) {
        this.sqsReceiverService = sqsReceiverService;
    }

    @GetMapping
    public List<MessageDto> getMessages() {
        return sqsReceiverService.receiveMessages();
    }

    @PostMapping("/complete")
    public ResponseEntity<String> deleteMessage(@RequestBody ReceiptHandleDto receiptHandleDto) {
        sqsReceiverService.deleteMessage(receiptHandleDto.getReceiptHandle());

        return new ResponseEntity<>("Message deleted successfully", HttpStatus.OK);
    }

}
