package com.heracles.net.repository;

import java.util.Optional;

import com.heracles.net.model.FollowerDB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FollowerRepository extends JpaRepository<FollowerDB, String> {
	
	@Modifying
	@Query(value = "DELETE FROM followers WHERE user_id = ?1 AND follower_id = ?2",
		nativeQuery = true)
	public void deleteByUserIdAndFollowerId(String userId, String followerId);

	public Optional<FollowerDB> findByUserIdAndFollowerId(String userId, String followerId);
}