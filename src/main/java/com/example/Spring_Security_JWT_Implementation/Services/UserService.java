package com.example.Spring_Security_JWT_Implementation.Services;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.Spring_Security_JWT_Implementation.Entities.Users;
import com.example.Spring_Security_JWT_Implementation.Repository.UserRepo;

@Service
public class UserService {
	
	@Autowired
	private UserRepo repo;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtService jwtService;
	
	//Encrypt password
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	public Users registerUser(Users user) {
		//Password encrypt
		user.setPassword(encoder.encode(user.getPassword()));
		Users add = repo.save(user);
		return add;
	}

	public String verify(Users user) {
		Authentication authentication = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
		
		if(authentication.isAuthenticated()) {
			return jwtService.generateToken(user.getUserName());
		}
		
		return "Fail";
		
	}

}
