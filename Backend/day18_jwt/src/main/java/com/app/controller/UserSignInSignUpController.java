package com.app.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;

import com.app.dto.AdminSignUp;
import com.app.dto.SigninRequest;
import com.app.dto.SigninResponse;
import com.app.dto.StudentSignUp;
import com.app.dto.TeacherSignUp;
import com.app.security.CustomUserDetails;
import com.app.security.JwtUtils;
import com.app.service.UserService;

import io.swagger.v3.oas.models.responses.ApiResponse;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserSignInSignUpController {
	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtils utils;

	@Autowired
	private AuthenticationManager mgr;

	// sign up
	@PostMapping(value = "/student_signup")
	public ResponseEntity<?> StudentSignup(@RequestBody @Valid StudentSignUp dto) {
		System.out.println("in sign up " + dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.studentRegistration(dto));
	}

	@PostMapping("/teacher_signup")
	public ResponseEntity<?> teacherSignup(@RequestBody @Valid TeacherSignUp dto) {
		System.out.println("in sign up " + dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.teacherRegistration(dto));
	}

	@PostMapping("/admin_signup")
	public ResponseEntity<?> AdminSignup(@RequestBody @Valid AdminSignUp dto) {
		System.out.println("in sign up " + dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.adminRegistration(dto));
	}
	/*
	 * request payload : Auth req DTO : email n password resp payload : In case of
	 * success : Auth Resp DTO : mesg + JWT token + SC 200 IN case of failure : SC
	 * 401
	 */
	@PostMapping("/signin")
	public ResponseEntity<?> signinUser(@RequestBody @Valid SigninRequest reqDTO) { // signin  to check 
		System.out.println("in signin " + reqDTO);
		// simply invoke authentucate(...) on AuthMgr
		// i/p : Authentication => un verifed credentials
		// i/f --> Authentication --> imple by UsernamePasswordAuthToken
		// throws exc OR rets : verified credentials (UserDetails i.pl class: custom
		// user details)
		try {
			Authentication verifiedAuth = mgr
					.authenticate(new UsernamePasswordAuthenticationToken(reqDTO.getEmail(), reqDTO.getPassword()));
			System.out.println(verifiedAuth.getClass());// Custom user details
			// => auth success
				
			String token = utils.generateJwtToken(verifiedAuth);
			
			return ResponseEntity
					.ok(new SigninResponse(token, "Successful Authentication!!"));
		} catch (Exception e) {
			SigninResponse err = new SigninResponse("","Inavalid credentials!!");
			return ResponseEntity.ok(err);
		}
	}

}
