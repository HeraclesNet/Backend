package com.heracles.net.repository;

import com.heracles.net.model.FollowerDB;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowerRepository extends JpaRepository<FollowerDB, String> {
	
}