package com.example.demo.controller;

import com.example.demo.domain.Order;
import com.example.demo.dto.MessageDto;
import com.example.demo.dto.OrderMessageDto;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.SqsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class UIController {
    private final SqsService sqsReceiverService;

    @Autowired
    private OrderRepository orderRepository;

    public UIController(SqsService sqsReceiverService) {
        this.sqsReceiverService = sqsReceiverService;
    }

    @GetMapping
    public String getMessages(Model model) {
        List<MessageDto> messageDtos = sqsReceiverService.receiveMessages();
        List<OrderMessageDto> orderMessageDtos = new ArrayList<>();

        for (MessageDto messageDto : messageDtos) {
            try {
                Long orderId = Long.parseLong(messageDto.getBody());
                Optional<Order> orderOpt = orderRepository.findById(orderId);

                if (orderOpt.isPresent()) {
                    Order order = orderOpt.get();
                    OrderMessageDto orderMessageDto = new OrderMessageDto();

                    orderMessageDto.setOrderId(order.getId());
                    orderMessageDto.setFoodItem(order.getFoodItem());
                    orderMessageDto.setQuantity(order.getQuantity());
                    orderMessageDto.setOrderDate(order.getOrderDate());
                    orderMessageDto.setReceiptHandle(messageDto.getReceiptHandle());

                    orderMessageDtos.add(orderMessageDto);
                }
            } catch (NumberFormatException e) {
                // Log error or handle exception
            }
        }

        model.addAttribute("orders", orderMessageDtos);
        return "message-list";
    }
}
