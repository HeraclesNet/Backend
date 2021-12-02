package com.heracles.net.dto;


import java.time.LocalDate;

import lombok.Data;

@Data
public class UserRegisterDTO {
	private String name;
	private String email;
	private String password;
	private String nickname;
	private char gender;
	private LocalDate dateOfBirth;
}
