package com.eatandpic.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatandpic.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}