package com.example.quiz_1141013.response;

import java.util.List;

import com.example.quiz_1141013.request.FillinReq;
import com.example.quiz_1141013.vo.Answers;

public class Feedback extends FillinReq{

	public Feedback() {
		super();
	}

	public Feedback(String name, String phone, String email, int age, int quizId, List<Answers> answers) {
		super(name, phone, email, age, quizId, answers);
	}
	
}
