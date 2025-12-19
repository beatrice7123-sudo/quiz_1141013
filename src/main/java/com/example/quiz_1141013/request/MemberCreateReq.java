package com.example.quiz_1141013.request;

import java.time.LocalDate;

import com.example.quiz_1141013.constants.ValidationMsg;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class MemberCreateReq {
	@NotBlank(message = ValidationMsg.NAME_ERROR)
	private String name;
	@NotBlank(message=ValidationMsg.EMAIL_ERROR)
	@Pattern(regexp="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$\r\n")
	private String email;
	@NotBlank(message=ValidationMsg.PHONE_ERROR)
	private String phone;
	@NotNull(message = ValidationMsg.BIRTH_DATE_ERROR)
	private LocalDate birthDate;
	@NotBlank(message = ValidationMsg.GENDER_ERROR)
	private String gender;
	@NotBlank(message = ValidationMsg.PASSWORD_ERROR)
	@Pattern(regexp="\\w{8,16}")
	private String password;
	private boolean admin;
	
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}
