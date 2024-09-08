package com.example.User.Management.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.User.Management.dto.LoginFormDTO;
import com.example.User.Management.dto.QuoteApiResponseDTO;
import com.example.User.Management.dto.RegisterFormDTO;
import com.example.User.Management.dto.ResetPasswordFormDTO;
import com.example.User.Management.dto.UserDTO;
import com.example.User.Management.service.DashboardService;
import com.example.User.Management.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private DashboardService dashboardService;

	@GetMapping("/register")
	public String loadRegisterPage(Model model) {
		Map<Integer, String> countriesMap = userService.getCountries();
		model.addAttribute("countries", countriesMap);

		RegisterFormDTO dto = new RegisterFormDTO();
		model.addAttribute("reigsterForm", dto);

		return "register";
	}

	@GetMapping("/states/{countryId}")
	@ResponseBody
	public Map<Integer, String> getStates(@PathVariable Integer countryId) {
		Map<Integer, String> statesMap = userService.getStates(countryId);
		return statesMap;
	}

	@GetMapping("/cities/{stateId}")
	@ResponseBody
	public Map<Integer, String> getCities(@PathVariable Integer stateId) {
		Map<Integer, String> citiesMap = userService.getCities(stateId);
		return citiesMap;
	}

	@PostMapping("/register")
	public String handleRegisteration(RegisterFormDTO registerFormDTO, Model model) {
		boolean isStatus = userService.duplicateEmailCheck(registerFormDTO.getEmail());
		if (isStatus) {
			model.addAttribute("emsg", "Duplicate Email Found");
		} else {
			boolean isSaveUser = userService.saveUser(registerFormDTO);
			if (isSaveUser) {
				model.addAttribute("smsg", "Registeartion is sucessfull,Plese check your mail");
			} else {
				model.addAttribute("emsg", "Registeration is not Sucessfull");
			}
		}
		model.addAttribute("countries", userService.getCountries());
		return "register";
	}

	@GetMapping("/")
	public String index(Model model) {
		LoginFormDTO loginFormDTO = new LoginFormDTO();
		model.addAttribute("loginForm", loginFormDTO);
		return "login";
	}

	@PostMapping("/login")
	public String handleUserLogin(LoginFormDTO loginFormDTO, Model model) {
		UserDTO userDTO = userService.login(loginFormDTO);

		if (userDTO == null) {
			model.addAttribute("emsg", "Invalid Credentails");
		} else {
			// String updatedPassword = userDTO.getUpdatedPassword();
			if ("Yes".equals(userDTO.getUpdatedPassword())) {

				return "redirect:dashboard";
			} else {
				return "redirect:/reset-pwd-page?email=" + userDTO.getEmail();
			}
		}
		return "login";
	}

	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		QuoteApiResponseDTO quoteApiResponseDTO = dashboardService.getQuote();
		model.addAttribute("quote", quoteApiResponseDTO);
		return "dashboard";
	}

	@GetMapping("/reset-pwd-page")
	public String loadResetPwdPage(@RequestParam String email, Model model) {
		ResetPasswordFormDTO resetPasswordFormDTO = new ResetPasswordFormDTO();
		resetPasswordFormDTO.setEmail(email);
		model.addAttribute("resetpwd", resetPasswordFormDTO);
		return "resetPwd";
	}

	@PostMapping("/resetPwd")
	public String handlePwdReset(ResetPasswordFormDTO resetPasswordFormDTO, Model model) {

		boolean isresetPwd = userService.resetPwd(resetPasswordFormDTO);

		if (isresetPwd) {
			return "redirect:/dashboard";
		}
		return "resetPwd";
	}

//	@PostMapping("/check-email")
//    public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam("email") String email) {
//        boolean isDuplicate = userService.duplicateEmailCheck(email);
//        return new ResponseEntity<>(isDuplicate, HttpStatus.OK);
//    }
//	
//
//    @PostMapping("/save-user")
//    public ResponseEntity<String> registerUser(@RequestBody RegisterFormDTO regFormDTO) {
//        boolean isSaved = userService.saveUser(regFormDTO);
//        if (isSaved) {
//            return new ResponseEntity<>("User registered successfully.", HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("User registration failed.", HttpStatus.BAD_REQUEST);
//        }
//    }
//    
//    @PostMapping("login")
//    public ResponseEntity<UserDTO> loginUser(@RequestBody LoginFormDTO loginFormDTO) {
//        UserDTO user = userService.login(loginFormDTO);
//        if (user != null) {
//            return new ResponseEntity<>(user, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//    }
//    
//    @PostMapping("/reset")
//    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordFormDTO resetPasswordFormDTO) {
//        boolean isReset = userService.resetPwd(resetPasswordFormDTO);
//        if (isReset) {
//            return new ResponseEntity<>("Password reset successfully.", HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("Password reset failed.", HttpStatus.BAD_REQUEST);
//        }
//    }
}
