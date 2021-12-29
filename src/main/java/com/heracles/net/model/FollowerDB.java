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
@Table(name = "followers")
@AllArgsConstructor
@NoArgsConstructor
public class FollowerDB {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", unique = true, nullable = false)
	private String id;

	@Column(name = "user_id", nullable = false)
	private String userId;

	@Column(name = "follower_id", nullable = false)
	private String followerId;

	public FollowerDB(String userId, String followerId) {
		this.userId = userId;
		this.followerId = followerId;
	}
}
