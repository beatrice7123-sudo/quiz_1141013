package com.example.quiz_1141013.response;

import java.time.LocalDate;
import java.util.List;

import com.example.quiz_1141013.entity.Fillin;
import com.example.quiz_1141013.vo.AnswerVo;

public class FillinRecordRes extends BasicRes{
	private List<Fillin> fillinList;

	public FillinRecordRes() {
		super();
	}

	public FillinRecordRes(int code, String message) {
		super(code, message);
	}

	public FillinRecordRes(int code, String message, List<Fillin> fillinList) {
		super(code, message);
		this.fillinList = fillinList;
	}

	public List<Fillin> getFillinList() {
		return fillinList;
	}

	public void setFillinList(List<Fillin> fillinList) {
		this.fillinList = fillinList;
	}
}
