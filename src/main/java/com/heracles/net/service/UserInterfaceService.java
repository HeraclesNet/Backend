package com.heracles.net.service;

import java.io.IOException;
import java.util.List;

import com.heracles.net.message.ResponseMessage;
import com.heracles.net.model.User;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

public interface UserInterfaceService {

	public void addNewUser(User user) throws Exception;

	public List<User> getUsers();

	public User findUserByEmail(String email) throws UsernameNotFoundException;

	public void EditUserExtraData(String email,String key,String value) throws Exception;

	public void EditUserImportantData() throws Exception;

	public ResponseMessage addPost(String email, String content, MultipartFile file) throws UsernameNotFoundException, IOException;
}
