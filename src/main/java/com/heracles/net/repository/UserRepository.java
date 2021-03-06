package com.heracles.net.repository;

import com.heracles.net.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    @Query(value = "SELECT * FROM users WHERE id IN (SELECT follower_id FROM followers WHERE user_id = ?1)",
       countQuery = "SELECT COUNT(*) FROM followers WHERE user_id = ?1",  
        nativeQuery = true)
    public Page<User> findAllFollowing(String userId, Pageable pageable);

    public Optional<User> findUserByEmail(String email);

    public Optional<User> findUserByNickName(String nickName);

    @Modifying
    @Query(value = "call delete_user(?1)", nativeQuery = true)
    public void deleteAccount(String userId);
    
}
