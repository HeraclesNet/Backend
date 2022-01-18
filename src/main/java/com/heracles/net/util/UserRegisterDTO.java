package com.heracles.net.util;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {

	private String name;
	private String email;
	private String password;
	private String nickName;
	private LocalDate dateOfBirth;
}
