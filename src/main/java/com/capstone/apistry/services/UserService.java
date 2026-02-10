package com.capstone.apistry.services;

import com.capstone.apistry.entities.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    void deleteUser(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
