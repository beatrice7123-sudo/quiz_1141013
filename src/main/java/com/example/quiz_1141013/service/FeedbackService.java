package com.example.quiz_1141013.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.example.quiz_1141013.response.StatisticsRes;
import com.example.quiz_1141013.vo.AnswerVo;
import com.example.quiz_1141013.vo.Answers;
import com.example.quiz_1141013.vo.OptionsCount;
import com.example.quiz_1141013.vo.Statistics;
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
					quizId, map.get(email), user.getBirthDate()));
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
					quizId, map.get(item.getEmail()), item.getBirthDate()));
		});
		return feedbackList;
	}
	
//	public StatisticsRes statistics_test(int quizId) throws Exception {
//		//res包含了多位使用者(email)填的答案
//		List<Fillin> res=fillinDao.getByQuizId(quizId);
//		//Map<questionId, List<OptionsCount>>  =>  questionId、count、optionName、code
//		Map<Integer, List<OptionsCount>> map=new HashMap<>();
//		for(Fillin item:res) {
//			try {
//				//把 字串answer 轉成 物件List<AnswerVo>
//				//一個List<AnswerVo> 只包含了一個問題的所有編號-選項
//				List<AnswerVo> voList = mapper.readValue(item.getAnswer(), new TypeReference<>() {});
//				//voList轉成List<OptionsCount>
//				List<OptionsCount> opCountList=CollectionUtils.isEmpty(map.get(item.getQuestionId())) 
//						?  new ArrayList<>() : map.get(item.getQuestionId());
//				/* voList.forEach 遍歷後，opCountList 裡面會是同一個 questionId 下，所有的 code-optionName 以及是否有選(0:沒選；1:有選) 的結果。
//				 * 就是 1.紅茶 count=0, 2.綠茶 count=1, 3.烏龍茶 count=0, 4.奶茶 count=0 這4筆 OptionsCount 資料，
//				 * 所以當有第2位填答者的答案時，opCountList 的資料就會是第一位填答著的4筆再加上新的4筆總共8筆資料 */
//				voList.forEach(vo->{
//					OptionsCount opCount=new OptionsCount(vo.getCode(), vo.getOptionName(), vo.isCheck()?1:0 );
//					opCountList.add(opCount);
//				});
//				map.put(item.getQuestionId(), opCountList);
//			} catch (Exception e) {
//				throw e;
//			}
//		}
//		return null;
//	}
//	public StatisticsRes statistics(int quizId) throws Exception {
//		//res包含了多位使用者(email)填的答案
//		List<Fillin> res=fillinDao.getByQuizId(quizId);
//		//Map<questionId, Map<code-optionName, count>>  =>  questionId、count、code
//		Map<Integer, Map<String, Integer>> map=new HashMap<>();
//		//先將相同問題編號取出，次數尚未整理
//		for(Fillin item:res) {
//			try {
//				//把 字串answer 轉成 物件List<AnswerVo>
//				//一個List<AnswerVo> 只包含了一個問題的所有編號-選項
//				List<AnswerVo> voList = mapper.readValue(item.getAnswer(), new TypeReference<>() {});
//				//從voList蒐集code對應的check
//				//遍歷後，codeCountMap會有資料，編號1, count=0, 編號2, count=1, ....  
//				Map<String, Integer> codeCountMap=CollectionUtils.isEmpty(map.get(item.getQuestionId())) //
//						? new HashMap<>() : map.get(item.getQuestionId());
//				voList.forEach(vo -> {
//					/* 第一筆資料時，codeCountMap 使用 code 當key 取出對應的 value 肯定是 null，因為沒資料，會 null 的原因是 
//					 * codeCountMap 的資料型態是 Integer */
//					String str=String.valueOf(vo.getCode()+"-"+vo.getOptionName());
//					int count=codeCountMap.get(str)==null ? 0 : codeCountMap.get(str);
//					if(vo.isCheck()) {
//						count++;
//					}
//					codeCountMap.put(str, count);
//				});
//				map.put(item.getQuestionId(), codeCountMap);
//			} catch (Exception e) {
//				throw e;
//			}
//		}
//		/* 把 map List<Statistics> */
//		List<Statistics> list = new ArrayList<>();
//		map.forEach((k,v) -> {
//			/* v 就是 Map<code-optioName, count>> */
//			List<OptionsCount> opCountList = new ArrayList<>();
//			v.forEach((k1, v1) -> {
//				/* array = [code, optioName]*/
//				String[] array = k1.split("-");
//				/*array[0] 是選項編號(code)，要把其資料型態轉回 int*/
//				OptionsCount opCount = new OptionsCount(Integer.valueOf(array[0]), array[1], v1);
//				opCountList.add(opCount);
//			});
//			Statistics st = new Statistics(k, opCountList);
//			list.add(st);
//		});
//		return new StatisticsRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), list);
//	}
	public StatisticsRes statistics2(int quizId) throws Exception {
		/* res 包含了多位使用者(email)的填答 (fillin表中的所有符合quiz_id的資料列)*/
		List<Fillin> res = fillinDao.getByQuizId(quizId);
		/* Map<questionId, List<OptionsCount>>  => questionId、count、code、optionName => 每題questionId和統計結果*/
		Map<Integer, List<OptionsCount>> map = new HashMap<>();
		Set<String> uniqueEmails = new HashSet<>();
		for (Fillin item : res) {
			// 只要有 email 就加進去，重複的會被 Set 自動擋掉
		    if (item.getEmail() != null && !item.getEmail().isEmpty()) {
		        uniqueEmails.add(item.getEmail());
		    }
			try {
				/* 把字串 answer 轉換成物件 List<AnswerVo> 這邊一個 List<AnswerVo> 只包含了一個問題的 所有編號-選項 */
				// AnswerVo => check、code、optionName   item.getAnswer取得res每一個資料列中的answer欄位資料
				List<AnswerVo> voList = mapper.readValue(item.getAnswer(), new TypeReference<>() {
				});
				/* voList 轉成 List<OptionsCount>  => count、code、optionName */
				// 若questionId不存在map中，清空opCountList，若已存在則取該questionId對應資料
				List<OptionsCount> opCountList = CollectionUtils.isEmpty(map.get(item.getQuestionId()))
						? new ArrayList<>() : map.get(item.getQuestionId());

				// voList:被統計資料  opCountList:統計結果
				voList.forEach(vo -> {
					if(vo.getCode()==0) {
						return;  // 跳過簡答
					}
					/* 有選 */
					if (vo.isCheck()) {
	                    boolean isExist = false; // 標記該選項是否已存在於統計清單中
						/*第一筆資料 --> opCountList 是空的，不用 CollectionUtilsE.isEmpty() 判斷是因為前面已經把其設定為 new ArrayList<>()，
						 * 要使用也可以*/
	                    /* 遍歷目前的統計清單，找找看有沒有這個選項代碼 */
	                    for (OptionsCount op : opCountList) {
	                        if (op.getCode() == vo.getCode()) {
	                            op.setCount(op.getCount() + 1);
	                            isExist = true;
	                            break; // 找到就不用繼續找了
	                        }
	                    }
	                    if (!isExist) {
	                        // 假設第一次出現，次數為 1
	                        opCountList.add(new OptionsCount(vo.getCode(), vo.getOptionName(), 1));
	                    }
	                    
//						if(opCountList.isEmpty()) {
//							/* 因為是第一筆資料，所以有選的次數直接變成1*/
//							opCountList.add(new OptionsCount(vo.getCode(), vo.getOptionName(), 1));
//						} else {
//							/* 遍歷並比對相同編號*/
//							opCountList.forEach(op -> {
//								/* 比對相同編號 --> 取出 op 中的次數 --> +1 --> set 回去 */
//								if (op.getCode() == vo.getCode()) {
//									op.setCount(op.getCount() + 1);
//								}
//							});
//						}						
					}
				});
				map.put(item.getQuestionId(), opCountList);
			} catch (Exception e) {
				throw e;
			}
		}
		/* 把 map 轉成 List<Statistics>  => questionId和統計結果*/
		List<Statistics> list = new ArrayList<>();
		map.forEach((k, v) -> {
			list.add(new Statistics(k, v));
		});
		return new StatisticsRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), list, uniqueEmails.size());
	}
//	public StatisticsRes statistics3(int quizId) throws Exception {
//	    /* res 包含了多位使用者(email)的填答 */
//	    List<Fillin> res = fillinDao.getByQuizId(quizId);
//	    
//	    /* Map<questionId, List<OptionsCount>> */
//	    Map<Integer, List<OptionsCount>> map = new HashMap<>();
//
//	    for (Fillin item : res) {
//	        try {
//	            /* 把字串 answer 轉換成物件 List<AnswerVo> */
//	            List<AnswerVo> voList = mapper.readValue(item.getAnswer(), new TypeReference<List<AnswerVo>>() {});
//
//	            /* 取得目前該問題已統計的 List，若無則 new 一個新的 */
//	            List<OptionsCount> opCountList = map.get(item.getQuestionId());
//	            if (opCountList == null) {
//	                opCountList = new ArrayList<>();
//	            }
//
//	            /* 遍歷使用者的回答 */
//	            for (AnswerVo vo : voList) {
//	                /* 有選才統計 */
//	                if (vo.isCheck()) {
//	                    boolean isExist = false; // 標記該選項是否已存在於統計清單中
//
//	                    /* 遍歷目前的統計清單，找找看有沒有這個選項代碼 */
//	                    for (OptionsCount op : opCountList) {
//	                        if (op.getCode() == vo.getCode()) {
//	                            // 【Bug A 修復】: 這裡是累加次數，不是修改編號，改成 setCount
//	                            op.setCount(op.getCount() + 1);
//	                            isExist = true;
//	                            break; // 找到就不用繼續找了
//	                        }
//	                    }
//
//	                    /* 【Bug B 修復】: 如果跑完一輪都沒找到 (isExist 為 false)，代表這是新選項，要加入 */
//	                    if (!isExist) {
//	                        // 假設第一次出現，次數為 1
//	                        opCountList.add(new OptionsCount(vo.getCode(), vo.getOptionName(), 1));
//	                    }
//	                }
//	            }
//
//	            /* 更新 Map */
//	            map.put(item.getQuestionId(), opCountList);
//
//	        } catch (Exception e) {
//	            throw e;
//	        }
//	    }
//
//	    /* 把 map 轉成 List<Statistics> 回傳 */
//	    List<Statistics> list = new ArrayList<>();
//	    map.forEach((k, v) -> {
//	        list.add(new Statistics(k, v));
//	    });
//
//	    return new StatisticsRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), list);
//	}

}
