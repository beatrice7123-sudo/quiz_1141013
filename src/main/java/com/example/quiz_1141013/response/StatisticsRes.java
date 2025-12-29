package com.example.quiz_1141013.response;

import java.util.List;

import com.example.quiz_1141013.vo.Statistics;

public class StatisticsRes extends BasicRes {
	private List<Statistics> statisticsList;
	private int fillinTotal;

	public StatisticsRes() {
		super();
	}

	public StatisticsRes(int code, String message) {
		super(code, message);
	}

//	public StatisticsRes(int code, String message, List<Statistics> statisticsList) {
//		super(code, message);
//		this.statisticsList = statisticsList;
//	}

	public StatisticsRes(int code, String message, List<Statistics> statisticsList, int fillinTotal) {
		super(code, message);
		this.statisticsList = statisticsList;
		this.fillinTotal = fillinTotal;
	}

	public List<Statistics> getStatisticsList() {
		return statisticsList;
	}

	public void setStatisticsList(List<Statistics> statisticsList) {
		this.statisticsList = statisticsList;
	}
	public int getFillinTotal() {
		return fillinTotal;
	}
	public void setFillinTotal(int fillinTotal) {
		this.fillinTotal = fillinTotal;
	}
}
