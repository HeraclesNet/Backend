package com.heracles.net.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMessage {
	
	private String token;
	private String refreshToken;
	private String message;
}
