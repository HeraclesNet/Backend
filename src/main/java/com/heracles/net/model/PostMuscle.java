package com.heracles.net.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "posts_muscles", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class PostMuscle {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", unique = true, nullable = false)
	private String id;

	@Column(name = "post_id", nullable = false, updatable = false)
	private String postId;

	@Column(name = "user_id", nullable = false, updatable = false)
	private String userId;

	public PostMuscle(String postId, String userId) {
		this.postId = postId;
		this.userId = userId;
	}
}
