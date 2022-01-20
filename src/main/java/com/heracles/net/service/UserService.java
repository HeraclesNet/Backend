
package com.heracles.net.service;

import java.io.IOException;
import java.util.*;

import com.heracles.net.message.ResponseMessage;
import com.heracles.net.model.*;
import com.heracles.net.repository.*;
import com.heracles.net.util.CustomUserDetails;
import com.heracles.net.util.UserDTO;
import com.heracles.net.util.UserUpdateDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UserService implements UserDetailsService, UserInterfaceService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FileDBRepository fileDBRepository;
    private final FollowerRepository followerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PostRepository postRepository,
    FileDBRepository fileDBRepository,FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.fileDBRepository = fileDBRepository;
        this.followerRepository = followerRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public void addNewUser(User user) throws Exception {
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            log.error("User with email {} already exists", user.getEmail());
            throw new Exception(String.format("User with email %s already exists", user.getEmail()));
        }
        userOptional = userRepository.findUserByNickName(user.getNickName());
        if (userOptional.isPresent()) {
            log.error("NickName already exists {}", user.getNickName());
            throw new Exception(String.format("Nickname already exist %s", user.getNickName()));
        }
        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(userOptional.get());
    }

    @Override
    public User findUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public void EditUserExtraData(String email,UserUpdateDTO userUpdateDTO) throws Exception {
        User user = userRepository.findUserByEmail(email).orElseThrow();
        user.setHeight(userUpdateDTO.getHeight());
        user.setWeight(userUpdateDTO.getWeight());
        user.setGender(userUpdateDTO.isGender());
        user.setVisibility(userUpdateDTO.isVisibility());
        userRepository.save(user);
    }

    @Override
    public void EditUserImportantData() throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public ResponseMessage addPost(String email, String content, MultipartFile file) throws UsernameNotFoundException, IOException{
        User user = userRepository.findUserByEmail(email).orElseThrow();
        ResponseMessage responseMessage = new ResponseMessage();
        if (file == null) {
            postRepository.save(new AppPost(content, user));
            responseMessage.setMessage("Post added only text");
        } else {
            AppPost post = new AppPost(content, user);
            FileDB fileDB = new FileDB(file, post);
            postRepository.save(post);
            fileDBRepository.save(fileDB);
            responseMessage.setMessage(fileDB.getPathUrl());
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage followUser(String email, String nickName) {
        User user = userRepository.findUserByEmail(email).orElseThrow();
        User follower = userRepository.findUserByNickName(nickName).orElseThrow();
        if(user.getId().equals(follower.getId())) {
            return new ResponseMessage("You can't follow yourself");
        } else if(followerRepository.findByUserIdAndFollowerId(user.getId(), follower.getId()).isPresent()) {
            return new ResponseMessage("You already follow this user");
        }
        followerRepository.save(new FollowerDB(user.getId(), follower.getId()));
        return new ResponseMessage("User followed");
    }

    @Override
    public Page<UserDTO> getFans(String email, Pageable pageable) {
        User user = userRepository.findUserByEmail(email).orElseThrow();
        Page<User> fans = userRepository.findAllFollowing(user.getId(), pageable);
        List<UserDTO> fansDTO = new ArrayList<>();
        fans.stream().forEach(f -> fansDTO.add(new UserDTO(f)));
        return new PageImpl<>(fansDTO);
    }

    @Override
    public UserDTO getUserDTO(String nickName) {
        User user = userRepository.findUserByNickName(nickName).orElseThrow();
        return new UserDTO(user);
    }

    public void unFollowUser(String email, String userToUnFollow) throws Exception {
        User user = userRepository.findUserByEmail(email).orElseThrow();
        User userToUnFollowUser = userRepository.findUserByNickName(userToUnFollow).orElseThrow();
        try {
            followerRepository.deleteByUserIdAndFollowerId(user.getId(), userToUnFollowUser.getId());
        } catch (Exception e) {
            throw new Exception("User no followed");
        }
    }

    @Override
    public void deleteAccount(String email) {
        log.info("Deleting account and information of {}", email);
        User user = userRepository.findUserByEmail(email).orElseThrow();
        userRepository.deleteAccount(user.getId());
    }
}
