package com.heracles.net.util;

import java.util.List;

import com.heracles.net.message.ResponseFile;
import com.heracles.net.model.AppPost;
import com.heracles.net.model.FileDB;

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
	private UserPostDTO user;
	private boolean isMuscle;

	public PostDTO(AppPost post, List<FileDB> files, boolean isMuscle) {
		this.id = post.getId();
		this.post = post.getContent();
		this.muscles = post.getMuscles();
		this.files = files.stream().map(ResponseFile::new).collect(java.util.stream.Collectors.toList());
		this.hasFiles = !this.files.isEmpty();
		this.user = new UserPostDTO(post.getUser());
		this.isMuscle = isMuscle;
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
