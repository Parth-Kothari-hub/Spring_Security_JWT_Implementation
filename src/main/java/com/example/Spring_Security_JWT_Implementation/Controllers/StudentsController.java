package com.example.Spring_Security_JWT_Implementation.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Spring_Security_JWT_Implementation.Models.Student;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class StudentsController {

	private List<Student> students = new ArrayList<>(
			List.of(new Student(1, "Navin", 33), new Student(2, "Pravin", 66)));

	@GetMapping("/students")
	public List<Student> getStudents() {
		return students;
	}

	@GetMapping("/csrfToken")
	public CsrfToken getCsrfToken(HttpServletRequest request) {
		return (CsrfToken) request.getAttribute("_csrf");
	}
	
	@PostMapping("/add/student")
	public List<Student> addStudent(Student student) {
		boolean add = students.add(student);
		return students;		
	}
}
