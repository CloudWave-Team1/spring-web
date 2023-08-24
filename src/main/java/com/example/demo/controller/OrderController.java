package com.example.demo.controller;

import com.example.demo.domain.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.SqsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "OrderController", description = "DB에 주문 정보 저장 및 SQS 큐 대기열에 메시지 추가")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SqsService sqsService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        // Order 저장
        Order save = orderRepository.save(order);

        return sendMessageToQueue(save.getId());
    }

    private ResponseEntity<Order> sendMessageToQueue(Long orderId) {
        // SQS 메시지 전송 (주문 번호를 메시지로 보냅니다. 주문번호가 없으므로 일단 id를 문자열로 변환하여 사용합니다.)
        sqsService.sendOrderMessage(String.valueOf(orderId));
        Order order = orderRepository.findById(orderId).get();
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
