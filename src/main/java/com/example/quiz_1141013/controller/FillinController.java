package com.example.quiz_1141013.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz_1141013.entity.Fillin;
import com.example.quiz_1141013.request.FillinReq;
import com.example.quiz_1141013.response.BasicRes;
import com.example.quiz_1141013.response.Feedback;
import com.example.quiz_1141013.response.FeedbackRes;
import com.example.quiz_1141013.response.FillinRecordRes;
import com.example.quiz_1141013.response.FillinUserRes;
import com.example.quiz_1141013.service.FeedbackService;
import com.example.quiz_1141013.service.FillinService;
import com.example.quiz_1141013.vo.Answers;

import jakarta.validation.Valid;

@RestController
public class FillinController {
	@Autowired
	private FillinService fillinService;
	@Autowired
	private FeedbackService feedbackService;
	
	@PostMapping("quiz/fillin")
	public BasicRes fillin(@Valid @RequestBody FillinReq req) throws Exception {
		return fillinService.fillin(req);
	}
	
	@GetMapping("quiz/statistics")
	public BasicRes statistics(@RequestParam("id") int quizId) throws Exception {
		return feedbackService.statistics2(quizId);
	}
	
	@GetMapping("quiz/text_ans")  //統計簡答題用
	public BasicRes getTextAns(@RequestParam("id") int quizId) throws Exception {
		return fillinService.getTextAns(quizId);
	}
	
//	@GetMapping("quiz/get_record")
//	public FillinRecordRes getRecord(@RequestParam("id") int quizId, @RequestParam("email") String email) {
//		return fillinService.getByQuizIdEmail(quizId, email);
//	}
	@GetMapping("quiz/feedback_list")
	public FeedbackRes feedback(@RequestParam("id") int quizId) throws Exception {
		return feedbackService.feedback(quizId);
	}
	
	@GetMapping("quiz/fillin_user")
	public FillinUserRes fillinUser(@RequestParam("id") int quizId) {
		return fillinService.fillinUser(quizId);
	}
}
