package com.heracles.net.service;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.heracles.net.model.FileDB;
import com.heracles.net.repository.FileDBRepository;

@Service
public class FileStorageService {

  @Autowired
  private FileDBRepository fileDBRepository;

  public FileDB store(MultipartFile file) throws IOException {
    String origianlName = file.getOriginalFilename();
    if (origianlName == null) {
      throw new IOException("File name is empty");
    }
    String fileName = StringUtils.cleanPath(origianlName);
    FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes());

    return fileDBRepository.save(fileDB);
  }

  public FileDB getFile(String id) {
    return fileDBRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found with id " + id));
  }
  
  public Stream<FileDB> getAllFiles() {
    return fileDBRepository.findAll().stream();
  }
}