package com.example.quiz_1141013.vo;

import java.util.ArrayList;
import java.util.List;

import com.example.quiz_1141013.constants.ValidationMsg;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

public class QuestionVo {
	private int quizId;
	private int questionId;
	@NotBlank(message=ValidationMsg.QUESTION_ERROR)
	private String question;
	@NotBlank(message=ValidationMsg.TYPE_ERROR)
	private String type;
	private boolean required;
	//不限制，因為有簡答題
	//給定空的list可預防這個屬性沒有被mapping到時會是null => 即預設值會從null變成List
	private List<Options> optionsList = new ArrayList<>();
	
	public int getQuizId() {
		return quizId;
	}
	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public List<Options> getOptionsList() {
		return optionsList;
	}
	public void setOptionsList(List<Options> optionsList) {
		this.optionsList = optionsList;
	}
	
	public QuestionVo() {
		super();
	}
	public QuestionVo(int quizId, int questionId, String question, String type, boolean required, List<Options> optionsList) {
		super();
		this.quizId = quizId;
		this.questionId = questionId;
		this.question = question;
		this.type = type;
		this.required = required;
		this.optionsList = optionsList;
	}
}
