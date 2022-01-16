package com.heracles.net.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.heracles.net.model.AppPost;
import com.heracles.net.model.FileDB;
import com.heracles.net.model.PostMuscle;
import com.heracles.net.model.User;
import com.heracles.net.repository.FileDBRepository;
import com.heracles.net.repository.MusclesRepository;
import com.heracles.net.repository.PostRepository;
import com.heracles.net.repository.UserRepository;
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
	private final FileDBRepository fileDBRepository;
	private final MusclesRepository musclesRepository;
	private final UserRepository userRepository;

	@Override
	public Page<PostDTO> getPosts(String email, Pageable pageable) {
		Optional<User> optional = userRepository.findUserByEmail(email);
		if (!optional.isPresent()) {
			log.error("User not found");
			return null;
		}
		User user = optional.get();
		log.info("Get all posts for user: {}", user.getId());
		List<AppPost> findAll = postRepository.findAll(pageable).getContent();
		List<PostDTO> collect = findAll.stream().map(post -> {
			List<FileDB> files = fileDBRepository.findByPost(post);
			Optional<PostMuscle> postMuscle = musclesRepository.findByUserIdAndPostId(user.getId(), post.getId());
			return new PostDTO(post, files, postMuscle.isPresent());
		}).collect(Collectors.toList());
		return new PageImpl<>(collect);
	}

	@Override
	public Page<PostDTO> getUserPost(String email) {
		User user = userRepository.findUserByEmail(email).orElseThrow();
		List<PostDTO> getPosts = postRepository.findUserPost(user.getId()).stream().map(post -> {
			List<FileDB> files = fileDBRepository.findByPost(post);
			return new PostDTO(post, files);
		}).collect(Collectors.toList());
		return new PageImpl<>(getPosts);
	}

	
	public void upDateMuscle(String postId, int muscle) {
		log.info("Update muscle of post {}", postId);
		Optional<AppPost> findById = postRepository.findById(postId);
		AppPost post = findById.orElseThrow();
		User user = post.getUser();
		post.setMuscles(post.getMuscles() + muscle);
		postRepository.save(post);
		if (muscle > 0) {
			log.info("User {} mulced post", user.getId());
			musclesRepository.save(new PostMuscle(postId, user.getId()));
		} else {
			log.info("User {} downvoted post", user.getId());
			musclesRepository.deleteByUserIdAndPostId(user.getId(), postId);
		}
	}

}
