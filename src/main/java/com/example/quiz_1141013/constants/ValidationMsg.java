package com.example.quiz_1141013.constants;

public class ValidationMsg {
	//加上final是因為使用在@Validation中的message限制
	//加上static是為了方便直接透過ValidationMsg. 來呼叫此常數變數
	public static final String TITLE_ERROR="Title error.";
	public static final String DESCRIPTION_ERROR="Description error.";
	public static final String STARTDATE_ERROR="Start date error.";
	public static final String ENDDATE_ERROR="End date error.";
	public static final String QUESTION_ERROR="Question error.";
	public static final String TYPE_ERROR="Type error.";
	public static final String QUIZID_ERROR="Quiz ID error.";
	
	public static final String NAME_ERROR="Name error.";
	public static final String EMAIL_ERROR="Email error.";
	public static final String PHONE_ERROR="Phone error.";
	public static final String BIRTH_DATE_ERROR="Birth date error.";
	public static final String GENDER_ERROR="Gender error.";
	public static final String PASSWORD_ERROR="Password error.";
	public static final String ADMIN_ERROR="Admin error.";
}
