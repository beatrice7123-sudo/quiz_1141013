package com.example.quiz_1141013.response;

import java.time.LocalDate;
import java.util.List;

import com.example.quiz_1141013.request.FillinReq;
import com.example.quiz_1141013.vo.Answers;

public class Feedback extends FillinReq{
	private LocalDate birthDate;
	
	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public Feedback() {
		super();
	}

	public Feedback(String name, String phone, String email, int quizId, List<Answers> answers) {
		super(name, phone, email, quizId, answers);
	}
	
	public Feedback(String name, String phone, String email, int quizId, List<Answers> answers, LocalDate birthDate) {
		super(name, phone, email, quizId, answers);
		this.birthDate = birthDate;
	}

}
