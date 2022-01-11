package com.heracles.net.repository;

import com.heracles.net.model.AppPost;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<AppPost, String> {

	public Page<AppPost> findAllByUserId(String userId, Pageable pageable);

}
