package com.heracles.net.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import com.heracles.net.model.FileDB;
import com.heracles.net.repository.FileDBRepository;

@Service
@AllArgsConstructor
public class FileStorageService {

  private FileDBRepository fileDBRepository;

  public FileDB getFile(String id) {
    return fileDBRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found with id " + id));
  }

}