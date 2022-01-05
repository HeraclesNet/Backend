package com.heracles.net.repository;

import com.heracles.net.model.AppPost;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<AppPost, String> {

}
