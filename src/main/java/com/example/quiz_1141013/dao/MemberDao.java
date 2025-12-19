package com.example.quiz_1141013.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.quiz_1141013.entity.Member;

import jakarta.transaction.Transactional;

@Repository
public interface MemberDao extends JpaRepository<Member, Integer>{
	@Modifying
	@Transactional
	@Query(value="insert into member (id, name, email, phone, birth_date, gender, password, admin) values(?1, ?2, ?3, ?4, ?5, ?6, ?7)", nativeQuery = true)
	public void createMember(int id, String name, String email, String phone, String birthDate, String gender, String password, boolean admin);
}
