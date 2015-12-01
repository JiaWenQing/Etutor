package net.dx.etutor.data;

import java.util.HashMap;
import java.util.Map;

import android.support.v4.text.TextUtilsCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;

import net.dx.etutor.app.EtutorApplication;
import net.dx.etutor.model.DxNeed;
import net.dx.etutor.model.DxStatistic;
import net.dx.etutor.util.Constants;
import net.dx.etutor.util.StrUtil;

public class UrlEngine {
	/**
	 * 获取订单详情
	 * 
	 * @return
	 */
	public static String getDetailOrder(String orderId, String orderType) {
		DataParam param = new DataParam();
		param.setApi("getDetailOrder");
		param.addParam("orderId", orderId);
		param.addParam("orderType", orderType);
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		return param.getUrlStr();
	}

	/**
	 * 开启推送
	 * 
	 * @return
	 */
	public static String registNotifi() {
		DataParam param = new DataParam();
		param.setApi("registNotification");
		param.addParam("deviceId", EtutorApplication.getInstance().getSpUtil()
				.getDeviceId());
		return param.getUrlStr();
	}

	/**
	 * 关闭推送
	 * 
	 * @return
	 */
	public static String logoutNotifi() {
		DataParam param = new DataParam();
		param.setApi("logoutNotification");
		return param.getUrlStr();
	}

	/**
	 * 登陆
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public static String userLogin(String userName, String password) {
		DataParam param = new DataParam();
		param.setApi("loginUsers");
		param.addParam("telephone", userName);
		param.addParam("password", password);
		return param.getUrlStr();
	}

	/**
	 * 获取用户信息,关联教师信息
	 * 
	 * @param userId
	 * @return
	 */
	public static String getUserDetail(String userId) {
		DataParam param = new DataParam();
		param.setApi("getDetailUsers");
		param.addParam("userId", userId);
		return param.getUrlStr();
	}

	/**
	 * 注册
	 * 
	 * @param map
	 * @return
	 */
	public static String userRegister(String telephone, String password,
			String verifyCode,String inviteCode) {
		DataParam param = new DataParam();
		param.setApi("registerUsers");
		param.addParam("telephone", telephone);
		param.addParam("password", password);
		param.addParam("verifyCode", verifyCode);
		if (!TextUtils.isEmpty(inviteCode)&&inviteCode.length()==4) {
			param.addParam("inviteCode", inviteCode);
		}
		return param.getUrlStr();
	}

	/**
	 * 注册
	 * 
	 * @param map
	 * @return
	 */
	public static String completeUserInfo(Map<String, Object> map) {
		DataParam param = new DataParam();
		param.setApi("completeUsers");
		if (!TextUtils.isEmpty(EtutorApplication.getInstance().getSpUtil()
				.getUserId())) {
			param.addParam("userId", EtutorApplication.getInstance()
					.getSpUtil().getUserId());
		}
		param.addParam("userName", map.get("userName"));
		param.addParam("longitude", map.get("longitude"));
		param.addParam("latitude", map.get("latitude"));
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		param.addParam("userType", map.get("userType"));
		return param.getUrlStr();
	}

	/**
	 * 完善教师信息
	 * 
	 * @param map
	 * @return
	 */
	public static String completeTeacherInfo(Map<String, Object> map) {
		DataParam param = new DataParam();
		param.setApi("completeUsers");
		if (!TextUtils.isEmpty(EtutorApplication.getInstance().getSpUtil()
				.getUserId())) {
			param.addParam("userId", EtutorApplication.getInstance()
					.getSpUtil().getUserId());
		}
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		if (!TextUtils.isEmpty((String) map.get("coachTime"))) {
			param.addParam("coachTime",
					StrUtil.getCoachTime(map.get("coachTime").toString()));
		}
		param.addParam("school", map.get("school"));
		param.addParam("userType", map.get("userType"));
		param.addParam("education", map.get("education"));
		param.addParam("major", map.get("major"));
		param.addParam("teacherBirthday", map.get("tBirthday"));
		param.addParam("graduateTime", map.get("graduateTime"));
		return param.getUrlStr();
	}

	public static String userRegister() {
		DataParam param = new DataParam();
		param.setApi("registerUsers");
		return param.getUrlStr();
	}

	/**
	 * 设置个人信息
	 * 
	 * @param map
	 * @return
	 */
	public static String setPersonInfo(Map<String, Object> map) {
		DataParam param = new DataParam();
		param.setApi("updateUsers");
		param.addParam("userId", map.get("userId"));
		param.addParam("userName", map.get("userName"));
		param.addParam("telephone", map.get("phone"));
		param.addParam("address", map.get("address"));
		param.addParam("sex", map.get("sex"));
		param.addParam("longitude", map.get("longitude"));
		param.addParam("latitude", map.get("latitude"));

		param.addParam("school", map.get("school"));
		param.addParam("education", map.get("education"));
		param.addParam("graduateTime", map.get("graduateTime"));
		if (!TextUtils.isEmpty((String) map.get("coachTime"))) {
			param.addParam("coachTime",
					StrUtil.getCoachTime(map.get("coachTime").toString()));
		}
		if (!TextUtils.isEmpty((String) map.get("teacherBirthday"))) {
			param.addParam("teacherBirthday", map.get("teacherBirthday"));
		}
		param.addParam("major", map.get("major"));

		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		return param.getUrlStr();
	}

	/**
	 * 添加需求
	 * 
	 * @param map
	 * @return
	 */
	public static String addStudentNeed(DxNeed need) {
		DataParam param = new DataParam();
		param.setApi("releaseNeed");
		if (!need.getNeedId().equals("-1")) {
			param.addParam("needId", need.getNeedId());
		}
		param.addParam("userId", need.getUserId());
		param.addParam("needTitle", need.getNeedTitle());
		param.addParam("subjectItemId", need.getSubjectItemId());
		param.addParam("introduce", need.getIntroduce());
		param.addParam("price", need.getPrice());
		param.addParam("subtime", need.getSubtime());
		param.addParam("teacherSex", need.getTeacherSex());
		param.addParam("teachMode", need.getTeachMode());
		param.addParam("lectureMode", need.getLectureMode());
		param.addParam("tradeNumber", need.getTradeNumber());
		param.addParam("publishTime", need.getPublishTime());
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		return param.getUrlStr();
	}

	/**
	 * 设置课程信息
	 * 
	 * @param map
	 * @return
	 */
	public static String setCourseInfo(Map<String, Object> map) {
		DataParam param = new DataParam();
		param.setApi("insertCourse");
		param.addParam("teacherId", map.get("teacherId"));
		if (!TextUtils.isEmpty((String) map.get("subjectItemId1"))) {
			param.addParam("subjectItemId1", map.get("subjectItemId1"));
		}
		if (!TextUtils.isEmpty((String) map.get("subjectItemId2"))) {
			param.addParam("subjectItemId2", map.get("subjectItemId2"));
		}
		if (!TextUtils.isEmpty((String) map.get("subjectItemId3"))) {
			param.addParam("subjectItemId3", map.get("subjectItemId3"));
		}
		param.addParam("price", map.get("price"));
		param.addParam("listenTest", map.get("listenTest"));
		param.addParam("shield", map.get("shield"));
		param.addParam("lectureType", map.get("lectureType"));
		param.addParam("subtime", map.get("subtime"));
		param.addParam("introduce", map.get("introduce"));
		if (!TextUtils.isEmpty((String) map.get("province"))
				&& !map.get("province").equals("请选择")) {
			param.addParam("province", map.get("province"));
		}
		if (!TextUtils.isEmpty((String) map.get("city"))
				&& !map.get("city").equals("请选择")) {
			param.addParam("city", map.get("city"));
		}
		if (!TextUtils.isEmpty((String) map.get("region"))
				&& !map.get("region").equals("请选择")) {
			param.addParam("region", map.get("region"));
		}
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		return param.getUrlStr();
	}

	/**
	 * 设置小班
	 * 
	 * @param map
	 * @return
	 */
	public static String setClassInfo(Map<String, Object> map) {
		DataParam param = new DataParam();
		param.setApi("insertClass");
		if (!TextUtils.isEmpty((String) map.get("title"))) {
			param.addParam("title", map.get("title"));
		}
		param.addParam("shield", map.get("shield"));
		param.addParam("teacherId", map.get("teacherId"));
		param.addParam("subjectItemId", map.get("subjectItemId"));
		param.addParam("introduce", map.get("introduce"));
		param.addParam("listenTest", map.get("listenTest"));
		param.addParam("classSize", map.get("classSize"));
		param.addParam("price", map.get("price"));
		param.addParam("beginTime", map.get("beginTime"));
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		return param.getUrlStr();
	}

	public static String setComment(Map<String, Object> map) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("insertComment");
		param.addParam("fromUserId", map.get("fromUserId"));
		param.addParam("toUserId", map.get("toUserId"));
		param.addParam("orderId", map.get("orderId"));
		param.addParam("content", map.get("content"));
		param.addParam("starLevel", map.get("starLevel"));
		return param.getUrlStr();
	}

	/**
	 * 查询学校信息
	 * 
	 * @param map
	 * @return
	 */
	public static String searchSchoolinfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("getListSchool");
		if (!TextUtils.isEmpty((String) map.get("name"))) {
			param.addParam("name", map.get("name"));
		}
		if (!TextUtils.isEmpty((String) map.get("province"))
				&& !map.get("province").equals("请选择")) {
			param.addParam("province", map.get("province"));
		}
		if (!TextUtils.isEmpty((String) map.get("city"))
				&& !map.get("city").equals("请选择")) {
			param.addParam("city", map.get("city"));
		}
		if (!TextUtils.isEmpty((String) map.get("region"))
				&& !map.get("region").equals("请选择")) {
			param.addParam("region", map.get("region"));
		}
		if (!TextUtils.isEmpty((String) map.get("distance"))) {
			param.addParam("distance", map.get("distance"));
		}
		if (!TextUtils.isEmpty((String) map.get("latitude"))) {
			param.addParam("latitude", map.get("latitude"));
		}
		if (!TextUtils.isEmpty((String) map.get("longitude"))) {
			param.addParam("longitude", map.get("longitude"));
		}
		if (!TextUtils.isEmpty((String) map.get("category"))) {
			param.addParam("category", map.get("category"));
		}
		if (!TextUtils.isEmpty((String) map.get("type"))) {
			param.addParam("type", map.get("type"));
		}
		param.addParam("pageSize", map.get("pageSize"));
		param.addParam("start", map.get("start"));
		return param.getUrlStr();
	}

	/**
	 * 查询机构信息
	 * 
	 * @param map
	 * @return
	 */
	public static String searchAgencyinfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("getListAgency");
		if (!TextUtils.isEmpty((String) map.get("name"))) {
			param.addParam("name", map.get("name"));
		}
		if (!TextUtils.isEmpty((String) map.get("province"))
				&& !map.get("province").equals("请选择")) {
			param.addParam("province", map.get("province"));
		}
		if (!TextUtils.isEmpty((String) map.get("city"))
				&& !map.get("city").equals("请选择")) {
			param.addParam("city", map.get("city"));
		}
		if (!TextUtils.isEmpty((String) map.get("region"))
				&& !map.get("region").equals("请选择")) {
			param.addParam("region", map.get("region"));
		}
		if (!TextUtils.isEmpty((String) map.get("classify"))
				&& !map.get("classify").equals("全部")) {
			param.addParam("type", map.get("classify"));
		}
		if (!TextUtils.isEmpty((String) map.get("distance"))) {
			param.addParam("distance", map.get("distance"));
		}
		if (!TextUtils.isEmpty((String) map.get("latitude"))) {
			param.addParam("latitude", map.get("latitude"));
		}
		if (!TextUtils.isEmpty((String) map.get("longitude"))) {
			param.addParam("longitude", map.get("longitude"));
		}
		param.addParam("pageSize", map.get("pageSize"));
		param.addParam("start", map.get("start"));
		return param.getUrlStr();
	}

	/**
	 * 获取学生需求列表
	 * 
	 * @param userId
	 * @return
	 */
	public static String studentNeedList(String userId) {
		DataParam param = new DataParam();
		param.setApi("ownNeed");
		param.addParam("userId", userId);
		return param.getUrlStr();
	}

	/**
	 * 获取学生需求列表
	 * 
	 * @param userId
	 * @return
	 */
	public static String needStudentList(Map<String, Object> map) {
		DataParam param = new DataParam();
		param.setApi("ownoNeed");
		if (!TextUtils.isEmpty((String) map.get("subject"))) {
			param.addParam("subjectItemId", map.get("subject"));
		}
		if (!TextUtils.isEmpty((String) map.get("province"))
				&& !map.get("province").equals("请选择")) {
			param.addParam("province", map.get("province"));
		}
		if (!TextUtils.isEmpty((String) map.get("city"))
				&& !map.get("city").equals("请选择")) {
			param.addParam("city", map.get("city"));
		}
		if (!TextUtils.isEmpty((String) map.get("region"))
				&& !map.get("region").equals("请选择")) {
			param.addParam("region", map.get("region"));
		}
		if (!TextUtils.isEmpty((String) map.get("distance"))) {
			param.addParam("distance", map.get("distance"));
			param.addParam("latitude", map.get("latitude"));
			param.addParam("longitude", map.get("longitude"));
		}

		if (map.get("orderType").equals("identify asc,id asc")) {
			param.addParam("orderType", "identify asc,needId asc");
		} else if (map.get("orderType").equals("identify desc,id desc")) {
			param.addParam("orderType", "identify desc,needId desc");
		} else {
			param.addParam("orderType", map.get("orderType"));
		}
		param.addParam("start", map.get("start"));
		param.addParam("pageSize", map.get("pageSize"));
		param.addParam("userId", map.get("userId"));
		return param.getUrlStr();
	}

	/**
	 * 获取学生需求详情
	 * 
	 * @param needId
	 * @param userId
	 *            判断是否收藏过
	 * @return
	 */
	public static String needStudent(String needId, String userId) {
		DataParam param = new DataParam();
		param.setApi("ownoNeed");
		param.addParam("needId", needId);
		param.addParam("userId", userId);
		param.addParam("start", 0);
		param.addParam("pageSize", 1);
		return param.getUrlStr();
	}

	/**
	 * 屏蔽需求
	 * 
	 * @param needId
	 * @return
	 */
	public static String shieldNeed(String needId, String userId) {
		DataParam param = new DataParam();
		param.setApi("shieldNeed");
		param.addParam("needId", needId);
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		param.addParam("userId", userId);
		return param.getUrlStr();
	}

	/**
	 * 重新发布
	 * 
	 * @param needId
	 * @return
	 */
	public static String releaseAgain(String needId, String userId) {
		DataParam param = new DataParam();
		param.setApi("releaseAgainNeed");
		param.addParam("needId", needId);
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		param.addParam("userId", userId);
		return param.getUrlStr();
	}

	/**
	 * 获取用户订单列表的Url
	 * 
	 * @param userId
	 * @return
	 */
	public static String getOrderListUrl(String userId) {
		DataParam param = new DataParam();
		param.setApi("listOrder");
		param.addParam("fromUserId", userId);
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		return param.getUrlStr();
	}

	/**
	 * 生成订单
	 * 
	 * @param fullTime 1 专职教师 0 非专职教师
	 * @return
	 */
	public static String insertOrder(Map<String, Object> map) {
		DataParam param = new DataParam();
		param.setApi("insertOrder");
		param.addParam("orderId", map.get("orderId"));
		param.addParam("fromUserId", map.get("fromUserId"));
		param.addParam("toUserId", map.get("toUserId"));
		param.addParam("needId", map.get("needId"));
		param.addParam("firstCourseTime", map.get("firstCourseTime"));
		if ((int)map.get("fullTime")==1) {
			param.addParam("courseNumber", map.get("courseNumber"));
			param.addParam("coursePrice", map.get("coursePrice"));
			param.addParam("totalPrice", map.get("totalPrice"));
			param.addParam("accountNumber", map.get("accountNumber"));
		}
		return param.getUrlStr();
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public static String getTeacherList(Map<String, Object> map) {
		DataParam param = new DataParam();
		param.setApi("getTeacherList");
		if (!TextUtils.isEmpty((String) map.get("userId"))) {
			param.addParam("userId", map.get("userId"));
		}
		if (!TextUtils.isEmpty((String) map.get("subject"))) {
			param.addParam("subjectItemId", map.get("subject"));
		}
		if (!TextUtils.isEmpty((String) map.get("province"))
				&& !map.get("province").equals("请选择")) {
			param.addParam("province", map.get("province"));
		}
		if (!TextUtils.isEmpty((String) map.get("city"))
				&& !map.get("city").equals("请选择")) {
			param.addParam("city", map.get("city"));
		}
		if (!TextUtils.isEmpty((String) map.get("region"))
				&& !map.get("region").equals("请选择")) {
			param.addParam("region", map.get("region"));
		}
		if (!TextUtils.isEmpty((String) map.get("distance"))) {
			param.addParam("distance", map.get("distance"));
			param.addParam("latitude", map.get("latitude"));
			param.addParam("longitude", map.get("longitude"));
		}
		if (!TextUtils.isEmpty((String) map.get("lectureType"))) {
			param.addParam("lectureType", map.get("lectureType"));
		}
		if (!TextUtils.isEmpty((String) map.get("fullTime"))) {
			param.addParam("fullTime", map.get("fullTime"));
		}
		param.addParam("start", map.get("start"));
		param.addParam("pageSize", map.get("pageSize"));
		param.addParam("orderType", map.get("orderType"));
		return param.getUrlStr();
	}

	/**
	 * 查看老师详情
	 * 
	 * @param userId
	 * @param teacherId
	 * @return
	 */
	public static String getTeacherInfo(String userId, String teacherId) {
		DataParam param = new DataParam();
		param.setApi("getTeacherList");
		param.addParam("userId", userId);
		param.addParam("id", teacherId);
		param.addParam("start", 0);
		param.addParam("pageSize", 1);
		return param.getUrlStr();
	}

	/**
	 * 私信列表
	 * 
	 * @param userId
	 * @return
	 */
	public static String getMessageList(Map<String, Object> map) {
		DataParam param = new DataParam();
		param.setApi("getListPrivateMsg");
		param.addParam("senderId", map.get("userId"));
		param.addParam("start", map.get("start"));
		param.addParam("pageSize", map.get("pageSize"));
		return param.getUrlStr();
	}

	/**
	 * 私信详情列表
	 * 
	 * @param userId
	 * @return
	 */
	public static String getMessageDetail(Map<String, Object> map) {
		DataParam param = new DataParam();
		param.setApi("getDetailPrivateMsg");
		param.addParam("senderId", map.get("senderId"));
		param.addParam("receiverId", map.get("receiverId"));
		param.addParam("start", map.get("start"));
		param.addParam("pageSize", map.get("pageSize"));
		return param.getUrlStr();
	}

	/**
	 * 发送消息
	 * 
	 * @param map
	 * @return
	 */
	public static String sendMessage(Map<String, Object> map) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("insertPrivateMsg");
		param.addParam("senderId", map.get("senderId"));
		param.addParam("receiverId", map.get("receiverId"));
		param.addParam("message", map.get("message"));
		param.addParam("id", map.get("msgId"));
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		return param.getUrlStr();
	}

	/**
	 * 获取系统消息
	 * 
	 * @param userId
	 * @return
	 */
	public static String getSysMessageList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("getListSystem");
		param.addParam("start", map.get("start"));
		param.addParam("pageSize", map.get("pageSize"));
		return param.getUrlStr();
	}

	/**
	 * 小班报名
	 * 
	 * @param userId
	 * @param teacherId
	 * @return
	 */
	public static String applyClass(String userId, String teacherId) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("applySmallClass");
		param.addParam("fromUserId", userId);
		param.addParam("toUserId", teacherId);
		return param.getUrlStr();
	}

	/**
	 * 老师详情
	 * 
	 * @param userId
	 * @return
	 */
	public static String getTeacherDetail(String userId) {
		DataParam param = new DataParam();
		param.setApi("getDetailTeacher");
		param.addParam("userId", userId);
		return param.getUrlStr();
	}

	/**
	 * 课程详情
	 * 
	 * @param teacherId
	 * @return
	 */
	public static String getCourseDetail(String teacherId) {
		DataParam param = new DataParam();
		param.setApi("getDetailCourse");
		param.addParam("teacherId", teacherId);
		return param.getUrlStr();
	}

	/**
	 * 小班详情
	 * 
	 * @param teacherId
	 * @return
	 */
	public static String getClassDetail(String teacherId) {
		DataParam param = new DataParam();
		param.setApi("getDetailClass");
		param.addParam("teacherId", teacherId);
		return param.getUrlStr();
	}

	/**
	 * 获取评论列表
	 * 
	 * @param teacherId
	 * @return
	 */
	public static String getCommentList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("getListComment");
		param.addParam("toUserId", map.get("toUserId"));
		param.addParam("start", map.get("start"));
		param.addParam("pageSize", map.get("pageSize"));
		return param.getUrlStr();
	}

	/**
	 * 意见反馈
	 * 
	 * @param useId
	 * @param feedback
	 * @return
	 */
	public static String feedback(String userId, String feedback) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.addParam("senderId", userId);
		param.addParam("receiverId", "");
		param.addParam("message", feedback);
		param.addParam("dialogueId", "");
		param.addParam("status", "1");

		return param.getUrlStr();
	}

	/**
	 * 获取收藏列表
	 * 
	 * @param userId
	 * @return
	 */
	public static String getCollectList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("getListCollect");
		param.addParam("userId", map.get("userId"));
		param.addParam("start", map.get("start"));
		param.addParam("pageSize", map.get("pageSize"));
		return param.getUrlStr();
	}

	/**
	 * 添加收藏
	 * 
	 * @param userId
	 * @param type
	 * @param needId
	 * @param courseId
	 * @param classId
	 * @param toUserId
	 * @return
	 */
	public static String insertCollect(String userId, String needId,
			String toUserId) {
		DataParam param = new DataParam();
		param.setApi("insertCollect");
		param.addParam("userId", userId);
		param.addParam("needId", needId);
		param.addParam("toUserId", toUserId);
		return param.getUrlStr();
	}

	/**
	 * 取消收藏
	 * 
	 * @param collectId
	 * @return
	 */
	public static String deleteCollect(String collectId) {
		DataParam param = new DataParam();
		param.setApi("deleteCollect");
		param.addParam("id", collectId);
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		return param.getUrlStr();
	}

	/**
	 * 点赞
	 * 
	 * @return
	 */
	public static String belaudReply(String userId, int replyId) {
		DataParam param = new DataParam();
		param.setApi("belaudReply");
		param.addParam("userId", userId);
		param.addParam("replyId", replyId);
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		return param.getUrlStr();
	}

	/**
	 * 
	 * @return
	 */
	public static String getUuid() {
		DataParam param = new DataParam();
		param.setApi("uuidUsers");
		return param.getUrlStr();
	}

	/**
	 * 获取会话Id
	 * 
	 * @param userId
	 * @param receiverId
	 * @return
	 */
	public static String getDialogueId(String userId, String receiverId) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("getDialoguePrivateMsg");
		param.addParam("senderId", userId);
		param.addParam("receiverId", receiverId);
		return param.getUrlStr();
	}

	/**
	 * 检查版本信息
	 * 
	 * @return
	 */
	public static String checkVersionInfo() {
		DataParam param = new DataParam();
		param.setApi("checkVersion");
		param.addParam("channel", Constants.CHANNEL);
		return param.getUrlStr();
	}

	/**
	 * 图片上传
	 * 
	 * @return
	 */
	public static String uploadPic() {
		DataParam param = new DataParam();
		param.setApi("uploadCertificate");
		if (!TextUtils.isEmpty(EtutorApplication.getInstance().getSpUtil()
				.getUserId())) {
			param.addParam("userId", EtutorApplication.getInstance()
					.getSpUtil().getUserId());
		}
		return param.getUrlStr();
	}

	/**
	 * 语音上传
	 * 
	 * @return
	 */
	public static String uploadRecord() {
		DataParam param = new DataParam();
		param.setApi("uploadRecord");
		return param.getUrlStr();
	}

	/**
	 * 语音上传
	 * 
	 * @return
	 */
	public static String uploadToppic() {
		DataParam param = new DataParam();
		param.setApi("uploadTopic");
		return param.getUrlStr();
	}

	/**
	 * 头像上传
	 * 
	 * @return
	 */
	public static String uploadHead() {
		DataParam param = new DataParam();
		param.setApi("uploadHead");
		if (!TextUtils.isEmpty(EtutorApplication.getInstance().getSpUtil()
				.getUserId())) {
			param.addParam("userId", EtutorApplication.getInstance()
					.getSpUtil().getUserId());
		}
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		return param.getUrlStr();
	}

	/**
	 * 获取上传列表
	 * 
	 * @param userId
	 * @return
	 */
	public static String getCertificate(String userId) {
		DataParam param = new DataParam();
		param.setApi("getListCertificate");
		param.addParam("userId", userId);
		return param.getUrlStr();
	}

	/**
	 * 修改图片
	 * 
	 * @param id
	 * @param summary
	 * @return
	 */
	public static String updateCertificate(String id, String summary) {
		DataParam param = new DataParam();
		param.setApi("updateCertificate");
		param.addParam("id", id);
		param.addParam("summary", summary);
		return param.getUrlStr();
	}

	/**
	 * 删除上传
	 * 
	 * @param id
	 * @return
	 */
	public static String deleteCertificate(String id, int count) {
		DataParam param = new DataParam();
		param.setApi("deleteCertificate");
		// param.addParam("userId",
		// EtutorApplication.getInstance().getSpUtil().getUserId());
		param.addParam("summary", id);
		// param.addParam("count", count);
		return param.getUrlStr();
	}

	/**
	 * 修改用户信息
	 * 
	 * @return
	 */
	public static String updateUser(Map<String, Object> map) {
		DataParam param = new DataParam();
		param.setApi("updateUsers");
		param.addParam("userId", map.get("userId"));
		param.addParam("userType", map.get("userType"));
		param.addParam("userName", map.get("userName"));
		param.addParam("telephone", map.get("phone"));
		param.addParam("address", map.get("address"));
		param.addParam("longitude", map.get("longitude"));
		param.addParam("latitude", map.get("latitude"));

		param.addParam("school", map.get("school"));
		param.addParam("education", map.get("education"));
		param.addParam("major", map.get("major"));
		return param.getUrlStr();
	}

	/**
	 * 
	 * @param userId
	 * @param token
	 * @return
	 */
	public static String refreshUser() {
		DataParam param = new DataParam();
		param.setApi("refreshUsers");
		if (!TextUtils.isEmpty(EtutorApplication.getInstance().getSpUtil()
				.getUserId())) {
			param.addParam("userId", EtutorApplication.getInstance()
					.getSpUtil().getUserId());
		}
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		return param.getUrlStr();
	}

	/**
	 * 忘记密码(提交)
	 * 
	 * @param phone
	 * @param passwrodMaker
	 * @param verifyCode
	 * @return
	 */
	public static String forgetPassword(String phone, String passwrodMaker,
			String verifyCode) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("updatePasswordUsers");
		param.addParam("telephone", phone);
		param.addParam("password", passwrodMaker);
		param.addParam("verifyCode", verifyCode);
		return param.getUrlStr();
	};

	/**
	 * 发送验证码(注册)
	 * 
	 * @param telephone
	 * @return
	 */
	public static String sendCode(String telephone) {
		DataParam param = new DataParam();
		param.setApi("sendCodeUsers");
		param.addParam("telephone", telephone);
		return param.getUrlStr();
	}

	/**
	 * 发送验证码(忘记密码)
	 * 
	 * @param telephone
	 * @return
	 */
	public static String updateCode(String telephone) {
		DataParam param = new DataParam();
		param.setApi("updateCodeUsers");
		param.addParam("telephone", telephone);
		return param.getUrlStr();
	}

	/**
	 * 发送验证码(QQ第三方绑定)
	 * 
	 * @param mPhone
	 * @return
	 */
	public static String bindqqCode(String telephone) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("bindqqCodeUsers");
		param.addParam("telephone", telephone);
		return param.getUrlStr();
	}

	/**
	 * 发送验证码(Sina第三方绑定)
	 * 
	 * @param mPhone
	 * @return
	 */
	public static String bindsinaCode(String telephone) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("bindsinaCodeUsers");
		param.addParam("telephone", telephone);
		return param.getUrlStr();
	}

	/**
	 * 验证第三方登录(未登录)
	 * 
	 * @param id
	 *            (openId.sinaOpenId)
	 * @return
	 */
	public static String getOpenId(String openId, String sinaOpenId) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("checkBindUsers");

		param.addParam("qqOpenId", openId);
		param.addParam("sinaOpenId", sinaOpenId);
		return param.getUrlStr();
	}

	/**
	 * 验证第三方登录(已登录)
	 * 
	 * @param id
	 *            (openId.sinaOpenId)
	 * @return
	 */
	public static String getOpenIdLogin(String userId, String openId,
			String sinaOpenId) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("loginCheckBindUsers");

		param.addParam("userId", userId);
		param.addParam("qqOpenId", openId);
		param.addParam("sinaOpenId", sinaOpenId);
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		return param.getUrlStr();
	}

	/**
	 * 第三方账号绑定
	 * 
	 * @param phone
	 * @param passwrodMaker
	 * @param code
	 * @param qQopenId
	 * @param sinaopenId
	 * @return
	 */
	public static String bindRegister(String telephone, String passwrodMaker,
			String code, String qQopenId, String sinaopenId) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("bindRegisterUsers");
		param.addParam("telephone", telephone);
		param.addParam("verifyCode", code);
		param.addParam("password", passwrodMaker);
		param.addParam("qqOpenId", qQopenId);
		param.addParam("sinaOpenId", sinaopenId);
		return param.getUrlStr();
	}

	/**
	 * updateOrderStatus
	 * 
	 * @param orderId
	 * @param status
	 * @return
	 */
	public static String updateOrder(String orderId, String status) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("updateOrder");
		param.addParam("orderId", orderId);
		param.addParam("status", status);
		return param.getUrlStr();
	}

	/**
	 * 纠正学校 机构信息
	 * 
	 * @param map
	 * @return
	 */
	public static String correctionInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("insertCorrection");
		param.addParam("correctId", map.get("id"));
		param.addParam("userId", map.get("userId"));
		param.addParam("type", map.get("type"));
		param.addParam("address", map.get("address"));
		param.addParam("phoneNumber", map.get("phone"));
		param.addParam("webSite", map.get("web"));
		param.addParam("schoolCategory", map.get("property"));
		param.addParam("moreMessage", map.get("more"));
		return param.getUrlStr();
	}

	/**
	 * 预约快照
	 * 
	 * @param map
	 * @return
	 */
	public static String getInstantaneInfo(String orderId) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("getDetailSpts");
		param.addParam("orderId", orderId);
		return param.getUrlStr();
	}

	public static String uploadStatisticInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("uploadStatistics");
		param.addParam("type", "Android");
		param.addParam("deviceId", EtutorApplication.getInstance().getImei());
		param.addParam("versionNumber", EtutorApplication.getInstance()
				.getVersionNumber());
		param.addParam("phoneModel", android.os.Build.MODEL);
		param.addParam("systemVersion", android.os.Build.VERSION.RELEASE);

		param.addParam("jstr", map.get("jstr"));
		return param.getUrlStr();
	}

	public static String getSystemMessage() {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("getListSystem");
		param.addParam("type", 0);
		param.addParam("start", 0);
		param.addParam("pageSize", 10);

		return param.getUrlStr();
	}

	public static String getBanner() {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("getListSystem");
		param.addParam("type", 1);
		param.addParam("start", 0);
		param.addParam("pageSize", 4);

		return param.getUrlStr();
	}

	public static String getCardList() {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("getCardTeacherList");
		param.addParam("province", EtutorApplication.getInstance().getSpUtil()
				.getLocationCity());
		param.addParam("type", EtutorApplication.getInstance().getSpUtil()
				.getCardType());
		if (!TextUtils.isEmpty(EtutorApplication.getInstance().getSpUtil()
				.getUserId())) {
			param.addParam("userId", EtutorApplication.getInstance()
					.getSpUtil().getUserId());
		}
		return param.getUrlStr();
	}

	/**
	 * 我收藏的帖子
	 * 
	 * @param map
	 * @return
	 */
	public static String getPostsCollectList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("getCollectListTopic");
		if (!TextUtils.isEmpty(EtutorApplication.getInstance().getSpUtil()
				.getUserId())) {
			param.addParam("userId", EtutorApplication.getInstance()
					.getSpUtil().getUserId());
		}
		param.addParam("pageSize", map.get("pageSize"));
		param.addParam("start", map.get("start"));
		return param.getUrlStr();
	}

	/**
	 * 我发布的帖子
	 * 
	 * @param map
	 * @return
	 */
	public static String getPostsIssueList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("getListTopic");
		param.addParam("orderType", "new");
		if (!TextUtils.isEmpty(EtutorApplication.getInstance().getSpUtil()
				.getUserId())) {
			param.addParam("userId", EtutorApplication.getInstance()
					.getSpUtil().getUserId());
		}
		param.addParam("pageSize", map.get("pageSize"));
		param.addParam("start", map.get("start"));
		return param.getUrlStr();
	}

	/**
	 * 删除我的帖子
	 * 
	 * @param collectId
	 * @return
	 */
	public static String deletePosts(int id) {
		DataParam param = new DataParam();
		param.setApi("removeReply");
		param.addParam("id", id);
		param.addParam("userId", EtutorApplication.getInstance().getSpUtil()
				.getUserId());
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		return param.getUrlStr();
	}

	/**
	 * 获取版块列表
	 */
	public static String getBoardList() {
		DataParam param = new DataParam();
		param.setApi("getListBoard");
		if (!TextUtils.isEmpty(EtutorApplication.getInstance().getSpUtil()
				.getUserId())) {
			param.addParam("userId", EtutorApplication.getInstance()
					.getSpUtil().getUserId());
		}
		return param.getUrlStr();
	}
	/**
	 * 帖子添加
	 * 
	 * @param title
	 * @param content
	 * @param boardId
	 * @return
	 */
	public static String insertTopic(String title, String content,
			String boardId) {
		DataParam param = new DataParam();
		param.setApi("insertTopic");
		if (!TextUtils.isEmpty(EtutorApplication.getInstance().getSpUtil()
				.getUserId())) {
			param.addParam("userId", EtutorApplication.getInstance()
					.getSpUtil().getUserId());
		}
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		param.addParam("title", title);
		param.addParam("content", content);
		param.addParam("boardId", boardId);
		return param.getUrlStr();
	}

	/**
	 * 帖子回复
	 * 
	 * @param title
	 * @param content
	 * @param boardId
	 * @return
	 */
	public static String insertReply(String content, String topicId,
			String replyId) {
		DataParam param = new DataParam();
		param.setApi("insertReply");
		param.addParam("replyUserId", EtutorApplication.getInstance()
				.getSpUtil().getUserId());
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		param.addParam("replyId", replyId);
		param.addParam("content", content);
		param.addParam("topicId", topicId);
		return param.getUrlStr();
	}

	/**
	 * 帖子回复列表
	 * 
	 * @param type
	 *            1:1级回复 2:2级回复
	 * @param map
	 * @return
	 */
	public static String getRelpyList(int type, HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		DataParam param = new DataParam();
		param.setApi("getListReply");
		if (type == 2) {
			param.addParam("replyIndex", map.get("replyIndex"));
		}
		param.addParam("topicId", map.get("topicId"));
		param.addParam("pageSize", map.get("pageSize"));
		param.addParam("start", map.get("start"));
		if (null != map.get("status") && (int) map.get("status") != 0) {
			param.addParam("status", map.get("status"));
		}
		if (!TextUtils.isEmpty(EtutorApplication.getInstance().getSpUtil()
				.getUserId())) {
			param.addParam("userId", EtutorApplication.getInstance()
					.getSpUtil().getUserId());
		}
		return param.getUrlStr();
	}

	// http://10.0.6.251:8080/AppServer/getListTopic.jspx?uuid=468906e927d2dd765a02d83c77c988f4&boardId=2&pageSize=10
	/**
	 * 获取帖子列表
	 * 
	 * @return
	 */
	public static String getTopicList(HashMap<String, Object> map) {
		DataParam param = new DataParam();
		param.setApi("getListTopic");
		param.addParam("pageSize", map.get("pageSize"));
		param.addParam("start", map.get("start"));
		// 最新 new 最热 hot 精华 digest
		if (!TextUtils.isEmpty((String) map.get("orderType"))) {
			param.addParam("orderType", map.get("orderType"));
		}
		if (!TextUtils.isEmpty((String) map.get("title"))) {
			param.addParam("title", map.get("title"));
		}
		if (!TextUtils.isEmpty(EtutorApplication.getInstance().getSpUtil()
				.getUserId())) {
			param.addParam("userId", EtutorApplication.getInstance()
					.getSpUtil().getUserId());
		}
		param.addParam("boardId", map.get("boardId"));
		return param.getUrlStr();
	}

	/**
	 * 举报
	 */
	public static String getReport(HashMap<String, Object> map) {
		DataParam param = new DataParam();
		param.setApi("replyReport");
		param.addParam("reportType", map.get("reportType"));// 类型
		param.addParam("reportContent", map.get("reportContent"));// 内容
		param.addParam("userId", EtutorApplication.getInstance().getSpUtil()
				.getUserId());
		param.addParam("replyId", map.get("replyId"));//
		param.addParam("token", EtutorApplication.getInstance().getSpUtil()
				.getToken());
		return param.getUrlStr();
	}

}
