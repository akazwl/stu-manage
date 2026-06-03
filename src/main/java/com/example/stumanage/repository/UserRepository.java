package com.example.stumanage.repository;

import com.example.stumanage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    List<User> findByRole(String role);          // 按角色查询（管理员查所有学生/教师）
    boolean existsByUsername(String username);   // 检查用户名是否已存在
}
