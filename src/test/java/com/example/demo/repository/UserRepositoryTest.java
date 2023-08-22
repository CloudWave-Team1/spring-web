package com.example.demo.repository;

import com.example.demo.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void createUser() {
        User newUser = new User();
        newUser.setName("John Doe");

        User savedUser = userRepository.save(newUser);

        assertNotNull(savedUser.getId());  // 저장 후 ID가 null이 아닌지 확인
        assertEquals("John Doe", savedUser.getName());  // 저장된 이름 확인
    }
}