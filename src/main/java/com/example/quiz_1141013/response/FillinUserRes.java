package com.example.quiz_1141013.response;

import java.util.List;

import com.example.quiz_1141013.entity.FillinUser;

public class FillinUserRes extends BasicRes{
	private List<FillinUser> fillinUser;

	public List<FillinUser> getFillinUser() {
		return fillinUser;
	}

	public void setFillinUser(List<FillinUser> fillinUser) {
		this.fillinUser = fillinUser;
	}

	public FillinUserRes() {
		super();
	}

	public FillinUserRes(int code, String message) {
		super(code, message);
	}

	public FillinUserRes(int code, String message, List<FillinUser> fillinUser) {
		super(code, message);
		this.fillinUser = fillinUser;
	}
}
