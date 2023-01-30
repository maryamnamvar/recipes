package com.abnamro.recipes.repository;

import com.abnamro.recipes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
