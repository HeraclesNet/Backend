package com.heracles.net.service;

import java.util.List;

import com.heracles.net.model.User;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserInterfaceService {

	public void addNewUser(User user) throws Exception;

	public User getUserLogin(String email, String password) throws Exception;

	public List<User> getUsers();

	public User findUserByEmail(String email) throws UsernameNotFoundException;

	public void EditUserExtraData(String email,String key,String value) throws Exception;

	public void EditUserImportantData() throws Exception;
}
