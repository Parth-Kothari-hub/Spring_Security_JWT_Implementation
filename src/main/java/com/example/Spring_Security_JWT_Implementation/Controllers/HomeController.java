package com.example.Spring_Security_JWT_Implementation.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HomeController {

	@GetMapping("/")
	public String greet(HttpServletRequest request) {
		return "Hello " + request.getSession().getId();
	}
}
