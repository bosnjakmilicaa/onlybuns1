package com.project.onlybuns.service;

import com.project.onlybuns.DTO.UserDTO;
import com.project.onlybuns.model.User;
import com.project.onlybuns.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.times;

@SpringBootTest

class UserServiceTest {

    @Autowired
    private UserService registeredUserService;

    @MockBean
    private UserRepository registeredUserRepository;

    @Test
    @Transactional
    @Rollback
    void testConcurrentRegistration() throws ExecutionException, InterruptedException {   // Arrange
    UserDTO userDTO1 = new UserDTO("testUser", "password123", "test1@example.com", "John", "Doe", "Address 1");
    UserDTO userDTO2 = new UserDTO("testUser", "password456", "test2@example.com", "Jane", "Doe", "Address 2");

    // Simulate that a user with "testUser" already exists
    when(registeredUserRepository.existsByUsername("testUser")).thenReturn(true);

    // Simulate concurrent registration
    CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
        try {
            registeredUserService.registerUser(userDTO1);
        } catch (Exception e) {
            // Log or handle exception (expected failure)
            System.out.println("Thread 1 failed: " + e.getMessage());
        }
    });

    CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
        try {
            registeredUserService.registerUser(userDTO2);
        } catch (Exception e) {
            // Log or handle exception (expected failure)
            System.out.println("Thread 2 failed: " + e.getMessage());
        }
    });

    // Wait for both threads to complete
        CompletableFuture.allOf(future1, future2).get();

    // Verify the repository count remains 0, meaning no new users were registered
    long count = registeredUserRepository.count();
    assertEquals(0, count, "No users should have been registered.");

}}
