package com.heracles.net.repository;

import com.heracles.net.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    @Query(value = "SELECT * FROM users LEFT JOIN followers ON users.id = ?1", nativeQuery = true)
    List<User> findAllFollowing(String userId);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByNickName(String nickName);
}
