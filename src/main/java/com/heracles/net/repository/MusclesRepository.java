package com.heracles.net.repository;

import java.util.Optional;

import com.heracles.net.model.PostMuscle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MusclesRepository extends JpaRepository<PostMuscle, String> {

	@Modifying
	@Query(value = "DELETE FROM public.posts_muscles WHERE user_id = ?1 AND post_id = ?2", nativeQuery = true)
	public int deleteByUserIdAndPostId(String userId, String postId);

	@Query(value = "SELECT * FROM public.posts_muscles WHERE user_id = ?1 AND post_id = ?2", nativeQuery = true)
	public Optional<PostMuscle> findByUserIdAndPostId(String userId, String postId);
}
