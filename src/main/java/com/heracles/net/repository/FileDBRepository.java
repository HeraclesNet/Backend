package com.heracles.net.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.heracles.net.model.FileDB;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {

}