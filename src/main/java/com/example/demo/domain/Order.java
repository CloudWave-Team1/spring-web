package com.example.demo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "food_item", nullable = false)
    private String foodItem;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    public Order(String customerId, String foodItem, int quantity, double totalPrice) {
        this.customerId = customerId;
        this.foodItem = foodItem;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = LocalDateTime.now();
    }

    public Order(String customerId, String foodItem, int quantity, double totalPrice, LocalDateTime orderDate) {
        this.customerId = customerId;
        this.foodItem = foodItem;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

}
