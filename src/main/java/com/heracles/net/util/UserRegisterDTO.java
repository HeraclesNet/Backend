package com.heracles.net.util;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserRegisterDTO {
	private String name;
	private String email;
	private String password;
	private String nickName;
	private LocalDate dateOfBirth;
}
