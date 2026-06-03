package com.example.stumanage.service;

import com.example.stumanage.model.User;
import com.example.stumanage.model.Person;
import com.example.stumanage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonService personService;

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
        
        // 第一步：先创建 Person 记录
        Person person = new Person();
        person.setName(user.getUsername());
        Person savedPerson = personService.savePerson(person);
        
        // 第二步：创建 User，关联到刚创建的 Person
        user.setPersonId(savedPerson.getId());
        return userRepository.save(user);
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
