package com.example.demo.repository;

import com.example.demo.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o.id FROM Order o WHERE o.customerId = ?1")
    String findIdByCustomerId(String customerId);

    Order findByCustomerId(String customerId);

}
