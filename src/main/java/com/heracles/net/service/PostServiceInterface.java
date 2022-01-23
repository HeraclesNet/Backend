package com.heracles.net.service;

import com.heracles.net.util.PostDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostServiceInterface {
	
	public Page<PostDTO> getPosts(boolean isFrends, String email, Pageable pageable);
	
	public List<PostDTO> getUserPost(String userID, boolean isEmail);
	
	public void upDateMuscle(String postId, int muscle, String email);

}
