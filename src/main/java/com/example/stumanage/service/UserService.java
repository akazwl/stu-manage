package com.example.stumanage.service;

import com.example.stumanage.model.User;
import com.example.stumanage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User authenticate(String username, String password, String role) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password) && user.getRole().equals(role))
                return user;
        }
        return null;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<User> getAllUsers() { return userRepository.findAll(); }
    public List<User> getUsersByRole(String role) { return userRepository.findByRole(role); }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new RuntimeException("用户名已存在: " + user.getUsername());
        User saved = userRepository.save(user);       // 先保存拿到自增 id
        saved.setPersonId(saved.getId());             // person_id = id
        return userRepository.save(saved);             // 更新
    }

    public User resetPassword(Integer userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + userId));
        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) { userRepository.deleteById(id); }
    public User saveUser(User user) { return userRepository.save(user); }
}
