package com.example.demo.controller;

import com.example.demo.domain.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.SqsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// WebMvcTest 어노테이션은 OrderController 클래스를 대상으로 웹 계층만 테스트하도록 설정합니다.
@WebMvcTest(OrderController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class OrderControllerTest {

    // mockMvc는 웹 애플리케이션의 요청과 응답을 모의화하여 테스트합니다.
    @Autowired
    private MockMvc mockMvc;

    // objectMapper는 객체를 JSON 형식으로 변환하거나 그 반대의 작업을 수행합니다.
    @Autowired
    private ObjectMapper objectMapper;

    // MockBean 어노테이션은 실제 OrderRepository 대신 가짜 객체를 생성합니다.
    @MockBean
    private OrderRepository orderRepository;

    // MockBean 어노테이션은 실제 SqsService 대신 가짜 객체를 생성합니다.
    @MockBean
    private SqsService sqsService;

    // 테스트에 사용할 Order 객체
    private Order testOrder;

    // BeforeEach 어노테이션은 이 메서드를 각 테스트 실행 전에 호출하도록 합니다.
    // 여기에서는 testOrder 객체를 초기화합니다.
    @BeforeEach
    public void setup() {
        testOrder = new Order("cust123", "Burger", 2, 15.98);
        testOrder.setId(1L);
    }


    // 테스트 메서드: 주문 생성 기능을 테스트합니다.
    @Test
    public void testCreateOrder() throws Exception {
        // 주문 저장 및 조회 동작을 모의화합니다.
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);  // 여기를 변경
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // mockMvc를 사용하여 /api/orders에 POST 요청을 시뮬레이션하고 응답을 검증합니다.
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrder)))
                .andExpect(status().isCreated())  // HTTP 상태 코드 201을 기대합니다.
                .andExpect(content().json(objectMapper.writeValueAsString(testOrder))); // 반환된 내용이 testOrder와 일치하는지 검증합니다.
    }
}
