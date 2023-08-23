package com.example.demo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderMessageDto {
    private Long orderId;
    private String foodItem;
    private int quantity;
    private LocalDateTime orderDate;
    private String receiptHandle;
    private String customerId;
}
