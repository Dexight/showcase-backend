package com.example.showcase.repository;

import com.example.showcase.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    User findByLogin(String login);

    User findByEmail(String email);

    //Fallback для старых записей без email: связывание OAuth-входа по ФИО
    List<User> findByFullNameAndEmailIsNull(String fullName);

    boolean existsUserByFullNameAndCourse(String fullName, String course);

    User getUserByFullNameAndCourse(String fullName, String course);
}
