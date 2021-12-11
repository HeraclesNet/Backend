package com.heracles.net.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "Post")
@Table(name = "post", schema = "public")
@NoArgsConstructor
public class AppPost {
	// Id of the post in the database table using uuid
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

}
