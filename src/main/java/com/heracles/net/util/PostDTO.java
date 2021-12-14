package com.heracles.net.util;

import java.util.List;

import com.heracles.net.message.ResponseFile;
import com.heracles.net.model.AppPost;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
	private String id;
	private String post;
	private int muscles;
	private boolean hasFiles;
	private List<ResponseFile> files;

	public PostDTO(AppPost post) {
		this.id = post.getId();
		this.post = post.getContent();
		this.muscles = post.getMuscles();
		this.files = post.getFiles().stream().map(ResponseFile::new).collect(java.util.stream.Collectors.toList());
		this.hasFiles = !this.files.isEmpty();
	}

	@Override
	public String toString() {
		return "{" +
			" content= '" + getPost() + "'" +
			", muscles= '" + getMuscles() + "'" +
			", files= '" + getFiles().size() + "'" +
			", hasFiles= '" + isHasFiles() + "'" +
			"}";
	}

}
