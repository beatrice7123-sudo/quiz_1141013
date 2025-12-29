package com.example.quiz_1141013.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.quiz_1141013.constants.ResMessage;
import com.example.quiz_1141013.constants.Type;
import com.example.quiz_1141013.dao.FillinDao;
import com.example.quiz_1141013.dao.QuestionDao;
import com.example.quiz_1141013.dao.UserDao;
import com.example.quiz_1141013.entity.Fillin;
import com.example.quiz_1141013.entity.FillinUser;
import com.example.quiz_1141013.entity.Question;
import com.example.quiz_1141013.entity.User;
import com.example.quiz_1141013.request.FillinReq;
import com.example.quiz_1141013.response.BasicRes;
import com.example.quiz_1141013.response.FeedbackRes;
import com.example.quiz_1141013.response.FillinRecordRes;
import com.example.quiz_1141013.response.FillinUserRes;
import com.example.quiz_1141013.response.GetTextAnsRes;
import com.example.quiz_1141013.vo.AnswerVo;
import com.example.quiz_1141013.vo.Answers;
import com.example.quiz_1141013.vo.Options;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class FillinService {
	private ObjectMapper mapper=new ObjectMapper();

	@Autowired
	private FillinDao fillinDao;
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private UserDao userDao;
	
	@Transactional(rollbackOn = Exception.class)
	public BasicRes fillin(FillinReq req) throws Exception {
		// TODO 使用 email 取得 User 資料
		/* 使用 quizId 取得問卷所有問題 */
		List<Question> questionList = questionDao.getByQuizId(req.getQuizId());
		/* CollectionUtils.isEmpty(): 有檢查 list 是否為 null */
		if (CollectionUtils.isEmpty(questionList)) {
			return new BasicRes(ResMessage.QUESTION_NOT_FOUND.getCode(), //
					ResMessage.QUESTION_NOT_FOUND.getMessage());
		}
		/* 把 answersList 轉成 Map<QuestionId, List<AnswerVo>> */
		Map<Integer, List<AnswerVo>> quesIdAnsMap = new HashMap<>();
		for (Answers item : req.getAnswers()) {
			quesIdAnsMap.put(item.getQuestionId(), item.getAnswerVoList());
		}
		for(Question question : questionList) {
			//取當前題目的答案串
			List<AnswerVo> voList = quesIdAnsMap.get(question.getQuestionId());
			/*必填但沒答案或選項*/
			if(question.isRequired() && CollectionUtils.isEmpty(voList)) {
				return new BasicRes(ResMessage.ANSWER_REQUIRED.getCode(), //
						ResMessage.ANSWER_REQUIRED.getMessage());
			}
			/* 跳過簡答題*/
			if(question.getType().equals(Type.TEXT.getType())) {
				continue;
			}
			/* 把 字串options 轉成 物件Options */
			try {
				//取當前題目的所有選項，並轉成物件
				List<Options> opList = mapper.readValue(question.getOptions(), new TypeReference<>() {});
				//當前題目答案串不為null且 每個單題答案串都合法
//				System.out.println(
//					    new ObjectMapper().writeValueAsString(voList)
//					);
				boolean isMatch = voList != null && voList.stream().allMatch(vo -> isSameOption.test(vo, opList));
				if(!isMatch) {
					return new BasicRes(ResMessage.OPTION_NAME_MISMATCH.getCode(), //
							ResMessage.OPTION_NAME_MISMATCH.getMessage());
				}
			} catch (Exception e) {
				throw e;
			}
		}
		//寫資料
		for(int questionId:quesIdAnsMap.keySet()) {
			List<AnswerVo> currentAnswers=quesIdAnsMap.get(questionId);
			if (CollectionUtils.isEmpty(currentAnswers)) {
		        continue;
		    }
			boolean isAllBlank = currentAnswers.stream()
		            .allMatch(vo -> vo.getOptionName() == null || vo.getOptionName().trim().isEmpty());
		    if (isAllBlank) {
		        continue; // 如果這題的所有答案內容都是空的，就跳過不存資料庫
		    }
			try {
				fillinDao.insert(req.getQuizId(), questionId, req.getEmail(),
						mapper.writeValueAsString(quesIdAnsMap.get(questionId)));
			} catch (Exception e) {
				throw e;
			}
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	private BiPredicate<AnswerVo, List<Options>> isSameOption = (ans, opList) -> {
		if(ans.getCode()==0) {
			return true;
		}else {
			//單題答案為null或單題選項為空時回傳false
			if (ans == null || CollectionUtils.isEmpty(opList)) {
				return false;
			}
			/* 比對選項編號一樣時，選項是否一樣 */
			for(Options op : opList) {
				if(ans.getCode() == op.getCode() && !ans.getOptionName().equals(op.getOptionName())) {
					return false;
				}
			}
			return true;
		}
	};
	
	public GetTextAnsRes getTextAns(int quizId) throws Exception {
		List<Fillin> fillinList=fillinDao.getByQuizId(quizId);
		List<AnswerVo> allObjects = new ArrayList<>();
		for(Fillin item:fillinList) {
			try {
				List<AnswerVo> ansList=mapper.readValue(item.getAnswer(), new TypeReference<>() {
				});
				if (ansList != null) {
		            allObjects.addAll(ansList);
		        }
			} catch (Exception e) {
				throw e;
			}
		}
		List<String> textAns=new ArrayList<>();
		for(AnswerVo item:allObjects) {
			if(item.getCode()==0) {
				textAns.add(item.getOptionName());
			}
		}
		return new GetTextAnsRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), textAns);
	}
	
	public FillinUserRes fillinUser(int quizId) {
		List<Fillin> fillinList=fillinDao.getByQuizId(quizId);
		List<FillinUser> fillinUser=new ArrayList<>();
		for(Fillin item:fillinList) {
			String name = userDao.getUser(item.getEmail()).getName();
			String email = item.getEmail();
		    LocalDate date = item.getFillinDate();

		    boolean exists = fillinUser.stream().anyMatch(u ->
		        u.getName().equals(name) &&
		        u.getEmail().equals(email) &&
		        u.getFillinDate().equals(date)
		    );

		    if (!exists) {
		        fillinUser.add(new FillinUser(name, email, date));
		    }
		}
		return new FillinUserRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), fillinUser);
	}
//	public FillinRecordRes getByQuizIdEmail(int quizId, String email) throws Exception {
//		List<Fillin> fillinList=fillinDao.getByQuizIdEmail(quizId, email);
//		for(Fillin item:fillinList) {
//			try {
//				List<AnswerVo> voList = mapper.readValue(item.getAnswer(), new TypeReference<>() {
//				});
//			} catch (Exception e) {
//				throw e;
//			}
//		}
//		return new FillinRecordRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), fillinList);
//	}
}
