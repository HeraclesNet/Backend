package com.heracles.net.util;

import java.time.LocalDate;

import com.heracles.net.model.User;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String name;
	private String email;
	private String nickName;
	private LocalDate dateOfBirth;
    private float weight;
    private boolean gender;
    private float height;

    public UserLoginDTO(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.nickName = user.getNickName();
        this.dateOfBirth = user.getDateOfBirth();
        this.weight = user.getWeight();
        this.height =user.getHeight();
        this.gender = user.isGender();
    }
}
