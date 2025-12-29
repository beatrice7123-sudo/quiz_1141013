package com.example.quiz_1141013.response;

import java.util.List;

public class GetTextAnsRes extends BasicRes{
	private List<String> textAns;

	public GetTextAnsRes() {
		super();
	}

	public GetTextAnsRes(int code, String message) {
		super(code, message);
	}

	public GetTextAnsRes(int code, String message, List<String> textAns) {
		super(code, message);
		this.textAns = textAns;
	}

	public List<String> getTextAns() {
		return textAns;
	}

	public void setTextAns(List<String> textAns) {
		this.textAns = textAns;
	}
}
