package com.example.showcase.repository;

import com.example.showcase.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuperAdminRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
}
