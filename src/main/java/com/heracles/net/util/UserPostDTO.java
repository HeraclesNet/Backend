package com.heracles.net.util;

import com.heracles.net.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPostDTO {
	
	private String nickName;
	private String name;

	public UserPostDTO(User user) {
		this.nickName = user.getNickName();
		this.name = user.getName();
	}
}
