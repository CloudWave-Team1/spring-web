package com.example.demo.repository;

import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // customerId로 User의 endpointArn만 조회하는 메서드 선언
    @Query("select u.endpointArn from User u where u.customerId = ?1")
    String findEndpointArnByCustomerId(String customerId);
}

