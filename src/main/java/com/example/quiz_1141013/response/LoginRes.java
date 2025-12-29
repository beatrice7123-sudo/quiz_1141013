package com.example.quiz_1141013.response;

import java.time.LocalDate;

import com.example.quiz_1141013.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class LoginRes extends BasicRes{
	private String name;
	private String phone;
	private String email;
	private LocalDate birthDate;
	private String gender;
	private boolean admin;
	
	public LoginRes() {
		super();
	}
	public LoginRes(int code, String message) {
		super(code, message);
	}
	public LoginRes(int code, String message, String name, String phone, String email, LocalDate birthDate,
			String gender, boolean admin) {
		super(code, message);
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.birthDate = birthDate;
		this.gender = gender;
		this.admin = admin;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
}
