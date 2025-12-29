package com.example.quiz_1141013.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.quiz_1141013.entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserDao extends JpaRepository<User, String> {
	@Modifying
	@Transactional
	@Query(value="insert into user (name, password, phone, email, birth_date, gender, admin) "//
			+ " values (?1, ?2, ?3, ?4, ?5, ?6, ?7)", nativeQuery = true)
	public void register(String name, String password, String phone, String email, LocalDate birthDate, String gender, boolean admin);
	
	@Query(value="select * from user where email=?", nativeQuery = true)
	public User getUser(String email);
	
	@Query(value="select * from user where email in (?)", nativeQuery = true)
	public List<User> getUsersIn(List<String> emailList);
}
