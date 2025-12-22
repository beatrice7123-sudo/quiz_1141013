package com.example.quiz_1141013.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.quiz_1141013.constants.ResMessage;
import com.example.quiz_1141013.dao.FillinDao;
import com.example.quiz_1141013.dao.UserDao;
import com.example.quiz_1141013.entity.Fillin;
import com.example.quiz_1141013.entity.User;
import com.example.quiz_1141013.response.BasicRes;
import com.example.quiz_1141013.response.Feedback;
import com.example.quiz_1141013.response.FeedbackRes;
import com.example.quiz_1141013.vo.AnswerVo;
import com.example.quiz_1141013.vo.Answers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FeedbackService {
	private ObjectMapper mapper=new ObjectMapper();
	
	@Autowired
	private FillinDao fillinDao;
	@Autowired
	private UserDao userDao;
	
	public FeedbackRes feedback(int quizId) throws Exception {
		//res包含了多位使用者(email)填的答案
		List<Fillin> res=fillinDao.getByQuizId(quizId);
		/* Map<email, List<Answers>> */
		Map<String, List<Answers>> map=new HashMap<>();
		List<Answers> ansList=new ArrayList<>();
		for(Fillin item : res) {
			try {
				//把 字串answer 轉成 物件List<AnswerVo>
				//一個List<AnswerVo> 只包含了一個問題的所有編號-選項
				List<AnswerVo> voList = mapper.readValue(item.getAnswer(), new TypeReference<>() {});
				Answers ans=new Answers(item.getQuestionId(), voList);
				//把相同email對應的List<Answers>取出
				ansList=map.get(item.getEmail());
				if(CollectionUtils.isEmpty(ansList)) {
					//如果判斷為真 --> 表示map中沒有該位使用者的email
					//清掉原本的ansList
					ansList=new ArrayList<>();
				}
				ansList.add(ans);
				map.put(item.getEmail(), ansList);
			} catch (Exception e) {
				throw e;
			}
		}
		List<Feedback> feedbackList= new ArrayList<>();
		for(String email:map.keySet()) {
			User user=userDao.getUser(email);
			feedbackList.add(new Feedback(user.getName(), user.getPhone(), email, //
					user.getAge(), quizId, map.get(email)));
		}
		return new FeedbackRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), feedbackList);
	}
	//一次性撈取所有email對應的user資訊
	private List<Feedback> getFeedbackList(List<Fillin> fillinList, int quizId, Map<String, List<Answers>> map){
		//蒐集同一張問卷下的所有email
		List<String> emailList=new ArrayList<>();
		fillinList.forEach(item->{
			emailList.add(item.getEmail());
		});
		//一次性撈取所有包含email的user資訊
		List<User> userList = userDao.getUsersIn(emailList);
		//生成所有feedbackRes
		List<Feedback> feedbackList=new ArrayList<>();
		userList.forEach(item->{
			feedbackList.add(new Feedback(item.getName(), item.getPhone(), item.getEmail(), //
					item.getAge(), quizId, map.get(item.getEmail())));
		});
		return feedbackList;
	}
}
