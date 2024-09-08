package com.example.User.Management.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.User.Management.Entity.Cities;
import com.example.User.Management.Entity.Countries;
import com.example.User.Management.Entity.States;
import com.example.User.Management.Entity.Users;
import com.example.User.Management.dao.CitiesRepository;
import com.example.User.Management.dao.CountriesRepository;
import com.example.User.Management.dao.StatesRepository;
import com.example.User.Management.dao.UsersRepository;
import com.example.User.Management.dto.LoginFormDTO;
import com.example.User.Management.dto.RegisterFormDTO;
import com.example.User.Management.dto.ResetPasswordFormDTO;
import com.example.User.Management.dto.UserDTO;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private CountriesRepository countriesRepository;

	@Autowired
	private StatesRepository statesRepository;

	@Autowired
	private CitiesRepository citiesRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private EmailService emailService;

	@Override
	public Map<Integer, String> getCountries() {
		Map<Integer, String> countriesmap = new HashMap<>();
		List<Countries> countries = countriesRepository.findAll();
		countries.stream().forEach(c -> {
			countriesmap.put(c.getCountryId(), c.getCountryName());
		});
		return countriesmap;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {
		Map<Integer, String> statesMap = new HashMap<>();
		List<States> stateList = statesRepository.findByCountryId(countryId);
		stateList.forEach(s -> {
			statesMap.put(s.getStateId(), s.getStateName());
		});
		return statesMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer stateId) {
		Map<Integer, String> citiesMap = new HashMap<>();
		List<Cities> citiesList = citiesRepository.findByStateId(stateId);
		citiesList.forEach(c -> {
			citiesMap.put(c.getCityId(), c.getCityName());
		});

		return citiesMap;
	}

	@Override
	public boolean duplicateEmailCheck(String email) {
		Users byEmail = usersRepository.findByEmail(email);
		return byEmail != null;
	}

	@Override
	public boolean saveUser(RegisterFormDTO regFormDTO) {
		Users users = new Users();

		BeanUtils.copyProperties(regFormDTO, users);

		Countries countries = countriesRepository.findById(regFormDTO.getCountryId()).orElse(null);
		users.setCountries(countries);

		States states = statesRepository.findById(regFormDTO.getCityId()).orElse(null);
		users.setStates(states);

		Cities cities = citiesRepository.findById(regFormDTO.getCityId()).orElse(null);
		users.setCities(cities);

		String randompassword = generateRandompassword();
		users.setPassword(randompassword);
		users.setUpdatePassword("No");
		Users users2 = usersRepository.save(users);
		if (null != users2.getUserId()) {
			String subject = "Your account created";
			String body = "Ypur password to login:" + randompassword;
			String to = regFormDTO.getEmail();
			emailService.sendEmail(subject, body, to);
		}
		return false;

	}

	@Override
	public UserDTO login(LoginFormDTO loginFormDTO) {

		Users users = usersRepository.findByemailAndpassword(loginFormDTO.getEmail(), loginFormDTO.getPassword());
		if (users != null) {
			UserDTO dto = new UserDTO();
			BeanUtils.copyProperties(users, dto);
			return dto;
		}
		return null;
	}

	@Override
	public boolean resetPwd(ResetPasswordFormDTO resetPasswordFormDTO) {
		String email = resetPasswordFormDTO.getEmail();

		Users users = usersRepository.findByEmail(email);

		users.setPassword(resetPasswordFormDTO.getNewPassword());
		users.setUpdatePassword("yes");
		usersRepository.save(users);
		return false;
	}

	private String generateRandompassword() {
		String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";

		String alphabets = upperCaseLetters + lowerCaseLetters;

		Random random = new Random();

		StringBuffer generatedpassword = new StringBuffer();

		for (int i = 0; i < 5; i++) {
			int randomIndex = random.nextInt(alphabets.length());
			generatedpassword.append(alphabets.charAt(randomIndex));
		}
		return generatedpassword.toString();
	}

}
