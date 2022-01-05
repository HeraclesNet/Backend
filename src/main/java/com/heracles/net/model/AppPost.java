package com.heracles.net.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "Post")
@Table(name = "posts", schema = "public")
@NoArgsConstructor
public class AppPost {

	@Id
	private String id;

	@Column(name = "content", columnDefinition = "TEXT")
	private String content;

	@Column(name = "muscles", columnDefinition = "INTEGER DEFAULT 0")
	private int muscles;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_user_id"))
	private User user;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP")
	private LocalDate createdAt;


	public AppPost(String content, User user) {
		this.id = UUID.randomUUID().toString();
		this.content = content;
		this.user = user;
		this.createdAt = LocalDate.now();
	}
}
