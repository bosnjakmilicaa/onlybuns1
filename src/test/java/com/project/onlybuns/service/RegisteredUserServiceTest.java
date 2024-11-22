package com.project.onlybuns.service;
/*
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.repository.RegisteredUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;

@SpringBootTest
public class RegisteredUserServiceTest {

    @Autowired
    private RegisteredUserService registeredUserService;

    @Autowired
    private RegisteredUserRepository registeredUserRepository;

    @Test
    @Transactional
    public void testConcurrentFollow() throws InterruptedException {
        Long followerId1 = 4L;
        Long followerId2 = 5L;
        Long followedId = 8L;

        CountDownLatch latch1 = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);

        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(50);
                registeredUserService.followUser(followerId1, followedId);
            } catch (Exception e) {
                System.out.println("Thread 1 error: " + e.getMessage());
            } finally {
                latch1.countDown();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                latch1.await();
                Thread.sleep(50);
                registeredUserService.followUser(followerId2, followedId);
            } catch (Exception e) {
                System.out.println("Thread 2 error: " + e.getMessage());
            } finally {
                latch2.countDown();
            }
        });

        thread1.start();
        thread2.start();

        latch2.await();

        RegisteredUser follower1 = registeredUserRepository.findById(followerId1).orElseThrow();
        RegisteredUser follower2 = registeredUserRepository.findById(followerId2).orElseThrow();
        RegisteredUser followed = registeredUserRepository.findById(followedId).orElseThrow();

        System.out.println("Follower 1's following count: " + follower1.getFollowing().size());
        System.out.println("Follower 2's following count: " + follower2.getFollowing().size());
        System.out.println("Followed's followers count: " + followed.getFollowers().size());

        Assertions.assertEquals(3, follower1.getFollowing().size());
        Assertions.assertEquals(3, follower2.getFollowing().size());
        Assertions.assertEquals(2, followed.getFollowers().size());
    }
}*/
