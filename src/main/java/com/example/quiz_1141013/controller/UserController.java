package com.example.quiz_1141013.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz_1141013.request.CreateUserReq;
import com.example.quiz_1141013.request.LoginReq;
import com.example.quiz_1141013.response.BasicRes;
import com.example.quiz_1141013.response.LoginRes;
import com.example.quiz_1141013.service.LoginService;

import jakarta.validation.Valid;

@RestController
public class UserController {
	@Autowired
	private LoginService loginService;
	
	@PostMapping("quiz/register")
	public BasicRes register(@Valid @RequestBody CreateUserReq req) {
		return loginService.register(req);
	}
	
	@PostMapping("quiz/login")
	public LoginRes login(@Valid @RequestBody LoginReq req) {
		return loginService.login(req);
	}
}
