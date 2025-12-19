package com.example.quiz_1141013.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name="member")
public class Member {
	@Id
	@Column(name="id")
	private int userId;
	@Column(name="name")
	private String name;
	@Column(name="email")
	private String email;
	@Column(name="phone")
	private String phone;
	@Column(name="birth_date")
	private LocalDate birthDate;
	@Column(name="gender")
	private String gender;
	@Column(name="password")
	private String password;
	@Column(name="admin")
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
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}
