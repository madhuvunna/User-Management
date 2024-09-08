package com.example.User.Management.service;

import java.util.Map;

import com.example.User.Management.dto.LoginFormDTO;
import com.example.User.Management.dto.RegisterFormDTO;
import com.example.User.Management.dto.ResetPasswordFormDTO;
import com.example.User.Management.dto.UserDTO;

public interface UserService {

	public Map<Integer, String> getCountries();

	public Map<Integer, String> getStates(Integer countryId);

	public Map<Integer, String> getCities(Integer stateId);

	public boolean duplicateEmailCheck(String email);

	public boolean saveUser(RegisterFormDTO regFormDTO);

	public UserDTO login(LoginFormDTO loginFormDTO);

	public boolean resetPwd(ResetPasswordFormDTO resetPasswordFormDTO);
	
//	public UserDTO getUserByEmail(String email);

}
