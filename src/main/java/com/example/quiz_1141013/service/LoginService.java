package com.example.quiz_1141013.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quiz_1141013.constants.ResMessage;
import com.example.quiz_1141013.dao.UserDao;
import com.example.quiz_1141013.entity.User;
import com.example.quiz_1141013.request.CreateUserReq;
import com.example.quiz_1141013.request.LoginReq;
import com.example.quiz_1141013.response.BasicRes;
import com.example.quiz_1141013.response.LoginRes;

@Service
public class LoginService {
	@Autowired
	private UserDao userDao;
	
	public BasicRes register(CreateUserReq req) {
		userDao.register(req.getName(), req.getPassword(), req.getPhone(), req.getEmail(), //
				req.getBirthDate(), req.getGender(), req.isAdmin());
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	
	public LoginRes login(LoginReq req) {
		User user=userDao.getUser(req.getEmail());
			if(!req.getEmail().equals(user.getEmail())) {
				return new LoginRes(ResMessage.EMAIL_MISMATCH.getCode(), ResMessage.EMAIL_MISMATCH.getMessage());
			}
			if(!req.getPassword().equals(user.getPassword())) {
				return new LoginRes(ResMessage.PASSWORD_ERROR.getCode(), ResMessage.PASSWORD_ERROR.getMessage());
			}
		return new LoginRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), user.getName(), //
				user.getPhone(), user.getEmail(), user.getBirthDate(), user.getGender(), user.isAdmin());
	}
}
