package com.example.quiz_1141013.request;

import com.example.quiz_1141013.constants.ValidationMsg;
import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.Min;

public class QuizUpdateReq extends QuizCreateReq{
	//因傳進來的主層是quiz裡的id，真正的quizId是包在questionVoList裡的
	//避免報錯，再加上當初設計quiz Table裡的id會等於question Table裡的quiz_id
	//用	@JsonAlias(value = "id")來認物件
	@JsonAlias(value = "id")
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
