package com.example.quiz_1141013.request;

import java.time.LocalDate;

import com.example.quiz_1141013.constants.ValidationMsg;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public class CreateUserReq {
	@NotBlank(message = ValidationMsg.USER_NAME_IS_EMPTY)
	private String name;
	@Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*?_]{8,16}$", message = ValidationMsg.USER_PASSWORD_ERROR)
	@NotBlank(message = ValidationMsg.USER_PASSWORD_IS_EMPTY)
	private String password;
	@Pattern(regexp = "^09\\d{8}$", message = ValidationMsg.USER_PHONE_ERROR)
	@NotBlank(message = ValidationMsg.USER_PHONE_ERROR)
	private String phone;
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = ValidationMsg.EMAIL_ERROR)
	@NotBlank(message = ValidationMsg.EMAIL_IS_EMPTY)
	private String email;
	@Past(message = ValidationMsg.USER_BIRTDATE_ERROR)
	@NotNull(message = ValidationMsg.USER_BIRTDATE_ERROR)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;
	@NotBlank(message = ValidationMsg.USER_GENDER_IS_EMPTY)
	private String gender;
	private boolean admin;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
