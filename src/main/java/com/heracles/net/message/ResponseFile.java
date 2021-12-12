package com.heracles.net.message;

import com.heracles.net.model.FileDB;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseFile {
  
    private String name;
    private String url;
    private String type;
    private long size;

    public ResponseFile(FileDB fileDB) {
        String fileDownloadUri = ServletUriComponentsBuilder
          .fromCurrentContextPath()
          .path("/files/")
          .path(fileDB.getId())
          .toUriString();
        this.name = fileDB.getName();
        this.url = fileDownloadUri;
        this.type = fileDB.getType();
        this.size = fileDB.getData().length;
    }
}