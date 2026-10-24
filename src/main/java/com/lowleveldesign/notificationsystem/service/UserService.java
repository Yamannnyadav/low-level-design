package com.lowleveldesign.notificationsystem.service;

import com.lowleveldesign.notificationsystem.exceptions.ResourceNotFoundException;
import com.lowleveldesign.notificationsystem.model.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserService {
    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>(); //userId vs User
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public User registerUser(String name, String email, String phone, String deviceToken) {
        User user = new User("U-" + idCounter.getAndIncrement(), name, email, phone, deviceToken);
        users.put(user.getId(), user);
        return user;
    }

    public User getUser(String userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + userId);
        }
        return user;
    }
}
