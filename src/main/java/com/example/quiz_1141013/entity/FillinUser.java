package com.example.quiz_1141013.entity;

import java.time.LocalDate;

public class FillinUser {
	private String name;
	private String email;
	private LocalDate fillinDate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDate getFillinDate() {
		return fillinDate;
	}
	public void setFillinDate(LocalDate fillinDate) {
		this.fillinDate = fillinDate;
	}
	
	public FillinUser(String name, String email, LocalDate fillinDate) {
		super();
		this.name = name;
		this.email = email;
		this.fillinDate = fillinDate;
	}
	
	
}
