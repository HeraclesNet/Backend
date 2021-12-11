package com.heracles.net.util;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
	private String content;
	private int muscles;
	private MultipartFile file;

	@Override
	public String toString() {
		return "{" +
			" content= '" + getContent() + "'" +
			", muscles= '" + getMuscles() + "'" +
			", file= '" + getFile().getOriginalFilename() + "'" +
			"}";
	}

}
