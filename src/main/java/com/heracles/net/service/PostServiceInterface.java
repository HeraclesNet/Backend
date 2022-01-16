package com.heracles.net.service;

import com.heracles.net.util.PostDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostServiceInterface {
	
	public Page<PostDTO> getPosts(Pageable pageable);
	public Page<PostDTO> getUserPost(String userID);
	
	public void upDateMuscle(String postId, int muscle);

}
