package com.example.quiz_1141013.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz_1141013.request.QuizCreateReq;
import com.example.quiz_1141013.request.QuizUpdateReq;
import com.example.quiz_1141013.response.BasicRes;
import com.example.quiz_1141013.response.GetListRes;
import com.example.quiz_1141013.response.GetQuestionRes;
import com.example.quiz_1141013.response.GetQuizRes;
import com.example.quiz_1141013.service.QuizService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin("http://localhost:4200")
public class QuizController {
	@Autowired
	private QuizService quizService;
	
	@PostMapping("quiz/create")
	public BasicRes create(@Valid @RequestBody QuizCreateReq req) throws Exception{
		return quizService.create(req);
	}
	@PostMapping("quiz/update")
	public BasicRes update(@Valid @RequestBody QuizUpdateReq req) throws Exception{
		return quizService.update(req);
	}
	
	@GetMapping("quiz/getAll")
	public GetListRes getAll() {
		return quizService.getAll();
	}
	//http://localhost:8080/quiz/get_quiz?id=1
	@GetMapping("quiz/get_quiz")
	public GetQuizRes getQuizById(@RequestParam("id") int quizId){
		return quizService.getQuizById(quizId);
	}
	//http://localhost:8080/quiz/get_questions?quizId=1
	@GetMapping("quiz/get_questions")
	public GetQuestionRes getQuestionByQuizId(@RequestParam("quizId") int quizId) throws Exception{
		return quizService.getQuestionByQuizId(quizId);
	}
	
	@GetMapping("quiz/get_fillter_data")
	public GetListRes getAll(
			@RequestParam("keyword") String keyword,
			@RequestParam("startDate") LocalDate startDate,
			@RequestParam("endDate") LocalDate endDate) {
		return quizService.getAll(keyword, startDate, endDate);
	}
}
