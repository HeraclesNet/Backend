package com.heracles.net.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "Post")
@Table(name = "posts", schema = "public")
@NoArgsConstructor
public class AppPost {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@Column(name = "content", columnDefinition = "TEXT")
	private String content;

	@Column(name = "muscles")
	private int muscles;

	@ManyToOne
	@JoinColumn(referencedColumnName = "id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_user_id"))
	private User user;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP")
	private LocalDate createdAt;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post", targetEntity = FileDB.class)
	private List<FileDB> files = new LinkedList<>();

	public AppPost(String content, int muscles, User user, MultipartFile file) throws IOException {
		this.content = content;
		this.muscles = muscles;
		this.user = user;
		files.add(new FileDB(file, this));
	}
}
