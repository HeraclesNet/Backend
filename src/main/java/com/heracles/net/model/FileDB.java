package com.heracles.net.model;

import java.io.IOException;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "files")
@NoArgsConstructor
public class FileDB {

  @Id
  @Column(name = "id", nullable = false, unique = true)
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "type", nullable = false)
  private String type;

  @Lob
  @Column(name = "content", nullable = false)
  private byte[] data;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(referencedColumnName = "id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_post_id"))
  private AppPost post;

  public FileDB(MultipartFile file, AppPost post) throws IOException {
    this.id = UUID.randomUUID().toString();
    String temp = file.getOriginalFilename();
    if (temp != null)
      this.name = StringUtils.cleanPath(temp);
    this.type = file.getContentType();
    this.data = file.getBytes();
    this.post = post;
  }
  
  public String getPathUrl(){
    return ServletUriComponentsBuilder
    .fromCurrentContextPath()
    .path("/home/files").queryParam("id",this.getId())
    .toUriString();
  }

}