package com.heracles.net.service;

import java.util.stream.Collectors;

import com.heracles.net.repository.PostRepository;
import com.heracles.net.util.PostDTO;

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

	@Override
	public Page<PostDTO> getPosts(Pageable pageable) {
		log.info("Get all posts");
		return new PageImpl<>(postRepository.findAll(pageable).stream().map(PostDTO::new).collect(Collectors.toList()));
	}

	
}
