package com.heracles.net.repository;

import java.util.List;

import com.heracles.net.model.AppPost;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<AppPost, String> {

    @Query(value = "select * from posts where posts.user_id = ?1", nativeQuery = true)
    public List<AppPost> findUserPost(String userID);

	public Page<AppPost> findAllByUserId(String userId, Pageable pageable);
    
    @Query(value = "select * from posts where user_id in (select follower_id from followers where user_id = ?1)", nativeQuery = true)
    public Page<AppPost> findFriendsPost(String userId, Pageable pageable);

    @Query(value = "select * from posts where user_id in (select id from users where visibility = TRUE)", nativeQuery = true)
    public Page<AppPost> findAllUserVisibility(Pageable pageable);

}
