package com.heracles.net.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity(name = "User")
@Table(name = "users", uniqueConstraints = {
		@UniqueConstraint(name = "user_email_unique", columnNames = "email"),
		@UniqueConstraint(name = "user_nick_name_unique", columnNames = "nick_name")
})
public class User implements Serializable {
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	@Column(name = "name", nullable = false, updatable = false, columnDefinition = "TEXT")
	private String name; // Se debe ingresar primero los nombre y continuo los apellidos
	@Column(name = "data_of_birth", updatable = false, nullable = false)
	private LocalDate dateOfBirth;
	@Column(name = "email", nullable = false)
	private String email;
	@Column(name = "nick_name", nullable = false)
	private String nickName;
	@Column(name = "password", nullable = false, columnDefinition = "TEXT")
	private String password;
	@Column(name = "weight", precision = 3, updatable = true)
	private float weight;
	@Column(name = "height", precision = 3, updatable = true)
	private float height;
	@Column(name = "gender", nullable = true, updatable = true)
	private boolean gender;
	@Column(name = "visibility",nullable = false)
	private boolean visibility;

	public User() {
	}

	public User(String name, LocalDate dateOfBirth, String email, String nickName, String password,
			float weight, float height, boolean gender) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.nickName = nickName;
		this.password = password;
		this.weight = weight;
		this.height = height;
		this.gender = gender;
	}
}