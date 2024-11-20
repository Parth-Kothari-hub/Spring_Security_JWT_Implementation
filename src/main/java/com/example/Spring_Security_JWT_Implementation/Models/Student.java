package com.example.Spring_Security_JWT_Implementation.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Student {
	
	private Integer id;
	private String name;
	private Integer marks;
}
