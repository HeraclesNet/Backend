package com.heracles.net.message;

import com.heracles.net.util.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthMessage {
	private String token;
	private String refreshToken;
	private UserDTO user;
}
