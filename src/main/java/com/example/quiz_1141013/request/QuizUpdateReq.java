package com.example.quiz_1141013.request;

import com.example.quiz_1141013.constants.ValidationMsg;

import jakarta.validation.constraints.Min;

public class QuizUpdateReq extends QuizCreateReq{
	//因為是新已存在的問卷，所以id至少是1
	@Min(value=1, message=ValidationMsg.QUIZID_ERROR)
	private int quizId;

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}
}
