package com.example.demo.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "mobile_token", nullable = false)
    private String mobileToken; // 사용자의 모바일 토큰 값

    @Column(name = "endpoint_arn", nullable = false)
    private String endpointArn; // SNS 엔드포인트 ARN
}