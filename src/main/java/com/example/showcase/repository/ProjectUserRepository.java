package com.example.showcase.repository;

import com.example.showcase.entity.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Integer> {
}
