package com.heracles.net.message;

import com.heracles.net.util.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileMessage {

	private UserDTO user;
	private boolean followed;
	private boolean isPrivate;

}