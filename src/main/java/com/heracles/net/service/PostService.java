package com.heracles.net.service;

import java.util.List;
import java.util.stream.Collectors;

import com.heracles.net.model.AppPost;
import com.heracles.net.model.FileDB;
import com.heracles.net.model.User;
import com.heracles.net.repository.FileDBRepository;
import com.heracles.net.repository.PostRepository;
import com.heracles.net.repository.UserRepository;
import com.heracles.net.util.PostDTO;
import com.heracles.net.util.UserRegisterDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class PostService implements PostServiceInterface {

	private final PostRepository postRepository;
	private final FileDBRepository fileDBRepository;
	private final UserRepository userRepository;

	@Override
	public Page<PostDTO> getPosts(Pageable pageable) {
		log.info("Get all posts");
		List<AppPost> findAll = postRepository.findAll(pageable).getContent();
		List<PostDTO> collect = findAll.stream().map(post -> {
			List<FileDB> files = fileDBRepository.findByPost(post);
			return new PostDTO(post, files);
		}).collect(Collectors.toList());
		return new PageImpl<>(collect);
	}

	@Override
	public Page<PostDTO> getUserPost(String email) {
		
		User user = userRepository.findUserByEmail(email).get();
		List<PostDTO> getPosts = postRepository.findUserPost(user.getId()).stream().map(post -> {
			List<FileDB> files = fileDBRepository.findByPost(post);
			return new PostDTO(post, files);
		}).collect(Collectors.toList());
		return new PageImpl<>(getPosts);
	}

	
}
