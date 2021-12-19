package com.heracles.net.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.heracles.net.model.AppPost;
import com.heracles.net.model.FileDB;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {

	@Query("SELECT f FROM FileDB f WHERE f.post = ?1")
	List<FileDB> findByPost(AppPost post);

}