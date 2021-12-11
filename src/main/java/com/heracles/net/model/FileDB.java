package com.heracles.net.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "files")
@NoArgsConstructor
public class FileDB {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uui2")
  @Column(name = "id", nullable = false, unique = true)
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "type", nullable = false)
  private String type;

  @Lob
  @Column(name = "content", nullable = false)
  private byte[] data;

  public FileDB(String name, String type, byte[] data) {
    this.name = name;
    this.type = type;
    this.data = data;
  }

}