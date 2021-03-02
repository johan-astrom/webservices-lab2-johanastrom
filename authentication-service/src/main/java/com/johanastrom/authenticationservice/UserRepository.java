package com.johanastrom.authenticationservice;

import com.johanastrom.authenticationservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String userName);
}
