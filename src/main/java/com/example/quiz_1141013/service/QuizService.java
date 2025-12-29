package com.example.quiz_1141013.service;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.quiz_1141013.constants.ResMessage;
import com.example.quiz_1141013.constants.Type;
import com.example.quiz_1141013.dao.MemberDao;
import com.example.quiz_1141013.dao.QuestionDao;
import com.example.quiz_1141013.dao.QuizDao;
import com.example.quiz_1141013.entity.Question;
import com.example.quiz_1141013.entity.Quiz;
import com.example.quiz_1141013.request.QuizCreateReq;
import com.example.quiz_1141013.request.QuizDeleteReq;
import com.example.quiz_1141013.request.QuizUpdateReq;
import com.example.quiz_1141013.response.BasicRes;
import com.example.quiz_1141013.response.GetListRes;
import com.example.quiz_1141013.response.GetQuestionRes;
import com.example.quiz_1141013.response.GetQuizRes;
import com.example.quiz_1141013.vo.Options;
import com.example.quiz_1141013.vo.QuestionVo;
import com.example.quiz_1141013.vo.QuizVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QuizService {
	private ObjectMapper mapper=new ObjectMapper();
	@Autowired
	private QuizDao quizDao;
	@Autowired
	private QuestionDao questionDao;
	@Autowired
	private MemberDao memberDao;
	
	private BasicRes check(QuizCreateReq req) {
		//驗證開始時間是否在結束時間之前 或 開始時間比當天早
		if(req.getStartDate().isAfter(req.getEndDate()) || req.getStartDate().isBefore(LocalDate.now())) {
			return new BasicRes(ResMessage.DATE_ERROR.getCode(), ResMessage.DATE_ERROR.getMessage());
		}
		List<QuestionVo> voList=req.getQuestionVoList();
		for(QuestionVo v:voList) {
			//排除非指定的題目類型
			if(!Type.checkType(v.getType())) {
				return new BasicRes(ResMessage.TYPE_ERROR.getCode(), ResMessage.TYPE_ERROR.getMessage());
			}
			//type為單選或多選
			if(Type.isChosenType(v.getType())) {
				if(v.getOptionsList().size()<1) {
					return new BasicRes(ResMessage.OPTIONS_SIZE_ERROR.getCode(),//
							ResMessage.OPTIONS_SIZE_ERROR.getMessage());
				}
			}else {
				//type為簡答題
				if(!v.getOptionsList().isEmpty()) {
					return new BasicRes(ResMessage.OPTIONS_SIZE_ERROR.getCode(),//
							ResMessage.OPTIONS_SIZE_ERROR.getMessage());
				}
			}
		}
		return null;
	}
	
	//表示只要發生exception，會回朔(rollback)
	@Transactional(rollbackFor = Exception.class)
	public BasicRes create(QuizCreateReq req) throws Exception {
		BasicRes checkRes=check(req);
		//方法check的結果只會有2種 => null和非null(BasicRes)，非null的結果表示檢查有誤
		if(checkRes!=null) {
			//把檢查有誤的結果直接return出去
			return checkRes;
		}
		//新增問卷
		quizDao.addQuiz(req.getTitle(), req.getDescription(), req.getStartDate(), req.getEndDate(), req.isPublished());
		//取得最新quiz_id編號
		//將question寫進DB
		int quizId=quizDao.getMaxId();
		for(QuestionVo v:req.getQuestionVoList()) {
			//把v中的List<Options>轉換成字串
			try {
				String optionsListStr=mapper.writeValueAsString(v.getOptionsList());
				questionDao.addQuestion(quizId, v.getQuestionId(), v.getQuestion(),
						v.getType(), v.isRequired(), optionsListStr);
			} catch (Exception e) {
				throw e;
			}
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	
	@Transactional(rollbackFor = Exception.class)
	public BasicRes update(QuizUpdateReq req) throws Exception {
		/* 方法 check 中的參數資料型態是 QuizCreateReq，對 QuizUpdateReq 來說是父類別，
		 * 若把子類別 QuizUpdateReq 當參數放到 check 中，其資料型態會自動轉型成父類別 QuizCreateReq，
		 * 即 check((QuizCreateReq)req)，這樣的結果查別只是在於子類別中的屬性 quizId 都會是預設值 0，
		 * 但不影響方法 check 的檢查，因為沒用到 quizId */
		BasicRes checkRes=check(req);
		//方法check的結果只會有2種 => null和非null(BasicRes)，非null的結果表示檢查有誤
		if(checkRes!=null) {
			//把檢查有誤的結果直接return出去
			return checkRes;
		}
		//檢查quizId和QuestionVo中的quizId是否相同
		for(QuestionVo vo:req.getQuestionVoList()) {
			if(req.getQuizId()!=vo.getQuizId()) {
				return new BasicRes(ResMessage.QUIZID_MISMATCH.getCode(), ResMessage.QUIZID_MISMATCH.getMessage());
			}
		}
		//更新quiz
		int updateRes=quizDao.update(req.getQuizId(), req.getTitle(), req.getDescription(), req.getStartDate(), req.getEndDate(), req.isPublished());
		//有找到quizId並更新成功(即使更新的資料和之前一樣)，會回傳1()
		if(updateRes!=1) {
			return new BasicRes(ResMessage.QUIZ_NOT_FOUND.getCode(), ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		//確定quizId有存在，就先刪掉全部問題，再新增問題，避免questionId錯亂
		questionDao.deleteByQuizId(req.getQuizId());
		//新增問題
		for(QuestionVo v:req.getQuestionVoList()) {
			//把v中的List<Options>轉換成字串
			try {
				String optionsListStr=mapper.writeValueAsString(v.getOptionsList());
				questionDao.addQuestion(v.getQuizId(), v.getQuestionId(), v.getQuestion(),
						v.getType(), v.isRequired(), optionsListStr);
			} catch (Exception e) {
				throw e;
			}
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	
	public BasicRes delete(List<Integer> quizId) {
		//檢查題目是否存在
		for(Integer q:quizId) {
			if(quizDao.getByQuizId(q)==null) {
				return new BasicRes(ResMessage.QUIZID_MISMATCH.getCode(), ResMessage.QUIZID_MISMATCH.getMessage());
			}else {
				//刪除quiz
				quizDao.delete(q);
				//確定quizId有存在，就刪掉全部問題
				questionDao.deleteByQuizId(q);
			}
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	
	public GetListRes getAll() {
		return new GetListRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(),//
				quizDao.getAll());
	}
	
//	public BasicRes createMember(MemberCreateReq req) {
//		if(req.getName()==null) {
//			return new BasicRes(ResMessage.NAME_ERROR.getCode(), ResMessage.NAME_ERROR.getMessage());
//		}
//		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
//	}
	
	public GetQuestionRes getQuestionByQuizId(int quizId) throws Exception {
		List<Question> list=questionDao.getByQuizId(quizId);
		List<QuestionVo> questionVoList=new ArrayList<>();
		//把Question中的每個字串Options轉換成字定義的物件Options
		for(Question item:list) {
			try {
				List<Options> opList=mapper.readValue(item.getOptions(), new TypeReference<>(){});
				//把Question 中的每一個值和opList，set 到QuestionVo對應的屬性位置
				QuestionVo vo=new QuestionVo(quizId, item.getQuestionId(), item.getQuestion(), item.getType(), item.isRequired(), opList);
				//把每個vo加到questionVoList
				questionVoList.add(vo);
			} catch (Exception e) {
				throw e;
			}
		}
		return new GetQuestionRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), questionVoList);
	}
	
	public GetQuizRes getQuizById(int quizId){
		Quiz quiz=quizDao.getByQuizId(quizId);
		return new GetQuizRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), quiz);
	}
	
	public GetListRes getAll(String keyword, LocalDate startDate, LocalDate endDate) {
		/* 把 keyword 是 null(沒有輸入值) 或 空字串 或全空白字串 轉換成空字串
		 * 目的是後面在取資料時會使用 like %%，%% 中間是空字串時，也是會撈全部*/
		if(!StringUtils.hasText(keyword)) {
			keyword="";
		}
		if(startDate==null) {
			startDate=LocalDate.of(1970, 1, 1);
		}
		if(endDate==null) {
			endDate=LocalDate.of(3000, 12, 31);
		}
		return new GetListRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(),//
				quizDao.getAll(keyword, startDate, endDate));
	}
}
