package com.example.demo.service;

import com.example.demo.domain.Order;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(String customerName, String foodItem, int quantity, double totalPrice) {
        Order order = new Order(customerName, foodItem, quantity, totalPrice, LocalDateTime.now());
        return orderRepository.save(order);
    }
}