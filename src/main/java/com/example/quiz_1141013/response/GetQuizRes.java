package com.example.quiz_1141013.response;

import java.util.List;

import com.example.quiz_1141013.entity.Quiz;
import com.example.quiz_1141013.vo.QuizVo;

public class GetQuizRes extends BasicRes{
	private Quiz quiz;

	public GetQuizRes() {
		super();
	}

	public GetQuizRes(int code, String message) {
		super(code, message);
	}

	public GetQuizRes(Quiz quiz) {
		super();
		this.quiz = quiz;
	}

	public GetQuizRes(int code, String message, Quiz quiz) {
		super(code, message);
		this.quiz = quiz;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}
}
